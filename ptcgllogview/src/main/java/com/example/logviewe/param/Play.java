package com.example.logviewe.param;

import com.example.logviewe.param.play.PlayDetail;

import lombok.Data;

@Data
public class Play {
	private PlayId playId;
	private PlayDetail playDetail;
	private String msg;
	private String imgUrl;	//カードがない場合は空文字
	
	//このプレイをする前の状態
	private BattleAreaDto myArea;
	private BattleAreaDto oppArea;
	private Hand hand;
}
