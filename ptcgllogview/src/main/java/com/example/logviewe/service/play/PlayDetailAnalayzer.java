package com.example.logviewe.service.play;

import java.util.List;

import com.example.logviewe.param.GameInfo;
import com.example.logviewe.param.LogConst;
import com.example.logviewe.param.Play;
import com.example.logviewe.param.PlayId;
import com.example.logviewe.param.PlayerDto;
import com.example.logviewe.param.play.PlayDetail;
import com.example.logviewe.service.PokeApiService;
import com.fasterxml.jackson.core.JsonProcessingException;

public class PlayDetailAnalayzer extends PlayAnalazerBase implements PlayAnalayzer {

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
		//ベンチに出す
		if( line.endsWith(LogConst.TOBENCH)) {
			//ベンチにポケモンを出す
			//現状をコピーする
			copyNowStuation(play,PlayId.BENCH,gameInfo);
			String value = line.replace(turnPlayer+LogConst.PREFIX_PLAYED, "").replace(LogConst.TOBENCH, "");
			msg = PlayId.BENCH.getMsg() + ":" + value;
		}else if(line.endsWith(LogConst.TOSTUDIUM)) {
			//スタジアムを出す
			copyNowStuation(play,PlayId.STUDIUM,gameInfo);
			String value = line.replace(turnPlayer+LogConst.PREFIX_PLAYED, "").replace(LogConst.TOSTUDIUM, "");
			msg = PlayId.STUDIUM.getMsg() + ":" + value;
		}else {
			//何かを使った
			copyNowStuation(play,PlayId.PLAY,gameInfo);
			String value = line.replace(turnPlayer+LogConst.PREFIX_PLAYED, "");
			try {
				imgUrl = PokeApiService.findSmallImage(value);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				imgUrl = "";
			}
			msg = PlayId.PLAY.getMsg() + ":" + value;
		}
		play.setMsg(msg);
		play.setImgUrl(imgUrl);
		return play;
	}

}
