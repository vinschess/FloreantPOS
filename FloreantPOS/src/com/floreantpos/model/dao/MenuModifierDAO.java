package com.floreantpos.model.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.util.TransactionSummary;



public class MenuModifierDAO extends BaseMenuModifierDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public MenuModifierDAO () {}

	public List<MenuModifier> getMenuModifierUsingName(String name) {
		Session session = null;
		TransactionSummary summary = new TransactionSummary();
		try {
			session = getSession();

			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuModifier.PROP_NAME, name));

			return criteria.list();
			
		} finally {
			closeSession(session);
		}
	}

}