package com.example.logviewe.Exception;

import com.fasterxml.jackson.core.JsonProcessingException;

public class PokemonNotFound extends JsonProcessingException {
	public PokemonNotFound(String msg) {
		super(msg);
	}
}
