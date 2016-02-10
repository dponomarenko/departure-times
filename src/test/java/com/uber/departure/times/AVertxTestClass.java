package com.uber.departure.times;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Danila Ponomarenko
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {AVertxTestClass.CONFIG_TEST})
public abstract class AVertxTestClass {
    public static final String CONFIG_TEST = "classpath:test-context.xml";
}
