package com.example.logviewe.param;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class BattleAreaDto {
	private CardDto battlefield;
	private List<CardDto> benchfield;
	
	public void adddBechField(CardDto card) {
		if( benchfield == null ) {
			benchfield = new ArrayList<CardDto>();
		}
		benchfield.add(card);
	}
	
	public BattleAreaDto clone() {
		BattleAreaDto bad = new BattleAreaDto();
		
		bad.setBattlefield(battlefield.clone());
		if( benchfield != null ) {
			for(CardDto card:benchfield) {
				bad.adddBechField(card.clone());
			}
		}
		return bad;
	}
}
