package com.example.logviewe.service.play;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.logviewe.param.BattleAreaDto;
import com.example.logviewe.param.FieldDto;
import com.example.logviewe.param.GameInfo;
import com.example.logviewe.param.LogConst;
import com.example.logviewe.param.Play;
import com.example.logviewe.param.PlayId;
import com.example.logviewe.param.PlayerDto;
import com.example.logviewe.param.play.Bench;
import com.example.logviewe.param.play.Draw;
import com.example.logviewe.param.play.PlayDetail;
import com.example.logviewe.param.play.UseCard;
import com.example.logviewe.service.PokeApiService;
import com.fasterxml.jackson.core.JsonProcessingException;

public class PlayDetailAnalayzer extends PlayAnalazerBase implements PlayAnalayzer {
	Logger logger = LoggerFactory.getLogger(PlayDetailAnalayzer.class);
	
	@Override
	public Play getPlay(GameInfo gameInfo, String turnPlayer, String line) {
		// TODO Auto-generated method stub
		return getPlay(gameInfo,turnPlayer,line,null);
	}

	@Override
	public Play getPlay(GameInfo gameInfo, String turnPlayer, String line, List<String> subData) {
		PlayDetail playDetail = null;
		Play play = new Play();
		String msg = "";
		String imgUrl = "";
		BattleAreaDto field = (
				gameInfo.getPlayers().getMyName().equals(turnPlayer)?
						gameInfo.getField().getMyArea() : gameInfo.getField().getOppArea());
		//ベンチに出す
		if( line.endsWith(LogConst.TOBENCH)) {
			//ベンチにポケモンを出す
			//現状をコピーする
			copyNowStuation(play,PlayId.BENCH,gameInfo);
			String value = line.replace(turnPlayer+LogConst.PREFIX_PLAYED, "").replace(LogConst.TOBENCH, "");
			Bench bench = getToBench(value,field);
			msg = PlayId.BENCH.getMsg() + ":" + value;
			play.setPlayDetail(bench);
			play.setPlayId(PlayId.BENCH);
		}else if(line.endsWith(LogConst.TOSTUDIUM)) {
			//スタジアムを出す
			copyNowStuation(play,PlayId.STUDIUM,gameInfo);
			String value = line.replace(turnPlayer+LogConst.PREFIX_PLAYED, "").replace(LogConst.TOSTUDIUM, "");
			msg = PlayId.STUDIUM.getMsg() + ":" + value;
		}else {
			//何かを使った
			copyNowStuation(play,PlayId.PLAY,gameInfo);
			play = playAnything(gameInfo,turnPlayer,line,subData,field);
		}
		//play.setMsg(msg);
		//play.setImgUrl(imgUrl);
		return play;
	}
	
