package com.example.logviewe.service.play;

import java.util.ArrayList;
import java.util.List;

import com.example.logviewe.param.BattleAreaDto;
import com.example.logviewe.param.GameInfo;
import com.example.logviewe.param.Play;
import com.example.logviewe.param.PlayId;
import com.example.logviewe.param.play.Evolved;

public class EvolvedAnalayzer extends PlayAnalazerBase implements PlayAnalayzer {

	@Override
	public Play getPlay(GameInfo gameInfo, String turnPlayer, String line) {
		// TODO Auto-generated method stub
		return getPlay(gameInfo,turnPlayer,line,null);
	}

	@Override
	public Play getPlay(GameInfo gameInfo, String turnPlayer, String line, List<String> subData) {
		Play play = new Play();
		copyNowStuation(play,PlayId.EVOLE,gameInfo);

		BattleAreaDto field;
		if( gameInfo.getMyPlayer().getName().equals(turnPlayer) ) {
			field = gameInfo.getField().getMyArea();
		}else {
			field = gameInfo.getField().getOppArea();
		}
		
		Evolved evo = getEvolved(field,turnPlayer,line);
		
		play.setPlayDetail(evo);
		
		return play;
	}
	

}
