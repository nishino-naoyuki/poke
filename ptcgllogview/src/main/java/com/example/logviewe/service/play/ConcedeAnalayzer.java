package com.example.logviewe.service.play;

import java.util.List;

import com.example.logviewe.param.GameInfo;
import com.example.logviewe.param.LogConst;
import com.example.logviewe.param.Play;
import com.example.logviewe.param.PlayId;
import com.example.logviewe.param.play.Conceded;

public class ConcedeAnalayzer extends PlayAnalazerBase implements PlayAnalayzer {

	@Override
	public Play getPlay(GameInfo gameInfo, String turnPlayer, String line) {
		Play play = new Play();
		
		//現状をコピーする
		copyNowStuation(play,PlayId.CONCEDE,gameInfo);
		
		Conceded conceded = new Conceded();
		
		String concededPlayer = line.substring(0, line.indexOf(LogConst.CONCEDED));
		if( concededPlayer.equals(LogConst.OPPONENT)) {
			conceded.setConcededPlayer(gameInfo.getOppPlayer().getName());
			conceded.setWinPlayer(gameInfo.getMyPlayer().getName());
		}else {
			conceded.setConcededPlayer(gameInfo.getMyPlayer().getName());
			conceded.setWinPlayer(gameInfo.getOppPlayer().getName());
		}
		
		String msg = conceded.getConcededPlayer() + "が降参しました。";
		conceded.setMsg(msg);
		conceded.setImgUrl(NOIMAGE);
		play.setPlayDetail(conceded);
		
		return play;
	}

	@Override
	public Play getPlay(GameInfo gameInfo, String turnPlayer, String line, List<String> subData) {
		// TODO Auto-generated method stub
		return getPlay(gameInfo,turnPlayer,line);
	}

}
