package com.floreantpos.model;

import com.floreantpos.model.base.BaseMenuLogTransaction;


public class MenuLogTransaction extends BaseMenuLogTransaction {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public MenuLogTransaction () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public MenuLogTransaction (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public MenuLogTransaction (
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