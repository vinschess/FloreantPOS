package com.floreantpos.model.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.Customer;

public class CustomerDAO extends BaseCustomerDAO {

	/**
	 * Default constructor. Can be used in place of getInstance()
	 */
	public CustomerDAO() {
	}

	public List<Customer> findBy(String phone, String loyalty, String name) {
		Session session = null;
		

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			Disjunction disjunction = Restrictions.disjunction();
			
			if(StringUtils.isNotEmpty(phone))
				disjunction.add(Restrictions.like(Customer.PROP_TELEPHONE_NO, "%" + phone + "%"));
			
			if(StringUtils.isNotEmpty(loyalty))
				disjunction.add(Restrictions.like(Customer.PROP_LOYALTY_NO, "%" + loyalty + "%"));
			
			if(StringUtils.isNotEmpty(name))
				disjunction.add(Restrictions.like(Customer.PROP_NAME, "%" + name + "%"));
			
			criteria.add(disjunction);

			return criteria.list();
			
		} finally {
			if (session != null) {
				closeSession(session);
			}
		}

	}
	
	/*public static void saveOrUpdateCustomerDetails(Customer customer) throws Exception{
		
		Session session = null;
		Transaction tx = null;
		
		GenericDAO dao = new GenericDAO();
		try {
			session = dao.createNewSession();
			tx = session.beginTransaction();
					
			CustomerDAO.getInstance().saveOrUpdate(customer);
			tx.commit();
		} catch (Exception e) {
			try {
				tx.rollback();
			} catch (Exception x) {
			}
			throw e;
		} finally {
			dao.closeSession(session);
		}
	}*/

}