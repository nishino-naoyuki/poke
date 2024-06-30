package com.example.logviewe.param;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class LostField {
	List<CardDto> lostCardList;
	
	public void addLostCard(CardDto card) {
		if( lostCardList == null ) {
			lostCardList = new ArrayList<>();
		}
		lostCardList.add(card);
	}
	
	public LostField clone() {
		LostField lf = new LostField();
		List<CardDto> lfList = new ArrayList<>();
		if( lostCardList == null ) {
			lf.setLostCardList(lfList);
			return lf;
		}
		
		for(CardDto card : lostCardList) {
			lf.addLostCard( card.clone() );
		}
		
		return lf;
	}
	
	public List<CardDto> getLostCardList(){
		if( lostCardList == null ) {
			lostCardList = new ArrayList<>();
		}
		return lostCardList;
	}
}
