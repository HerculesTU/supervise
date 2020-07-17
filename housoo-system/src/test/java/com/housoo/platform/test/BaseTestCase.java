package com.housoo.platform.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * 基础测试类
 *
 * @author gf
 */
@RunWith(SpringJUnit4ClassRunner.class) //使用Spring Test组件进行单元测试
@ContextConfiguration(locations = {"classpath:app-test.xml"})
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = false)
@Transactional //应用事务，这样测试就不会在数据库中留下痕迹
public class BaseTestCase extends AbstractJUnit4SpringContextTests {

}
