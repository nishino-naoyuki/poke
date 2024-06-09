package com.example.logviewe.param.play;

import com.example.logviewe.param.CardDto;

import lombok.Data;

@Data
public class Attache extends PlayDetail{
	CardDto attacheCardDto;
	CardDto toCardDto;
	boolean isActivePokemon;
}
