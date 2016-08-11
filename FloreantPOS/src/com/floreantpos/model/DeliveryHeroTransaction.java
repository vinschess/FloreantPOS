package com.floreantpos.model;

import com.floreantpos.model.base.BaseDeliveryHeroTransaction;


public class DeliveryHeroTransaction extends BaseDeliveryHeroTransaction {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public DeliveryHeroTransaction () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public DeliveryHeroTransaction (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public DeliveryHeroTransaction (
		java.lang.Integer id,
		java.lang.String transactionType,
		java.lang.String paymentType) {

		super (
			id,
			transactionType,
			paymentType);
	}

	/*[CONSTRUCTOR MARKER END]*/
}