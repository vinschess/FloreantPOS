package com.floreantpos.model;

public class KitchenReceiptVisibility {
	private static final long serialVersionUID = 1L;
	
	public static String REF = "KitchenTicket";
	public static String PROP_ID = "id";
	public static String PROP_ENABLE = "enable";

/*[CONSTRUCTOR MARKER BEGIN]*/
	public KitchenReceiptVisibility () {
	}

	/**
	 * Constructor for primary key
	 */
	public KitchenReceiptVisibility (java.lang.Integer id) {
		this.id = id;
	}
		

/*[CONSTRUCTOR MARKER END]*/
	
	// primary key
	private java.lang.Integer id;
	protected java.lang.Integer enable;

	/**
	 * @return the id
	 */
	public java.lang.Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	/**
	 * @return the enable
	 */
	public java.lang.Integer getEnable() {
		return enable;
	}

	/**
	 * @param enable the enable to set
	 */
	public void setEnable(java.lang.Integer enable) {
		this.enable = enable;
	}

	
	
}