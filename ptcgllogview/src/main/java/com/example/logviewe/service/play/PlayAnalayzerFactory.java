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
		}else if( playId.equals(PlayId.USE) ) {
			return new UseAnalayzer();
		}else if( playId.equals(PlayId.EVOLE) ) {
			return new EvolvedAnalayzer();
		}
		
		return null;
	}
}
