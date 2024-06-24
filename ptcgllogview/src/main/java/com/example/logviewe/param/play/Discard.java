package com.example.logviewe.param.play;

import java.util.ArrayList;
import java.util.List;

import com.example.logviewe.param.CardDto;

import lombok.Data;

@Data
public class Discard extends PlayDetail {
	private List<CardDto> discardList;
	
	public void addDiscardList(CardDto cardDto) {
		if( discardList == null ) {
			discardList = new ArrayList<>();
		}
		discardList.add(cardDto);
	}
}
