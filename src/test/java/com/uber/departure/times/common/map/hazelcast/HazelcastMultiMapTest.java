package com.uber.departure.times.common.map.hazelcast;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;

import com.uber.departure.times.AVertxTestClass;
import com.uber.departure.times.common.map.AsyncMultiMap;
import com.uber.departure.times.mock.EntityHelper;
import com.uber.departure.times.mock.FutureHelper;
import com.uber.departure.times.mock.HazelcastMultiMapFactory;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

/**
 * @author Danila Ponomarenko
 */
public final class HazelcastMultiMapTest extends AVertxTestClass {
    @Resource(name = HazelcastMultiMapFactory.BEAN_NAME)
    private AsyncMultiMap<String, String> map;

    @Test
    public void testGetEmpty() {
        final String key = "" + EntityHelper.nextId();
        final Collection<String> value = FutureHelper.wait(map.get(key));
        Assert.assertThat(value.isEmpty(), is(equalTo(true)));
    }

    @Test
    public void testPut() {
        final String key = "" + EntityHelper.nextId();
        final Collection<String> empty = FutureHelper.wait(map.get(key));
        Assert.assertThat(empty.isEmpty(), is(equalTo(true)));

        final String newValue = "value";
        FutureHelper.wait(map.add(key, newValue));

        final Collection<String> value = FutureHelper.wait(map.get(key));
        Assert.assertThat(value.size(), is(equalTo(1)));
        Assert.assertThat(value.iterator().next(), is(equalTo(newValue)));
    }

    @Test
    public void testPutPut() {
        final String key = "" + EntityHelper.nextId();
        final Collection<String> empty = FutureHelper.wait(map.get(key));
        Assert.assertThat(empty.isEmpty(), is(equalTo(true)));

        final String newValue = "value";
        FutureHelper.wait(map.add(key, newValue));

        final Collection<String> value = FutureHelper.wait(map.get(key));
        Assert.assertThat(value.size(), is(equalTo(1)));
        Assert.assertThat(value.iterator().next(), is(equalTo(newValue)));

        final String newValue2 = "value2";
        FutureHelper.wait(map.add(key, newValue2));

        final Collection<String> value2 = FutureHelper.wait(map.get(key));
        Assert.assertThat(value2.size(), is(equalTo(2)));
        final Iterator<String> iterator = value2.iterator();
        Assert.assertThat(new HashSet<>(value2), is(equalTo(new HashSet<>(Arrays.asList(newValue, newValue2)))));
    }
}
