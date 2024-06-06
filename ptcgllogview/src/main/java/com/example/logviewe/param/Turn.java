package com.example.logviewe.param;

import java.util.List;

import lombok.Data;

@Data
public class Turn {
	private int turnNo;
	private String turnPlayerName;
	private boolean isFirst;
	List<Play> plays;
}
