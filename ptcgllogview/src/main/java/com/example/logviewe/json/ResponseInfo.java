package com.example.logviewe.json;

import lombok.Data;

@Data
public class ResponseInfo {
	private PokecaInfo[] data;
	private int page;
	private int pageSize;
	private int count;
	private int totalCount;
}
