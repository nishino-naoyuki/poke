package com.example.logviewe.param;

import lombok.Data;

@Data
public class CardDto {
	private String name;
	private String imgPath;
	
	public CardDto clone() {
		CardDto carddto = new CardDto();
		carddto.setImgPath(new String(imgPath));
		carddto.setName(new String(name));
		return carddto;
	}
}
