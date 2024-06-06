package com.example.logviewe.param;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Hand {
	private List<CardDto> cards;
	
	public void addCard(CardDto card) {
		if( cards == null ) {
			cards = new ArrayList<CardDto>();
		}
		cards.add(card);
	}
	
	public void removeCard(CardDto card) {
		cards.remove(card);
	}
}
