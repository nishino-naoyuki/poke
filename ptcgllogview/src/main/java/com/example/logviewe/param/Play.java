package com.example.logviewe.param;

import com.example.logviewe.param.play.PlayDetail;

import lombok.Data;

@Data
public class Play {
	private PlayId playId;
	private PlayDetail playDetail;
	private String msg;
	private String imgUrl;	//カードがない場合は空文字
}
