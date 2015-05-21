package com.philos.frameit.order.jaxb;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class OrderUpdate {
	String orderID;
	String status;

	/**
	 * Getter for the order ID.
	 * 
	 * @return the order ID.
	 */
	public String getOrderID() {
		return orderID;
	}

	/**
	 * Setter for the order ID.
	 * 
	 * @param orderID the order ID.
	 */
	@XmlElement
	public void setOrderID(final String orderID) {
		this.orderID = orderID;
	}

	/**
	 * Getter for the order status.
	 * 
	 * @return the order status.
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Setter for the order status.
	 * 
	 * @param status the order status.
	 */
	@XmlElement
	public void setStatus(final String status) {
		this.status = status;
	}
}