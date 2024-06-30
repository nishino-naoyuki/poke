package com.example.logviewe.param;


import lombok.Data;

@Data
public class FieldDto {
	private BattleAreaDto myArea;
	private BattleAreaDto oppArea;
	private StudiumCard studium;
	private LostField lostField;
	
	
	public FieldDto clone() {
		FieldDto fd = new FieldDto();
		
		fd.setMyArea(myArea.clone());
		fd.setOppArea(oppArea.clone());
		fd.setStudium(studium.clone());
		fd.setLostField(lostField.clone());
		
		return fd;
	}
}
