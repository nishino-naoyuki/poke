package com.example.logviewe.param.play;

import com.example.logviewe.param.CardDto;

import lombok.Data;

@Data
public class Evolved extends PlayDetail {
	private CardDto prePoke;
	private CardDto evoPoke;
}
