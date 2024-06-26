package com.example.logviewe.service.play;

import java.util.List;

import com.example.logviewe.param.GameInfo;
import com.example.logviewe.param.Play;
import com.example.logviewe.param.PlayerDto;

public interface PlayAnalayzer {
	Play getPlay(GameInfo gameInfo,String turnPlayer,String line);
	Play getPlay(GameInfo gameInfo,String turnPlayer,String line,List<String> subData);
}
