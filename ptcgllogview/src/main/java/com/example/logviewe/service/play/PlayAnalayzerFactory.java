package com.example.logviewe.service.play;

import com.example.logviewe.param.PlayId;

public class PlayAnalayzerFactory {

	public static PlayAnalayzer getInst(PlayId playId) {
		if( playId.equals(PlayId.DRAW)) {
			return new DrawAnalayzer();
		}else if( playId.equals(PlayId.PLAY) ) {
			return new PlayDetailAnalayzer();
		}else if( playId.equals(PlayId.ATTACH) ) {
			return new AttachAnalayzer();
		}
		
		return null;
	}
}