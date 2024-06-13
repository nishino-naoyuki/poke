package com.example.logviewe.param.play;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class UseCard extends PlayDetail {
	private List<PlayDetail> subPlayList;
	
	public void addSubPlay(PlayDetail playDetail) {
		if( subPlayList != null ) {
			subPlayList = new ArrayList<>();
		}
		subPlayList.add(playDetail);
	}
}
