package com.example.logviewe.json;

import lombok.Data;

@Data
public class Set {
	private String id;
	private String name;
	private String series;
	private int printedTotal;
	private int total;
	private Legality legalities;
	private String ptcgoCode;
	private String releaseDate;
	private String updatedAt;
	private Image images;
}
