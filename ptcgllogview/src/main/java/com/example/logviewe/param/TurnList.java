package com.example.logviewe.param;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TurnList {
	List<Turn> turnList;
	
	public void addTurn(Turn turn) {
		if( turnList == null ) {
			turnList = new ArrayList<>();
		}
		turnList.add(turn);
	}
	
}
