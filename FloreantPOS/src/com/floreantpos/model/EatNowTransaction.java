package com.floreantpos.model;

import com.floreantpos.model.base.BaseEatNowTransaction;


public class EatNowTransaction extends BaseEatNowTransaction {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public EatNowTransaction () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public EatNowTransaction (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public EatNowTransaction (
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