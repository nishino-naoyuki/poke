package com.example.logviewe.param;


import lombok.Data;

@Data
public class FieldDto {
	BattleAreaDto myArea;
	BattleAreaDto oppArea;
	
	public FieldDto clone() {
		FieldDto fd = new FieldDto();
		
		fd.setMyArea(myArea.clone());
		fd.setOppArea(oppArea.clone());
		
		return fd;
	}
}
