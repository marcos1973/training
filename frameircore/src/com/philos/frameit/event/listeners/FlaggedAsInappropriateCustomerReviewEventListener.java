package com.philos.frameit.event.listeners;

import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;

import org.apache.log4j.Logger;

import com.philos.frameit.events.FlaggedAsInappropriateCustomerReviewEvent;

public class FlaggedAsInappropriateCustomerReviewEventListener extends AbstractEventListener<FlaggedAsInappropriateCustomerReviewEvent> {
	private final static Logger LOG = Logger.getLogger(FlaggedAsInappropriateCustomerReviewEvent.class);

	/**
	 * Handle the flagged-as-inappropriate customer review event.
	 * 
	 * @param event the event
	 * @see de.hybris.platform.servicelayer.event.impl.AbstractEventListener#onEvent(de.hybris.platform.servicelayer.event.events.AbstractEvent)
	 */
	@Override
	protected void onEvent(final FlaggedAsInappropriateCustomerReviewEvent event) {
		LOG.info("Customer " + event.getBlockedBy().getUid() + " flagged review of '" + event.getReview().getProduct().getName() + "' ("
				+ event.getReview().getProduct().getCode() + ") by " + event.getReview().getUser().getUid() + " as inappropriate!");
		// TODO
		// We need to create a workflow for this and kick it off here.
		// This is the same process as the new customer review workflow (maybe with different states etc.)
		// so I will not do this here - this workspace is to provide an example and that has been done
		// with the new customer review workflow.
	}
}
