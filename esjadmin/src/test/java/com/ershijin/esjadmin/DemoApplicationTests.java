package com.ershijin.esjadmin;

import com.ershijin.esjadmin.dao.UserMapper;
import com.ershijin.esjadmin.model.entity.User;
import com.ershijin.esjadmin.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
    @Autowired
    UserService userService;
    @Autowired
    UserMapper userDao;

    @Test
    public void contextLoads() {
        User user = (User) userDao.getByUsername("admin");
        System.out.println(user);
    }

}
