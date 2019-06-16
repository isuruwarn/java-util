package org.warn.utils.core;

import org.warn.utils.json.JsonUtil;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringHelper {
	
	public static boolean isEmpty( String s ) {
		return s == null || s.isEmpty();
	}

	public static String arrayToString( Object[] arr ) {
		try {
			return JsonUtil.mapper.writeValueAsString( arr );
		} catch (JsonProcessingException e) {
			log.error("Error while converting array to string", e);
			return "";
		}
	}
}
