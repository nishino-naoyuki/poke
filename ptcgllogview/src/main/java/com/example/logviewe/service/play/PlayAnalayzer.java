package com.example.logviewe.service.play;

import java.util.List;

import com.example.logviewe.param.Play;
import com.example.logviewe.param.PlayerDto;

public interface PlayAnalayzer {
	Play getPlay(PlayerDto player,String turnPlayer,String line);
	Play getPlay(PlayerDto player,String turnPlayer,String line,List<String> subData);
}
