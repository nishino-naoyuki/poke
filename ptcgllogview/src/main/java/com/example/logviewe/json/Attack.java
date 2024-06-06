package com.example.logviewe.json;

import lombok.Data;

@Data
public class Attack {
	private String name;
	private String[] cost;
	private int convertedEnergyCost;
	private String damage;
	private String text;
}
