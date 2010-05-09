package com.loadtrend.web.mobile.dao.hibernate;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.loadtrend.web.mobile.dao.UserRecorderDAO;
import com.loadtrend.web.mobile.dao.model.UserRecorder;

public class UserRecorderHibernateDAO  extends HibernateDaoSupport implements UserRecorderDAO {

	public UserRecorder getUserRecorder(String id) {
		return (UserRecorder) super.getHibernateTemplate().get(UserRecorder.class, id);
	}

	public void saveUserRecorder(UserRecorder userRecorder) {
		super.getHibernateTemplate().saveOrUpdate(userRecorder);
	}

	public void removeUserRecorder(String id) {
        super.getHibernateTemplate().delete(this.getUserRecorder(id));
	}

}
