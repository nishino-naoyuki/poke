package com.example.logviewe.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.logviewe.json.PokecaInfo;
import com.example.logviewe.json.ResponseInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PokeApiService {
	private final String SEARCH_CARDS = "https://api.pokemontcg.io/v2/cards";
	Logger logger = LoggerFactory.getLogger(PokeApiService.class);

	Map<String,String> map = new HashMap<>();
	
	public String findSmallImage(String name) throws JsonMappingException, JsonProcessingException {

		//すでにロード済みならAPIに投げない
		String imgPath = map.get(name);
		if( imgPath != null ) {
			return imgPath;
		}
		
		RestTemplate rest = new RestTemplate();

        final String url = SEARCH_CARDS + "?q=name:\""+name+"\" -rarity:\"Rare Ultra\" -rarity:\"Hyper Rare\" -rarity:\"Shiny Rare\" -rarity:\"Ultra Rare\" -rarity:\"Illustration Rare\" -rarity:\"Special Illustration Rare\"&orderBy=-set.releaseDate";

        logger.info(url);
        // 直接Beanクラスにマップ出来るけど今回はめんどくさいのでStringで。
        ResponseEntity<String> response = rest.getForEntity(url, String.class);

        String json = response.getBody();
    
        ObjectMapper mapper = new ObjectMapper();
        ResponseInfo pokeInfo = mapper.readValue(json, ResponseInfo.class);

        PokecaInfo[] dates = pokeInfo.getData();
        imgPath = dates[0].getImages().getSmall();
        
        map.put(name, imgPath);
        return imgPath;
	}
}