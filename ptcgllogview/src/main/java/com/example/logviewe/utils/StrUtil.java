package com.example.logviewe.utils;

public class StrUtil {
	public static String removeStr(String str,String[] removeStrings) {
		for(String removeString: removeStrings) {
			str = str.replace(removeString, "");
		}
		return str;
	}
}
