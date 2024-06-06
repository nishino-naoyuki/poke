package com.example.logviewe.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PokecaInfo {
	private String id;
	private String name;
	private String supertype;
	private String[] subtypes;
	private String hp;
	private String[] types;
	private String[] rules;
	private String evolvesFrom;
	private Ability[] abilities;
	private Attack[] attacks;
	private Weakness[] Weaknesses;
	private Resistance[] resistances;
	private String[] retreatCost;
	private int convertedRetreatCost;
	private Set set;
	private String number;
	private String artist;
	private String rarity;
	private String flavorText;
	private int[] nationalPokedexNumbers;
	private Legality legalities;
	private String regulationMark;
	private Image2 images;
}
