package com.example.logviewe.param;

import lombok.Data;

@Data
public class GameInfo {
	private PlayerDto players;
	private Hand hand;
	private FieldDto field;
}