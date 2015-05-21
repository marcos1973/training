package com.philos.frameit.services.impl;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.util.mail.MailUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;

import com.philos.frameit.services.MailService;

/**
 * Service implementation for emailing.
 */
public class MailServiceImpl implements MailService {

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
	@Override
	public void sendEmail(final String to, final String fromAddress, final String fromName, final String subject, final String htmlFragment)
			throws EmailException {
		// Wrap the sendEmail(full set of arguments) method.
		sendEmail(to, null, null, fromAddress, fromName, subject, htmlFragment);
	}

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
	@Override
	public void sendEmail(final List<String> to, final List<String> cc, final List<String> bcc, final String fromAddress, final String fromName,
			final String subject, final String htmlFragment) throws EmailException {
		final HtmlEmail mail = (HtmlEmail) MailUtils.getPreConfiguredEmail();
		mail.setCharset("UTF-8");

		// add all to addresses they should be not null!
		for (final String t : to) {
			mail.addTo(t);
		}

		if (cc != null && cc.size() > 0) {
			// add all cc emails
			for (final String c : cc) {
				if (c != null) {
					mail.addCc(c);
				}
			}
		}
		if (bcc != null && bcc.size() > 0) {
			// add all bcc emails
			for (final String b : bcc) {
				if (b != null) {
					mail.addBcc(b);
				}
			}
		}

		mail.setFrom(fromAddress, fromName);
		mail.setSubject(subject);
		mail.setHtmlMsg(htmlFragment);

		Logger.getLogger(this.getClass()).info("Sending mail to '" + to + "' with subject '" + subject + "'.");

		mail.send();
	}

	/**
	 * Method used for sending an email with media attachments
	 * 
	 * @param to the list of to address
	 * @param cc the list of cc address
	 * @param bcc the list of bcc address
	 * @param fromAddress the from address
	 * @param fromName the name the mail will appear from
	 * @param subject the mail subject
	 * @param htmlFragment the content - without the html, head or body tags (they are already in there)
	 * @param siteUrl - the siteUrl which will be appended before the mediaAttachement(s).getUrl() example
	 *            http://localhost:9001
	 * @param attachments - the list of MediaModels which will be attached to the mail
	 * @throws EmailException
	 * @throws MalformedURLException
	 * @throws MessagingException
	 */
	public void sendEmailWithMediaAttachments(final List<String> to, final List<String> cc, final List<String> bcc, final String fromAddress,
			final String fromName, final String subject, final String htmlFragment, final String siteUrl, final Collection<? extends MediaModel> attachments)
			throws EmailException, MalformedURLException, MessagingException {
		final HtmlEmail mail = (HtmlEmail) MailUtils.getPreConfiguredEmail();

		mail.setCharset("UTF-8");

		// add all to addresses they should be not null!
		for (final String t : to) {
			mail.addTo(t);
		}

		if (cc != null && cc.size() > 0) {
			// add all cc emails
			for (final String c : cc) {
				if (c != null) {
					mail.addCc(c);
				}
			}
		}
		if (bcc != null && bcc.size() > 0) {
			// add all bcc emails
			for (final String b : bcc) {
				if (b != null) {
					mail.addBcc(b);
				}
			}
		}

		mail.setFrom(fromAddress, fromName);
		mail.setSubject(subject);

		// Create the message part

		BodyPart messageBodyPart = new MimeBodyPart();
		// Fill the message
		messageBodyPart.setContent(htmlFragment, "text/html");
		// messageBodyPart.setText(htmlFragment);

		final MimeMultipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);

		if (attachments != null && attachments.size() > 0) {
			for (final MediaModel attachment : attachments) {
				messageBodyPart = new MimeBodyPart();
				messageBodyPart.setDataHandler(new DataHandler(new URL(siteUrl + attachment.getURL())));
				messageBodyPart.setFileName(attachment.getRealFileName());
				multipart.addBodyPart(messageBodyPart);
			}
		}
		// Put parts in message
		mail.addPart(multipart);

		Logger.getLogger(this.getClass()).info("Sending mail to '" + to + "' with subject '" + subject + "'.");

		mail.send();
	}

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
	@Override
	public void sendEmail(final String to, final String cc, final String bcc, final String fromAddress, final String fromName, final String subject,
			final String htmlFragment) throws EmailException {

		this.sendEmail(Collections.singletonList(to), cc != null ? Collections.singletonList(cc) : Collections.<String> emptyList(),
				bcc != null ? Collections.singletonList(bcc) : Collections.<String> emptyList(), fromAddress, fromName, subject, htmlFragment);
	}
}
