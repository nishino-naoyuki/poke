package com.example.logviewe.param.input;

import java.util.ArrayList;
import java.util.List;

import com.example.logviewe.Exception.NotInitializeException;

import lombok.Data;

@Data
public class InputData {
	private static InputData inputData = null;
	private List<String> blogList;
	private int readPt;
	
	public static InputData getInst() throws NotInitializeException {
		if( inputData == null ) {
			throw new NotInitializeException();
		}
		return inputData;
	}
	public static void initialize(List<String> fileContents) {
		inputData = new InputData();
		inputData.blogList = fileContents;
		inputData.readPt = 0;
	}
	
	public void reset() {
		readPt = 0;
	}
	public boolean next() {
		return ( readPt < blogList.size());
	}
	public int getNum() {
		return blogList.size();
	}
	public String read() {
		return blogList.get(readPt);
	}
	public String read(int readPt) {
		return blogList.get(readPt);
	}
	public String readOffset(int offset) {
		return blogList.get(readPt+offset);
	}
	public String readAndAhead() {
		String value = blogList.get(readPt);
		if( readPt < blogList.size()) {
			readPt++;
		}
		return value;
	}
	public void ahead() {
		readPt++;
	}
}
