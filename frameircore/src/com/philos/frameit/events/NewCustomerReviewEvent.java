package com.philos.frameit.events;

import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

public class NewCustomerReviewEvent extends AbstractEvent {
	private CustomerReviewModel review;

	/**
	 * Constructor for NewCustomerReviewEvent.
	 * 
	 * @param review the review to be approved/rejected
	 */
	public NewCustomerReviewEvent(final CustomerReviewModel review) {
		super();
		this.review = review;
	}

	/**
	 * Getter for the review.
	 * 
	 * @return the review
	 */
	public CustomerReviewModel getReview() {
		return review;
	}

	/**
	 * Setter for the review.
	 * 
	 * @param review the review.
	 */
	public void setReview(final CustomerReviewModel review) {
		this.review = review;
	}
}
