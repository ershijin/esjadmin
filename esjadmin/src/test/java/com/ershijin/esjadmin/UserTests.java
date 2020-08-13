package com.ershijin.esjadmin;

import com.ershijin.esjadmin.dao.UserMapper;
import com.ershijin.esjadmin.model.query.UserQuery;
import com.ershijin.esjadmin.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTests {
    @Autowired
    UserService userService;
    @Autowired
    UserMapper userMapper;

    @Test
    public void testUserServiceList() {
        userService.list(1, 10, new UserQuery());
    }

}
