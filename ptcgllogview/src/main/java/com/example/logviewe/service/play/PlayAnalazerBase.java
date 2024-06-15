package com.example.logviewe.service.play;

import java.util.List;

import com.example.logviewe.param.BattleAreaDto;
import com.example.logviewe.param.CardDto;
import com.example.logviewe.param.GameInfo;
import com.example.logviewe.param.Play;
import com.example.logviewe.param.PlayId;
import com.example.logviewe.service.PokeApiService;

public abstract class PlayAnalazerBase {

	public void copyNowStuation(Play play,PlayId playId,GameInfo gameInfo) {

		play.setPlayId(playId);
		//現状をコピーする
		play.setHand( gameInfo.getHand().clone() );
		play.setMyArea( gameInfo.getField().getMyArea().clone() );
		play.setOppArea( gameInfo.getField().getOppArea().clone() );
	}
	
	protected void switchPoke(BattleAreaDto field,String activePoke,String benchPoke) {
		
		List<CardDto> benchList = field.getBenchfield();
		CardDto nowActive = field.getBattlefield();
		//ベンチからバトル場へ行くポケモンを削除して今のバトル場のポケモンをベンチへ追加しばとるばへ
		for(int i=0; i<benchList.size(); i++) {
			CardDto benchCard = benchList.get(i);
			if( benchCard.getName().equals(activePoke)) {
				field.setBattlefield(benchCard);
				benchList.remove(i);
				field.adddBechField(nowActive);
				break;
			}
		}
	}
}
