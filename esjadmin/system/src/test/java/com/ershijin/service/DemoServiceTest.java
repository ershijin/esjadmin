package com.ershijin.service;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoServiceTest {
//    @Autowired
//    private DemoService demoService;
//
//    @Test
//    public void testGet() {
//        demoService.findById(1L);
//    }
//
//    @Test
//    public void testList() {
//        System.out.println(demoService.list(null));
//    }
//
//    @Test
//    public void testListPage() {
//        Page<Demo> page = new Page<>();
//        page.setCurrent(1);
//        page.setSize(2);
//        System.out.println(demoService.list(null, page));
//    }
//
//    @Test
//    public void testSave() {
//        Demo demo = new Demo();
//        demo.setCategoryId(12);
//        demo.setTitle("test title");
//        demo.setLink("test link");
//        demoService.save(demo);
//
//        Assert.assertTrue(demo.getId() != null);
//
//        demoService.removeAll(new Long[]{demo.getId()});
//    }
//
//    @Test
//    public void testUpdate() {
//        DemoVO demoVO = demoService.findById(1L);
//        demoVO.setCategoryId(100);
//
//        demoService.update((Demo) MyBeanUtils.convert(demoVO, Demo.class));
//
//        DemoVO newDemoVO = demoService.findById(1L);
//
//        Assert.assertEquals((long) newDemoVO.getCategoryId(), 100L);
//    }
}
