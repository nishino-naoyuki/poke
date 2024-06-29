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
	
	public List<Turn> getFirstList(){
		List<Turn> fList = new ArrayList<>();
		if( turnList == null ) {
			return fList;
		}
		for(int i=0; i<turnList.size();i+=2) {
			fList.add(turnList.get(i));
		}
		return fList;
	}
	
	public List<Turn> getSecondList(){
		List<Turn> sList = new ArrayList<>();
		if( turnList == null ) {
			return sList;
		}
		for(int i=1; i<turnList.size();i+=2) {
			sList.add(turnList.get(i));
		}
		return sList;
	}
	
}
