package com.ershijin;

import com.ershijin.component.Config;
import com.ershijin.service.ConfigService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigTests {
    @Autowired
    private ConfigService configService;
    @Test
    public void testGet() {
        System.out.println(configService.get("test"));
    }

    @Test
    public void testGetStatic() {
        System.out.println(Config.get("test"));
    }

    @Test
    public void testStaticGetAll() {
        System.out.println(Config.get());
    }
}
