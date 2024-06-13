package com.example.logviewe.param.play;

import lombok.Data;

@Data
public abstract class PlayDetail {
	private String playerName;
	private String msg;
	private String imgUrl;	//カードがない場合は空文字
}
