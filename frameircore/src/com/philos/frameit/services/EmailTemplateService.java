package com.philos.frameit.services;

import com.philos.frameit.model.template.EmailTemplateModel;

/**
 * This is the email template service. It uses Velocity templates and the methods provided will provide the data to the
 * Velocity template and return the mail body as a String.
 */
public interface EmailTemplateService {

	/**
	 * Finds an email template by code
	 * 
	 * @param code the code
	 * @return the email template oject
	 */
	public EmailTemplateModel getByCode(String code);
}