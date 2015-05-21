package com.philos.frameit.services;

import java.util.Map;

/**
 * This is the velocity service. It uses Velocity templates and the data provided to get the content as a String.
 */
public interface VelocityService {

	/**
	 * Convert the template to a string using the data provided and the velocity template.
	 * 
	 * @param template the velocity template
	 * @param data the data to use in the velocity template
	 * @return a string with the converted template/data.
	 */
	public String toString(String template, Map<String, Object> data);
}