package com.example.logviewe.param;

import lombok.Data;

@Data
public class StudiumCard {
	private CardDto studium;

	public StudiumCard clone() {
		StudiumCard sc = new StudiumCard();
		if( studium == null ) {
			studium = new CardDto();
		}
		
		sc.setStudium( studium.clone() );
		
		return sc;
	}
}