	/**
	 * 
	 * @param gameInfo
	 * @param turnPlayer
	 * @param line
	 * @param subData
	 * @param field
	 * @return
	 */
	private Play playAnything(
			GameInfo gameInfo,
			String turnPlayer, 
			String line, 
			List<String> subData,
			BattleAreaDto field) {
		Play play = new Play();
		UseCard useCard = new UseCard();
		String imgUrl;
		String msg = "";
		String value = line.replace(turnPlayer+LogConst.PREFIX_PLAYED, "");
		try {
			imgUrl = PokeApiService.findSmallImage(value);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			imgUrl = "";
		}
		msg = PlayId.PLAY.getMsg() + ":" + value;
		
		useCard.setImgUrl(imgUrl);
		useCard.setMsg(msg);
		
		//サブデータがある場合
		if( subData.size() > 0 ) {
			useCard.setSubPlayList(
					getSubPlayList(gameInfo,turnPlayer,subData,field)
					);
		}
		
		play.setPlayDetail(useCard);
		
		return play;
	}
	/**
	 * 
	 * @param gameInfo
	 * @param turnPlayer
	 * @param subData
	 * @param field
	 * @return
	 */
	private List<PlayDetail> getSubPlayList(
			GameInfo gameInfo,
			String turnPlayer,
			List<String> subData,
			BattleAreaDto field){
		int idx = 0;
		List<PlayDetail> playDetalList = new ArrayList<>();
		while(idx < subData.size()) {
			String subLine = subData.get(idx);
			if(subLine.startsWith(LogConst.PLAY_SUB_PREFIX)){
				if( subLine.startsWith(LogConst.PLAY_SUB_PREFIX+turnPlayer)) {
					//ターンプレイヤー
					subLine = subLine.replace(
							LogConst.PLAY_SUB_PREFIX+turnPlayer, "");
					playDetalList = getTurnPlayerSubData(
							gameInfo,turnPlayer,subLine,subData,idx,field
							);
					
				}
			}
			idx++;
		}
		
		return playDetalList;
	}
	/**
	 * 
	 * @param gameInfo
	 * @param turnPlayer
	 * @param subLine
	 * @param subData
	 * @param idx
	 * @param field
	 * @return
	 */
	private List<PlayDetail> getTurnPlayerSubData(
			GameInfo gameInfo,
			String turnPlayer,
			String subLine,
			List<String> subData,
			int idx,
			BattleAreaDto field
			){
		List<PlayDetail> playDetalList = new ArrayList<>();
		if( gameInfo.getPlayers().getMyName().equals(turnPlayer) ) {
			field = gameInfo.getField().getMyArea();
		}else {
			field = gameInfo.getField().getOppArea();
		}
		
		if(subLine.endsWith(LogConst.SUFFIX_TOBENCH)) {
			playDetalList = getToBechSub(gameInfo,turnPlayer,subLine,field);
			gameInfo.getField().setMyArea(field);
			
		}else if(subLine.endsWith(LogConst.SUFFIX_THEMTOBENCH)) {
			idx++;
			String value = subData.get(idx);
			playDetalList = getToBechList(gameInfo,turnPlayer,value,field);
			gameInfo.getField().setMyArea(field);
			//ターンプレイヤーがカードを引いた
		}else if( subLine.startsWith(LogConst.PREFIX_DRAW) ) {
			//引いたカードを取得
			idx++;
			String value = subData.get(idx);
			playDetalList = getTurnPlayerDrawCard(value);
		}else if( subLine.endsWith(LogConst.SUFFIX_SWITCH) ){
			//入れ替え
			logger.info("ポケモン入れ替えを使った");
			playedSwitch(gameInfo,turnPlayer,subLine,field);
		}else if( subLine.endsWith(LogConst.SUFFIX_SHUFFUL) ){
			
		}
		
		return playDetalList;
	}
	/**
	 * 
	 * @param gameInfo
	 * @param turnPlayer
	 * @param subLine
	 * @param field
	 * @return
	 */
	private List<PlayDetail> playedSwitch(
			GameInfo gameInfo,
			String turnPlayer,
			String subLine,
			BattleAreaDto field){
		List<PlayDetail> playDetalList = new ArrayList<>();
		
		subLine = subLine.replaceFirst(LogConst.PREFIX_SWITCH,"");
		subLine = subLine.replace(LogConst.SUFFIX_SWITCH,"");
		
		int midIdx = subLine.indexOf(LogConst.MID_SWITCH);
		subLine = subLine.replace(turnPlayer+LogConst.PREFIX_SWITCH, "");
		
		
		String toActivePoke = subLine.substring(0,midIdx);
		String toBenchPoke = subLine.substring(
				midIdx+LogConst.MID_SWITCH.length(),
				subLine.length());
		
		logger.info(toBenchPoke+"が下がって"+toActivePoke+"がバトル場へ");
		
		switchPoke(field,toActivePoke,toBenchPoke);		
		
		return playDetalList;
	}

	private Bench getToBench(
			String card,
			BattleAreaDto field
			) {
		Bench benchObj = new Bench();
		benchObj.setDrawCard(PokeApiService.getCardDto(card));
		benchObj.setMsg(card + "をベンチを出した");
		benchObj.setImgUrl(benchObj.getDrawCard().getImgPath());
		field.adddBechField(benchObj.getDrawCard());
		
		return benchObj;
	}
	/**
	 * 
	 * @param gameInfo
	 * @param turnPlayer
	 * @param bench
	 * @return
	 */
	private List<PlayDetail> getToBechSub(
			GameInfo gameInfo,
			String turnPlayer,
			String bench,
			BattleAreaDto field){
		List<PlayDetail> playDetalList = new ArrayList<>();
		
		String card = bench.replace(LogConst.PREFIX_DRAW, "").replace(LogConst.SUFFIX_TOBENCH, "");
		Bench benchObj = getToBench(card,field);
		playDetalList.add(benchObj);
				
		return playDetalList;
	}
	/**
	 * 
	 * @param gameInfo
	 * @param turnPlayer
	 * @param bench
	 * @return
	 */
	private List<PlayDetail> getToBechList(
			GameInfo gameInfo,String turnPlayer,String bench,BattleAreaDto field){

		List<PlayDetail> playDetalList = new ArrayList<>();
		
		String[] cards = bench.replace(LogConst.PLAY_CARD_PREFIX, "").split(",");
		for(String card : cards) {
			Bench benchObj = new Bench();
			benchObj.setDrawCard(PokeApiService.getCardDto(card));
			benchObj.setMsg(card + "をベンチを出した");
			benchObj.setImgUrl(benchObj.getDrawCard().getImgPath());
			playDetalList.add(benchObj);
		}

		//フィールドを更新;
		for(PlayDetail pd :  playDetalList) {
			field.adddBechField(((Bench)pd).getDrawCard());
		}
		
		return playDetalList;
	}
	private List<PlayDetail> getTurnPlayerDrawCard(String drawCardsStr){
		List<PlayDetail> playDetalList = new ArrayList<>();
		
		String[] cards = drawCardsStr.replace(LogConst.PLAY_CARD_PREFIX, "").split(",");
		for(String card : cards) {
			Draw draw = new Draw();
			draw.setDrawCard(PokeApiService.getCardDto(card));
			draw.setMsg(card + "を引いた");
			draw.setImgUrl(draw.getDrawCard().getImgPath());
			playDetalList.add(draw);
		}
		
		return playDetalList;
	}

}
