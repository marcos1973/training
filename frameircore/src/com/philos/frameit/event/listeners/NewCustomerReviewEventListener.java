package com.philos.frameit.event.listeners;

import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.WorkflowProcessingService;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.WorkflowTemplateService;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import org.apache.log4j.Logger;

import com.philos.frameit.events.NewCustomerReviewEvent;

public class NewCustomerReviewEventListener extends AbstractEventListener<NewCustomerReviewEvent> {
	private final static Logger LOG = Logger.getLogger(NewCustomerReviewEvent.class);
	private WorkflowService workflowService;
	private WorkflowTemplateService workflowTemplateService;
	private WorkflowProcessingService workflowProcessingService;
	private ModelService modelService;
	private UserService userService;

	/**
	 * Handle the new customer review event.
	 * 
	 * @param event the event
	 * @see de.hybris.platform.servicelayer.event.impl.AbstractEventListener#onEvent(de.hybris.platform.servicelayer.event.events.AbstractEvent)
	 */
	@Override
	protected void onEvent(final NewCustomerReviewEvent event) {
		LOG.info("New customer review of '" + event.getReview().getProduct().getName() + "' (" + event.getReview().getProduct().getCode() + ") received from: "
				+ event.getReview().getUser().getUid());

		final WorkflowTemplateModel workflowTemplate = getWorkflowTemplateService().getWorkflowTemplateForCode("customer_review_approval_workflow");

		final WorkflowModel workflow = getWorkflowService().createWorkflow(workflowTemplate, event.getReview(), getUserService().getAdminUser());
		// workflow.setAttachments(Collections.singletonList(event.getReview()));

		getModelService().save(workflow);

		for (final WorkflowActionModel action : workflow.getActions()) {
			getModelService().save(action);
		}

		getWorkflowProcessingService().startWorkflow(workflow);
	}

	/**
	 * Getter for the workflow service.
	 * 
	 * @return the workflow service.
	 */
	public WorkflowService getWorkflowService() {
		return workflowService;
	}

	/**
	 * Setter for the workflow service.
	 * 
	 * @param workflowService the workflow service.
	 */
	public void setWorkflowService(final WorkflowService workflowService) {
		this.workflowService = workflowService;
	}

	/**
	 * Getter for the workflow template service.
	 * 
	 * @return the workflow template service.
	 */
	public WorkflowTemplateService getWorkflowTemplateService() {
		return workflowTemplateService;
	}

	/**
	 * Setter for the workflow template service.
	 * 
	 * @param workflowTemplateService the workflow template service.
	 */
	public void setWorkflowTemplateService(final WorkflowTemplateService workflowTemplateService) {
		this.workflowTemplateService = workflowTemplateService;
	}

	/**
	 * Getter for the workflow processing service.
	 * 
	 * @return the workflow processing service.
	 */
	public WorkflowProcessingService getWorkflowProcessingService() {
		return workflowProcessingService;
	}

	/**
	 * Setter for the workflow processing service.
	 * 
	 * @param workflowProcessingService the workflow processing service.
	 */
	public void setWorkflowProcessingService(final WorkflowProcessingService workflowProcessingService) {
		this.workflowProcessingService = workflowProcessingService;
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
	 * Getter for the user service.
	 * 
	 * @return the user service.
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * Setter for the user service.
	 * 
	 * @param userService the user service.
	 */
	public void setUserService(final UserService userService) {
		this.userService = userService;
	}
}
