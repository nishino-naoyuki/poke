package com.example.logviewe.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.logviewe.Exception.NotInitializeException;
import com.example.logviewe.param.BattleAreaDto;
import com.example.logviewe.param.CardDto;
import com.example.logviewe.param.FieldDto;
import com.example.logviewe.param.GameInfo;
import com.example.logviewe.param.Hand;
import com.example.logviewe.param.LogConst;
import com.example.logviewe.param.Play;
import com.example.logviewe.param.PlayId;
import com.example.logviewe.param.PlayerDto;
import com.example.logviewe.param.Turn;
import com.example.logviewe.param.TurnList;
import com.example.logviewe.param.input.InputData;
import com.example.logviewe.param.play.Draw;
import com.example.logviewe.param.play.PlayDetail;
import com.example.logviewe.service.play.PlayAnalayzerFactory;
import com.example.logviewe.utils.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class LogAnalayzer {
	Logger logger = LoggerFactory.getLogger(LogAnalayzer.class);
	
	/**
	 * プレイヤー名を取得する
	 * @return
	 * @throws NotInitializeException 
	 */
	public PlayerDto getPlayerNames() throws NotInitializeException{
		PlayerDto playerdto = new PlayerDto();
		
		int num = 0;
		//プレイヤー名を抜き出す
		InputData.getInst().reset();
		while( InputData.getInst().next()&& num < 2 ) {
			String line = InputData.getInst().readAndAhead();
			if( line.endsWith(LogConst.DRAW7OP)) {
				String name = line.replace(LogConst.DRAW7OP, "");
				//2行したにカードの種類があれば、自分
				String mark = InputData.getInst().readOffset(1);
				if( mark.startsWith(LogConst.CARDMAKR)) {
					playerdto.setMyName( name );
					num++;
				}else {
					playerdto.setOppName(name);
					num++;
				}
			}
		}
		//先攻後攻を検知する
		InputData.getInst().reset();
		while( InputData.getInst().next() ) {
			String line = InputData.getInst().readAndAhead();
			if( line.endsWith(LogConst.DFIRST) ) {
				//先行をとった人は・・・
				String name = line.replace(LogConst.DFIRST,"");
				playerdto.setFirst( (name.equals(playerdto.getMyName())) );
			}else if(line.endsWith(LogConst.DSECOND)) {
				//後攻を取った人は・・・
				String name = line.replace(LogConst.DSECOND,"");
				playerdto.setFirst( !(name.equals(playerdto.getMyName())) );
			}
		}
		
		return playerdto;
	}
	
	/**
	 * 
	 * @return
	 * @throws JsonProcessingException 
	 * @throws JsonMappingException 
	 * @throws NotInitializeException 
	 */
	public Hand getFirstHand(String myName) throws JsonMappingException, JsonProcessingException, NotInitializeException {
		Hand hand = new Hand();
		//初手の７枚を探す
		String firstHandMsg = myName + LogConst.DRAW7OP;
		String cards = "";
		InputData.getInst().reset();
		while( InputData.getInst().next() ) {
			String line = InputData.getInst().readAndAhead();
			if( line.equals(firstHandMsg)) {
				cards = InputData.getInst().readOffset(1);
				cards = cards.replace(LogConst.CARDMAKR, "");
				break;
			}
		}
		
		//カンマ区切りの七枚を取得する
		String[] cardarry = cards.split(",");
		for( String cardname : cardarry) {
			hand.addCard( PokeApiService.getCardDto(cardname) );
		}
		
		return hand;
	}
	
	/**
	 * 対戦準備処理（バトル場とベンチへのセット）
	 * @param myName
	 * @param oppName
	 * @return
	 * @throws NotInitializeException 
	 */
	public GameInfo getInitilaize(GameInfo gameInfo) throws NotInitializeException {
		String myName = gameInfo.getPlayers().getMyName();
		String oppName = gameInfo.getPlayers().getOppName();
		FieldDto field = new FieldDto();
		BattleAreaDto myArea = new BattleAreaDto();
		BattleAreaDto oppArea = new BattleAreaDto();
		String pName;
		//自分のバトル場を取得する
		String myStartStr = myName + LogConst.PLAYED;
		String oppStartStr = oppName + LogConst.PLAYED;
		
		boolean turn1Flag = false;
		InputData.getInst().reset();
		while( InputData.getInst().next()&& !turn1Flag ) {
			String line = InputData.getInst().readAndAhead();
			if( line.startsWith(myStartStr)&& 
					line.endsWith(LogConst.TOACTIVESP)) {
				//自分のバトル場
				pName = StrUtil.removeStr(line,new String[]{myStartStr,LogConst.TOACTIVESP});
				myArea.setBattlefield(PokeApiService.getCardDto(pName));
			}else if(line.startsWith(oppStartStr)&& 
					line.endsWith(LogConst.TOACTIVESP)) {
				//相手のバトル場
				pName = StrUtil.removeStr(line,new String[]{oppStartStr,LogConst.TOACTIVESP});
				oppArea.setBattlefield(PokeApiService.getCardDto(pName));
				
			}else if( line.startsWith(myStartStr)&& 
					line.endsWith(LogConst.TOBENCH)) {
				//自分のベンチ
				pName = StrUtil.removeStr(line,new String[]{myStartStr,LogConst.TOBENCH});
				myArea.adddBechField(PokeApiService.getCardDto(pName));
			}else if(line.startsWith(oppStartStr)&& 
					line.endsWith(LogConst.TOBENCH)) {
				//相手のベンチ
				pName = StrUtil.removeStr(line,new String[]{oppStartStr,LogConst.TOBENCH});
				oppArea.adddBechField(PokeApiService.getCardDto(pName));
			}else if(line.startsWith(LogConst.TURN1)) {
				turn1Flag = true;
			}
		}
		
		field.setMyArea(myArea);
		field.setOppArea(oppArea);
		gameInfo.setField(field);
		
		//バトル場にだしカードを手札から消す
		gameInfo.getHand().removeCard(myArea.getBattlefield());
		removeCard(gameInfo.getHand(),myArea.getBenchfield());
		
		return gameInfo;
	}
	
	/**
	 * 手札を削除する
	 * @param srcHand
	 * @param cardList
	 * @return
	 */
	public Hand removeCard(Hand srcHand,List<CardDto> cardList) {
		if( cardList == null ) {
			return srcHand;
		}
		for(CardDto card : cardList) {
			srcHand.removeCard(card);
		}
		return srcHand;
	}
	
	public TurnList getTurnList(GameInfo gameInfo) throws NotInitializeException {
		TurnList turnList = new TurnList();
		PlayerDto player = gameInfo.getPlayers();
		int turn = 1;
		int pidx = LogConst.FIRST;
		GameInfo wkGameInfo = gameInfo.clone();
		String[] turnPlayers = {
			(player.isFirst() ? player.getMyName():player.getOppName()),
			(player.isFirst() ? player.getOppName():player.getMyName()),
		};

		Turn trunObj = null;
		String turnPlayer = turnPlayers[0];
		//1ターン目まで読み飛ばし
		InputData.getInst().reset();
		while( InputData.getInst().next() ) {
			if( InputData.getInst().readAndAhead().startsWith(LogConst.TURN1)) {
				trunObj = new Turn();
				trunObj.setTurnNo(1);
				trunObj.setFirst(true);
				trunObj.setTurnPlayerName(turnPlayer);
				break;
			}
		}
		//対戦情報読み出し
		while(InputData.getInst().next()) {
			String line = InputData.getInst().readAndAhead();
			String turnStr = LogConst.TURN_PREFIX;
			Play play = null;
			if( line.startsWith(turnStr)) {
				//ターンの切り替え
				logger.info("ターン;"+line);
				if(trunObj != null) {
					turnList.addTurn(trunObj);
				}
				trunObj = new Turn();
				if( pidx == LogConst.SECOND) {
					turn++;
				}
				pidx = (pidx==LogConst.FIRST? LogConst.SECOND:LogConst.FIRST);
				turnPlayer = turnPlayers[pidx];
				trunObj.setTurnNo(turn);
				trunObj.setTurnPlayerName(turnPlayer);
				trunObj.setFirst((pidx==0?true:false));
			}else if(line.startsWith(turnPlayer+LogConst.PREFIX_DRAW)) {
				//ドロー
				logger.info("draw:"+line); 
				play = PlayAnalayzerFactory.getInst(PlayId.DRAW).getPlay( wkGameInfo,turnPlayer,line );
				trunObj.addPlay(play);
			}else if(line.startsWith(turnPlayer+LogConst.PREFIX_EVOLVED)) {
				//ドロー
				logger.info("evolved:"+line); 
				play = PlayAnalayzerFactory.getInst(PlayId.EVOLE).getPlay( wkGameInfo,turnPlayer,line );
				trunObj.addPlay(play);
			}else if(line.startsWith(turnPlayer+LogConst.PREFIX_PLAYED)) {
				//プレイ
				logger.info("played:"+line);
				//サブデータを取得する‘
				List<String> subData = getSubData();
				play = PlayAnalayzerFactory.getInst(PlayId.PLAY).getPlay(wkGameInfo,turnPlayer,line,subData);
				trunObj.addPlay(play);
			}else if(line.startsWith(turnPlayer+LogConst.PREFIX_ATTACHED)) {
				logger.info("attached:"+line);
				play = PlayAnalayzerFactory.getInst(PlayId.ATTACH).getPlay(wkGameInfo,turnPlayer,line);
				trunObj.addPlay(play);
			}else if(line.contains(LogConst.MID_USED) &&
						!line.endsWith(LogConst.SUFFIX_DAMAGE)) {
				logger.info("used:"+line);
				List<String> subData = getSubData();
				play = PlayAnalayzerFactory.getInst(PlayId.USE).getPlay(wkGameInfo,turnPlayer,line,subData);
				trunObj.addPlay(play);
			}else if(line.contains(LogConst.CONCEDED)) {
				//降参
				logger.info("conceded:");
				List<String> subData = getSubData();
				play = PlayAnalayzerFactory.getInst(PlayId.CONCEDE).getPlay(wkGameInfo,turnPlayer,line,subData);
				trunObj.addPlay(play);
			}else {
			}
			
		}
		
		if(trunObj != null) {
			turnList.addTurn(trunObj);
		}
		return turnList;
	}
	
	private List<String> getSubData() throws NotInitializeException{
		List<String> subData = new ArrayList<>();
		boolean isSub = true;
		while(isSub) {
			String line = InputData.getInst().read();
			if( line.startsWith(LogConst.PLAY_SUB_PREFIX) || 
					line.startsWith(LogConst.PLAY_CARD_PREFIX) ) {
				subData.add(line);
				InputData.getInst().ahead();
				logger.info("subdata:"+line);
			}else {
				isSub = false;
			}
		}
		return subData;
	}
	
	
}
