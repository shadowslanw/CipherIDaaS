package com.portal;

import com.alibaba.fastjson.JSONObject;
import com.portal.redis.RedisClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class TestCollectMail extends BaseControllerTest{

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private RedisClient<String,Object> redisClient = new RedisClient<>();

    @Before
    public void setUp() throws Exception{
        //MockMvcBuilders.webAppContextSetup(WebApplicationContext context)：指定WebApplicationContext，将会从该上下文获取相应的控制器并得到相应的MockMvc；
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();//建议使用这种
    }


    @Test
    public void testMailCollect() throws Exception {

        //检查是否已经绑定手机号
        MultiValueMap map = new LinkedMultiValueMap();
        map.add("companyUUid","123456");
        map.add("userId","403d66d4828b11e9b5af00163e00cc6a");

        MvcResult result = getMvcResult(map,"/cipher/user/getContactInfo");
        String content = result.getResponse().getContentAsString();
        JSONObject jsonObject = parseJsonObject(content);

        Assert.assertEquals(getReturnCode(),0);
        Assert.assertEquals(jsonObject.getIntValue("isMailCollect"),1);



        String code = "422808";

        Assert.assertEquals(getReturnCode(),0);

        //进行手机号绑定
        map.clear();
        map.add("companyUUid","123456");
        map.add("mail","957444517@qq.com");
        map.add("code",code);
        map.add("userId","403d66d4828b11e9b5af00163e00cc6a");


        result = getMvcResult(map,"/cipher/user/collectMailNumber");

        content = result.getResponse().getContentAsString();

        jsonObject = parseJsonObject(content);

        Assert.assertEquals(getReturnCode(),0);

    }




}
