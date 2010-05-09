package com.loadtrend.web.mobile.test.dao;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TotalDAOTest extends TestCase {
    
    public void testTotalDAO() {
        TestSuite testSuite = new TestSuite();
        testSuite.addTestSuite(ProductHibernateDAOTest.class);
        testSuite.addTestSuite(ItemHibernateDAOTest.class);
        testSuite.addTestSuite(OrderHibernateDAOTest.class);
        testSuite.run(super.createResult());
    }
}
