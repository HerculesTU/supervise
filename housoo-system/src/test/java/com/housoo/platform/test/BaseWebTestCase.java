package com.housoo.platform.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 基础WEB测试类
 *
 * @author gf
 */
@RunWith(SpringJUnit4ClassRunner.class) //使用Spring Test组件进行单元测试
@ContextConfiguration(locations={"classpath:app-test.xml","classpath:app-servlet.xml"})
@WebAppConfiguration
@TransactionConfiguration(transactionManager="txManager",defaultRollback=true)
@Transactional //应用事务，这样测试就不会在数据库中留下痕迹
public class BaseWebTestCase extends AbstractJUnit4SpringContextTests {

    protected MockMvc mockMvc;
    protected MockHttpSession mockHttpSession;
    protected MockHttpServletRequest request;
    protected MockHttpServletResponse response;

    @Autowired
    protected WebApplicationContext context;

    /**
     * 初始化SpringMvcController类测试环境
     */
    @Before
    public void initMockMvc() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        this.mockHttpSession = new MockHttpSession();
        mockMvc.perform(MockMvcRequestBuilders.post("/security/LoginController/backLogin")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("USERNAME", "admin")
                .param("PASSWORD", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92")
                .session(mockHttpSession))
                .andExpect(status().isOk())
                .andExpect(content().string("登陆成功"))
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void test(){

    }

}
