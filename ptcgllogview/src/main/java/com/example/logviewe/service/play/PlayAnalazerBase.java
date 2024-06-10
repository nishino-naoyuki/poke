package com.example.logviewe.service.play;

import com.example.logviewe.param.GameInfo;
import com.example.logviewe.param.Play;
import com.example.logviewe.param.PlayId;

public abstract class PlayAnalazerBase {

	public void copyNowStuation(Play play,PlayId playId,GameInfo gameInfo) {

		play.setPlayId(playId);
		//現状をコピーする
		play.setHand( gameInfo.getHand().clone() );
		play.setMyArea( gameInfo.getField().getMyArea().clone() );
		play.setOppArea( gameInfo.getField().getOppArea().clone() );
	}
}
