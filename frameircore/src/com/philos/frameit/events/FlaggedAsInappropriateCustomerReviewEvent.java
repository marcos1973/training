package com.philos.frameit.events;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

public class FlaggedAsInappropriateCustomerReviewEvent extends AbstractEvent {
	private CustomerReviewModel review;
	private UserModel blockedBy;

	/**
	 * Constructor for BlockedCustomerReviewEvent.
	 * 
	 * @param review the review to be unblocked/rejected
	 * @param blockedBy the user that flagged this review as inappropriate
	 */
	public FlaggedAsInappropriateCustomerReviewEvent(final CustomerReviewModel review, final UserModel blockedBy) {
		super();
		this.review = review;
		this.blockedBy = blockedBy;
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

	/**
	 * Getter for the user that flagged this review as inappropriate
	 * 
	 * @return the user that flagged this review as inappropriate
	 */
	public UserModel getBlockedBy() {
		return blockedBy;
	}

	/**
	 * Setter for the user that flagged this review as inappropriate.
	 * 
	 * @param blockedBy the user that flagged this review as inappropriate
	 */
	public void setBlockedBy(final UserModel blockedBy) {
		this.blockedBy = blockedBy;
	}
}
