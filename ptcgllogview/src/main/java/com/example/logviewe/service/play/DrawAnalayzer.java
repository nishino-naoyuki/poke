package com.example.logviewe.service.play;

import java.util.List;

import com.example.logviewe.param.GameInfo;
import com.example.logviewe.param.LogConst;
import com.example.logviewe.param.Play;
import com.example.logviewe.param.PlayId;
import com.example.logviewe.param.PlayerDto;
import com.example.logviewe.param.play.Draw;
import com.example.logviewe.service.PokeApiService;

public class DrawAnalayzer extends PlayAnalazerBase implements PlayAnalayzer {

	@Override
	public Play getPlay(GameInfo gameInfo, String turnPlayer, String line) {
		Play play = new Play();
		
		//現状をコピーする
		copyNowStuation(play,PlayId.DRAW,gameInfo);
		
		Draw playDetail = new Draw();
		
		playDetail.setPlayerName(turnPlayer);
		String msg = "";
		String imgUrl = "";
		//何を引いたかを確認
		if( turnPlayer.equals(gameInfo.getPlayers().getMyName())) {
			//自分の場合
			String value = line.replace(turnPlayer+LogConst.PREFIX_DRAW, "").replace(".", "");
			playDetail.setDrawCard( PokeApiService.getCardDto(value) );
			msg = value+"を引いた";
			imgUrl = playDetail.getDrawCard().getImgPath();
			
		}else {
			playDetail.setDrawCard(null);
			msg = PlayId.DRAW.getMsg();
		}
		playDetail.setMsg(msg);
		playDetail.setImgUrl(imgUrl);
		play.setPlayDetail(playDetail);
		
		return play;
	}

	@Override
	public Play getPlay(GameInfo gameInfo, String turnPlayer, String line, List<String> subData) {
		return getPlay(gameInfo,turnPlayer,line);
	}

}
