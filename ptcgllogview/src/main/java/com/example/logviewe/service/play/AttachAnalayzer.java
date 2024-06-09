package com.example.logviewe.service.play;

import java.util.List;

import com.example.logviewe.param.CardDto;
import com.example.logviewe.param.LogConst;
import com.example.logviewe.param.Play;
import com.example.logviewe.param.PlayId;
import com.example.logviewe.param.PlayerDto;
import com.example.logviewe.param.play.Attache;
import com.example.logviewe.service.PokeApiService;

public class AttachAnalayzer implements PlayAnalayzer {

	@Override
	public Play getPlay(PlayerDto player, String turnPlayer, String line) {
		Play play = new Play();
		
		play.setPlayId(PlayId.ATTACH);
		Attache playDetail = new Attache();
		String trimedStr = line.replace(turnPlayer+LogConst.PREFIX_ATTACHED, "");
		
		playDetail.setPlayerName(turnPlayer);
		String msg = "";
		//何をつけたかを取得
		int toIndex = trimedStr.indexOf(LogConst.TO);
		String attachedCardName = trimedStr.substring(0, toIndex);
		CardDto attachedCard = PokeApiService.getCardDto(attachedCardName);
		playDetail.setAttacheCardDto(attachedCard);
		
		//誰につけたかを取得
		String toPokemon = "";
		CardDto toCard;
		if( trimedStr.endsWith(LogConst.SUFFIX_INACTIVE)) {
			//アクティブスポットへつけた
			trimedStr = trimedStr.replace(LogConst.SUFFIX_INACTIVE, "");
			playDetail.setActivePokemon(true);
		}else {
			//ベンチスポットへつけた
			trimedStr = trimedStr.replace(LogConst.SUFFIX_ONBENCH, "");
			playDetail.setActivePokemon(false);
		}
		toPokemon = trimedStr.substring(toIndex+4);
		toCard = PokeApiService.getCardDto(toPokemon);
		playDetail.setToCardDto(toCard);
		
		msg = attachedCardName + "を" + toPokemon + "につけた";
		
		play.setPlayDetail(playDetail);
		play.setMsg(msg);
		play.setImgUrl(attachedCard.getImgPath());
		
		return play;
	}

	@Override
	public Play getPlay(PlayerDto player, String turnPlayer, String line, List<String> subData) {
		return getPlay(player,turnPlayer,line);
	}

}