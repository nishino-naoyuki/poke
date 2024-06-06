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
import com.example.logviewe.param.PlayerDto;
import com.example.logviewe.param.TurnList;
import com.example.logviewe.param.input.InputData;
import com.example.logviewe.param.play.Draw;
import com.example.logviewe.param.play.PlayDetail;
import com.example.logviewe.utils.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class LogAnalayzer {
	PokeApiService pokeApiService;
	Logger logger = LoggerFactory.getLogger(LogAnalayzer.class);
	
	
	private final String DRAW7OP = " drew 7 cards for the opening hand.";
	private final String CARDMAKR = "   • ";
	private final String DFIRST = " decided to go first.";
	private final String DSECOND = " decided to go second.";
	private final String TOACTIVESP = " to the Active Spot.";
	private final String TOBENCH = " to the Bench.";
	private final String TOSTUDIUM = " to the Stadium spot.";
	private final String PLAYED = " played ";
	private final String TURN1 = "Turn # 1";
	private final String TURN_PREFIX = "Turn # ";
	private final String TURN_AFTER = " - ";
	private final String DREW_PREFIX = " drew ";
	private final String PLAY_PREFIX = " played ";
	private final String PLAY_SUB_PREFIX = "- ";
	private final String PLAY_CARD_PREFIX = "   • ";
	
	@Autowired
	public LogAnalayzer(PokeApiService pokeApiService) {
		this.pokeApiService = pokeApiService;
	}
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
			if( line.endsWith(DRAW7OP)) {
				String name = line.replace(DRAW7OP, "");
				//2行したにカードの種類があれば、自分
				String mark = InputData.getInst().readOffset(1);
				if( mark.startsWith(CARDMAKR)) {
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
			if( line.endsWith(DFIRST) ) {
				//先行をとった人は・・・
				String name = line.replace(DFIRST,"");
				playerdto.setFirst( (name.equals(playerdto.getMyName())) );
			}else if(line.endsWith(DSECOND)) {
				//後攻を取った人は・・・
				String name = line.replace(DSECOND,"");
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
		String firstHandMsg = myName + DRAW7OP;
		String cards = "";
		InputData.getInst().reset();
		while( InputData.getInst().next() ) {
			String line = InputData.getInst().readAndAhead();
			if( line.equals(firstHandMsg)) {
				cards = InputData.getInst().readOffset(1);
				cards = cards.replace(CARDMAKR, "");
				break;
			}
		}
		
		//カンマ区切りの七枚を取得する
		String[] cardarry = cards.split(",");
		for( String cardname : cardarry) {
			hand.addCard( getCardDto(cardname) );
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
		String myStartStr = myName + PLAYED;
		String oppStartStr = oppName + PLAYED;
		
		boolean turn1Flag = false;
		InputData.getInst().reset();
		while( InputData.getInst().next()&& !turn1Flag ) {
			String line = InputData.getInst().readAndAhead();
			if( line.startsWith(myStartStr)&& 
					line.endsWith(TOACTIVESP)) {
				//自分のバトル場
				pName = StrUtil.removeStr(line,new String[]{myStartStr,TOACTIVESP});
				myArea.setBattlefield(getCardDto(pName));
			}else if(line.startsWith(oppStartStr)&& 
					line.endsWith(TOACTIVESP)) {
				//相手のバトル場
				pName = StrUtil.removeStr(line,new String[]{oppStartStr,TOACTIVESP});
				oppArea.setBattlefield(getCardDto(pName));
				
			}else if( line.startsWith(myStartStr)&& 
					line.endsWith(TOBENCH)) {
				//自分のベンチ
				pName = StrUtil.removeStr(line,new String[]{myStartStr,TOBENCH});
				myArea.adddBechField(getCardDto(pName));
			}else if(line.startsWith(oppStartStr)&& 
					line.endsWith(TOBENCH)) {
				//相手のベンチ
				pName = StrUtil.removeStr(line,new String[]{oppStartStr,TOBENCH});
				oppArea.adddBechField(getCardDto(pName));
			}else if(line.startsWith(TURN1)) {
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
	
	public TurnList getTurnList(PlayerDto player) throws NotInitializeException {
		TurnList turnList = new TurnList();
		int turn = 1;
		int pidx = 0;
		String[] turnPlayers = {
			(player.isFirst() ? player.getMyName():player.getOppName()),
			(player.isFirst() ? player.getOppName():player.getMyName()),
		};
		
		//1ターン目まで読み飛ばし
		int turn1LineNum = 0;
		InputData.getInst().reset();
		while( InputData.getInst().next() ) {
			if( InputData.getInst().readAndAhead().startsWith(TURN1)) {
				break;
			}
		}
		//対戦情報読み出し
		while(InputData.getInst().next()) {
			String line = InputData.getInst().readAndAhead();
			String turnPlayer = turnPlayers[pidx];
			String turnStr = TURN_PREFIX + turn + TURN_AFTER;
			PlayDetail playDetail = null;
			if( line.startsWith(turnStr)) {
				logger.info("ターン;"+line);
				if( pidx == 0) {
					turn++;
				}
				pidx = (pidx==0?1:0);
			}else if(line.startsWith(turnPlayer+DREW_PREFIX)) {
				//ドロー
				logger.info("draw:"+line);
				playDetail = drawProcess( player,turnPlayer,line );
			}else if(line.startsWith(turnPlayer+PLAY_PREFIX)) {
				//プレイ
				logger.info("played:"+line);
				//サブデータを取得する‘
				List<String> subData = getSubData();
				playDetail = getPlayDetail(player,turnPlayer,line,subData);
			}else {
			}
		}
		return turnList;
	}
	
	private List<String> getSubData() throws NotInitializeException{
		List<String> subData = new ArrayList<>();
		boolean isSub = true;
		while(isSub) {
			String line = InputData.getInst().readAndAhead();
			if( line.startsWith(PLAY_SUB_PREFIX) || 
					line.startsWith(PLAY_CARD_PREFIX) ) {
				subData.add(line);
				logger.info("subdata:"+line);
			}else {
				isSub = false;
			}
		}
		return subData;
	}
	
	private PlayDetail getPlayDetail(PlayerDto player,
									String turnPlayer,
									String line,
									List<String> subData) {
		PlayDetail playDetail = null;
		
		//ベンチに出す
		if( line.endsWith(TOBENCH)) {
			//ベンチにポケモンを出す
			String value = line.replace(turnPlayer+PLAY_PREFIX, "").replace(TOBENCH, "");
		}else if(line.endsWith(TOSTUDIUM)) {
			//スタジアムを出す
			String value = line.replace(turnPlayer+PLAY_PREFIX, "").replace(TOSTUDIUM, "");
		}else {
			//何かを使った
		}
		
		return playDetail;
	}
	private PlayDetail drawProcess(PlayerDto player,String turnPlayer,String line) {
		Draw playDetail = new Draw();
		
		playDetail.setPlayerName(turnPlayer);
		//何を引いたかを確認
		if( turnPlayer.equals(player.getMyName())) {
			//自分の場合
			String value = line.replace(turnPlayer+DREW_PREFIX, "").replace(".", "");
			playDetail.setDrawCard( getCardDto(value) );
		}else {
			playDetail.setDrawCard(null);
		}
		
		return playDetail;
	}
	
	private CardDto getCardDto(String cardname) {
		CardDto card = new CardDto();
		cardname = cardname.trim();
		String imgPath;
		
		try {
			imgPath = pokeApiService.findSmallImage(cardname);
			card.setName(cardname);
			card.setImgPath(imgPath);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			card.setName("");
		}

		return card;
	}
}
