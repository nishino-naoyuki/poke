package com.example.logviewe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Controller
@RequestMapping(value= {"/"})
public class IndexController {

	@RequestMapping(value= {""}, method=RequestMethod.GET)
    public ModelAndView index(
    		ModelAndView mv
    		)   {
    	mv.setViewName("index");
    	
    	return mv;
    }
}