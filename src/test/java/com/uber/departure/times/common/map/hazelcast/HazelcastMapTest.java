package com.uber.departure.times.common.map.hazelcast;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;

import com.uber.departure.times.AVertxTestClass;
import com.uber.departure.times.common.map.AsyncMap;
import com.uber.departure.times.mock.EntityHelper;
import com.uber.departure.times.mock.FutureHelper;
import com.uber.departure.times.mock.HazelcastMapFactory;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

/**
 * @author Danila Ponomarenko
 */
public final class HazelcastMapTest extends AVertxTestClass {

    @Resource(name = HazelcastMapFactory.BEAN_NAME)
    private AsyncMap<String, String> map;

    @Test
    public void testGetEmpty() {
        final String key = "" + EntityHelper.nextId();
        final String value = FutureHelper.wait(map.get(key));
        Assert.assertThat(value, is(equalTo(null)));
    }

    @Test
    public void testPut() {
        final String key = "" + EntityHelper.nextId();
        final String noValue = FutureHelper.wait(map.get(key));
        Assert.assertThat(noValue, is(equalTo(null)));

        final String newValue = "value";
        FutureHelper.wait(map.put(key, newValue, TimeUnit.DAYS.toMillis(1)));

        final String value = FutureHelper.wait(map.get(key));
        Assert.assertThat(value, is(equalTo(newValue)));
    }

    @Test
    public void testOverwrite() {
        final String key = "" + EntityHelper.nextId();
        final String noValue = FutureHelper.wait(map.get(key));
        Assert.assertThat(noValue, is(equalTo(null)));

        final String newValue = "value";
        FutureHelper.wait(map.put(key, newValue, TimeUnit.DAYS.toMillis(1)));

        final String value = FutureHelper.wait(map.get(key));
        Assert.assertThat(value, is(equalTo(newValue)));

        final String newValue2 = "value-2";
        FutureHelper.wait(map.put(key, newValue2, TimeUnit.DAYS.toMillis(1)));

        final String value2 = FutureHelper.wait(map.get(key));
        Assert.assertThat(value2, is(equalTo(newValue2)));
    }

    @Test
    public void testTTL() throws InterruptedException {
        final String key = "" + EntityHelper.nextId();
        final String noValue = FutureHelper.wait(map.get(key));
        Assert.assertThat(noValue, is(equalTo(null)));

        final String newValue = "value";
        FutureHelper.wait(map.put(key, newValue, 100));
        Thread.sleep(200);

        final String value = FutureHelper.wait(map.get(key));
        Assert.assertThat(value, is(equalTo(null)));
    }

}
