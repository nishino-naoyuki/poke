package com.example.logviewe.param;

import lombok.Data;

@Data
public class GameInfo {
	private PlayerDto myPlayer;
	private PlayerDto oppPlayer;
	//private Hand hand;
	private FieldDto field;
	
	public GameInfo clone() {
		GameInfo gameInfo = new GameInfo();
		
		gameInfo.setMyPlayer(myPlayer);
		gameInfo.setOppPlayer(oppPlayer);
		//gameInfo.setHand(hand.clone());
		gameInfo.setField(field.clone());
		
		return gameInfo;
	}
}
