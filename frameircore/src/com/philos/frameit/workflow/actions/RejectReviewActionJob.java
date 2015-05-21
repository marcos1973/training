package com.philos.frameit.workflow.actions;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.customerreview.enums.CustomerReviewApprovalType;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.workflow.jalo.WorkflowAction;
import de.hybris.platform.workflow.jalo.WorkflowDecision;
import de.hybris.platform.workflow.jobs.AutomatedWorkflowTemplateJob;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;

import com.philos.frameit.model.template.EmailTemplateModel;
import com.philos.frameit.services.EmailTemplateService;
import com.philos.frameit.services.MailService;
import com.philos.frameit.services.VelocityService;

@SuppressWarnings("deprecation")
public class RejectReviewActionJob implements AutomatedWorkflowTemplateJob, de.hybris.platform.workflow.jalo.AutomatedWorkflowTemplateJob {
	private final static Logger LOG = Logger.getLogger(ApproveReviewActionJob.class);
	private ModelService modelService;
	private MailService mailService;
	private EmailTemplateService emailTemplateService;
	private VelocityService velocityService;

	/**
	 * Workflow has been approved manually - the workflow task status has been set to "Approve Review".
	 * 
	 * @param workflowActionModel the workflow action model
	 * @return the workflow decision
	 * @see de.hybris.platform.workflow.jobs.AutomatedWorkflowTemplateJob#perform(de.hybris.platform.workflow.model.WorkflowActionModel)
	 */
	@Override
	public WorkflowDecisionModel perform(final WorkflowActionModel workflowActionModel) {
		if (workflowActionModel.getAttachmentItems() != null) {
			for (final ItemModel item : workflowActionModel.getAttachmentItems()) {
				if (item instanceof CustomerReviewModel) {
					final CustomerReviewModel review = (CustomerReviewModel) item;
					LOG.info(review.getUser().getUid() + "'s review (" + review.getHeadline() + ") of product '" + review.getProduct().getName() + "' ("
							+ review.getProduct().getCode() + ") has been rejected.");
					final EmailTemplateModel template = getEmailTemplateService().getByCode("review_rejected_email_template");
					final Map<String, Object> data = new HashMap<String, Object>();
					data.put("review", review);
					data.put("product", review.getProduct());
					data.put("reviewGuidelinesUrl", "TODO");
					data.put("user", review.getUser());

					final String body = getVelocityService().toString(template.getBody(), data);
					final String subject = getVelocityService().toString(template.getSubject(), data);

					try {
						getMailService().sendEmail("eoin@localhost", "info@frameit.test.com", "Frame It Customer Support", subject, body);
					} catch (final EmailException e) {
						LOG.error("Email creation error!", e);
					}

					review.setApprovalStatus(CustomerReviewApprovalType.REJECTED);
					getModelService().save(review);
				}
			}
		}
		for (final WorkflowDecisionModel decision : workflowActionModel.getDecisions()) {
			return decision;
		}
		return null;
	}

	/**
	 * Have to implement the old jalo stuff to make this work with the hMC.
	 * 
	 * @param workflowAction the workflow action
	 * @return the workflow decision
	 * @see de.hybris.platform.workflow.jalo.AutomatedWorkflowTemplateJob#perform(de.hybris.platform.workflow.jalo.WorkflowAction)
	 */
	@Override
	public WorkflowDecision perform(final WorkflowAction workflowAction) {
		final WorkflowActionModel workflowActionModel = getModelService().get(workflowAction.getPK());
		final WorkflowDecision decision = getModelService().getSource(perform(workflowActionModel));
		return decision;
	}

	/**
	 * Getter for the model service.
	 * 
	 * @return the model service.
	 */
	public ModelService getModelService() {
		return modelService;
	}

	/**
	 * Setter for the model service.
	 * 
	 * @param modelService the model service.
	 */
	public void setModelService(final ModelService modelService) {
		this.modelService = modelService;
	}

	/**
	 * Getter for the mail service.
	 * 
	 * @return the mail service.
	 */
	public MailService getMailService() {
		return mailService;
	}

	/**
	 * Setter for the mail service.
	 * 
	 * @param mailService the mail service.
	 */
	public void setMailService(final MailService mailService) {
		this.mailService = mailService;
	}

	/**
	 * Getter for the email template service.
	 * 
	 * @return the email template service.
	 */
	public EmailTemplateService getEmailTemplateService() {
		return emailTemplateService;
	}

	/**
	 * Setter for the email template service.
	 * 
	 * @param emailTemplateService the email template service.
	 */
	public void setEmailTemplateService(final EmailTemplateService emailTemplateService) {
		this.emailTemplateService = emailTemplateService;
	}

	/**
	 * Getter for the velocity service.
	 * 
	 * @return the velocity service.
	 */
	public VelocityService getVelocityService() {
		return velocityService;
	}

	/**
	 * Setter for the velocity service.
	 * 
	 * @param velocityService the velocity service.
	 */
	public void setVelocityService(final VelocityService velocityService) {
		this.velocityService = velocityService;
	}
}
