package com.philos.frameit.services;

import java.util.List;

import org.apache.commons.mail.EmailException;

/**
 * Service for emailing.
 */
public interface MailService {

	/**
	 * Send an email to the 'to' address given, from the 'fromAddress' and 'fromName', with the given subject and HTML
	 * fragment. The HTML fragment can contain any HTML - the standard html, head, body elements are already present in
	 * the message template so just the HTML fragment is needed.
	 * 
	 * @param to the to address
	 * @param fromAddress the from address
	 * @param fromName the name the mail will appear from
	 * @param subject the mail subject
	 * @param htmlFragment the content - without the html, head or body tags (they are already in there)
	 * @throws EmailException
	 */
	public void sendEmail(final String to, final String fromAddress, final String fromName, final String subject, final String htmlFragment)
			throws EmailException;

	/**
	 * Send an email to the 'to' address given, from the 'fromAddress' and 'fromName', with the given subject and HTML
	 * fragment. The HTML fragment can contain any HTML - the standard html, head, body elements are already present in
	 * the message template so just the HTML fragment is needed.
	 * 
	 * @param to the to address
	 * @param cc the cc address
	 * @param bcc the bcc address
	 * @param fromAddress the from address
	 * @param fromName the name the mail will appear from
	 * @param subject the mail subject
	 * @param htmlFragment the content - without the html, head or body tags (they are already in there)
	 * @throws EmailException
	 */
	public void sendEmail(String to, String cc, String bcc, String fromAddress, String fromName, String subject, String htmlFragment) throws EmailException;

	/**
	 * Send an email to the 'to' address given, from the 'fromAddress' and 'fromName', with the given subject and HTML
	 * fragment. The HTML fragment can contain any HTML - the standard html, head, body elements are already present in
	 * the message template so just the HTML fragment is needed.
	 * 
	 * @param to the list of to address
	 * @param cc the list of cc address
	 * @param bcc the list of bcc address
	 * @param fromAddress the from address
	 * @param fromName the name the mail will appear from
	 * @param subject the mail subject
	 * @param htmlFragment the content - without the html, head or body tags (they are already in there)
	 * @throws EmailException
	 */
	public void sendEmail(List<String> to, List<String> cc, List<String> bcc, String fromAddress, String fromName, String subject, String htmlFragment)
			throws EmailException;
}
