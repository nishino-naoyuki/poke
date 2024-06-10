package com.example.logviewe.param;

import lombok.Data;

@Data
public class GameInfo {
	private PlayerDto players;
	private Hand hand;
	private FieldDto field;
	
	public GameInfo clone() {
		GameInfo gameInfo = new GameInfo();
		
		gameInfo.setPlayers(players);
		gameInfo.setHand(hand.clone());
		gameInfo.setField(field.clone());
		
		return gameInfo;
	}
}
