package com.example.logviewe.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.example.logviewe.json.PokecaInfo;
import com.example.logviewe.json.ResponseInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping(value= {"/test"})
public class TestController {

	@ResponseBody
	@RequestMapping(value= {"/api"}, method=RequestMethod.GET)
    public String input(
    		) throws JsonMappingException, JsonProcessingException  {
		
		RestTemplate rest = new RestTemplate();
        final String endpoint = "https://api.pokemontcg.io/v2/cards";

        final String url = endpoint + "?q=name:\"Radiant Alakazam\"";

        // 直接Beanクラスにマップ出来るけど今回はめんどくさいのでStringで。
        ResponseEntity<String> response = rest.getForEntity(url, String.class);

        String json = response.getBody();
        
        //return json;
        
        ObjectMapper mapper = new ObjectMapper();
        ResponseInfo pokeInfo = mapper.readValue(json, ResponseInfo.class);

        PokecaInfo[] dates = pokeInfo.getData();
        return (dates[0].getImages().getLarge());
	}
	

	@RequestMapping(value= {"/sample"}, method=RequestMethod.GET)
    public ModelAndView dashboad(
    		ModelAndView mv
    		) throws JsonMappingException, JsonProcessingException  {
		

		RestTemplate rest = new RestTemplate();
        final String endpoint = "https://api.pokemontcg.io/v2/cards";

        final String url = endpoint + "?q=name:\"Natu\"";

        // 直接Beanクラスにマップ出来るけど今回はめんどくさいのでStringで。
        ResponseEntity<String> response = rest.getForEntity(url, String.class);

        String json = response.getBody();
        
        //return json;
        
        ObjectMapper mapper = new ObjectMapper();
        ResponseInfo pokeInfo = mapper.readValue(json, ResponseInfo.class);

        PokecaInfo[] dates = pokeInfo.getData();
		mv.addObject("imgurl", dates[0].getImages().getSmall());
        mv.setViewName("sample");
        
		return mv;
	}


}
