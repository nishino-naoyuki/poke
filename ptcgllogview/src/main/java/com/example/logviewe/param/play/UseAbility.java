package com.example.logviewe.param.play;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class UseAbility extends PlayDetail {
	private String abilityName;
	private List<PlayDetail> effectList;
	
	public void addEffect(PlayDetail effect) {
		if( effectList == null ) {
			effectList = new ArrayList<>();
		}
		effectList.add(effect);
	}
}
