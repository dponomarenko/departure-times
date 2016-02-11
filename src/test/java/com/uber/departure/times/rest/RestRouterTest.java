package com.uber.departure.times.rest;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.uber.departure.times.AVertxTestClass;
import com.uber.departure.times.common.SpringVerticleConnector;
import com.uber.departure.times.common.pojo.Location;
import com.uber.departure.times.common.pojo.ProvidedPredictions;
import com.uber.departure.times.common.pojo.Stop;
import com.uber.departure.times.mock.EntityHelper;
import com.uber.departure.times.mock.FutureHelper;
import com.uber.departure.times.mock.TestStorage;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.json.JsonArray;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

/**
 * @author Danila Ponomarenko
 */
public final class RestRouterTest extends AVertxTestClass {
    @Autowired
    private SpringVerticleConnector connector;
    @Autowired
    private HttpClient httpClient;
    @Autowired
    private TestStorage storage;
    @Autowired
    private RestConfiguration conf;


    private static final String HOST = "localhost";
    private static final String URI = "/departure-times/api/v1/predictions";

    @Test
    public void testNoArgs() {
        FutureHelper.wait(connector.startFuture());
        final Future<HttpClientResponse> f = Future.future();
        httpClient.getNow(conf.getHttpPort(), HOST, URI, f::complete);
        final HttpClientResponse response = FutureHelper.wait(f);
        Assert.assertThat(response.getHeader("content-type"), is(equalTo("application/json")));
        Assert.assertThat(response.statusCode(), is(equalTo(400)));
    }

    @Test
    public void testOnlyLat() {
        FutureHelper.wait(connector.startFuture());
        final Future<HttpClientResponse> f = Future.future();
        httpClient.getNow(conf.getHttpPort(), HOST, URI + "?lat=0", f::complete);
        final HttpClientResponse response = FutureHelper.wait(f);
        Assert.assertThat(response.getHeader("content-type"), is(equalTo("application/json")));
        Assert.assertThat(response.statusCode(), is(equalTo(400)));
    }

    @Test
    public void testOnlyLon() {
        FutureHelper.wait(connector.startFuture());
        final Future<HttpClientResponse> f = Future.future();
        httpClient.getNow(conf.getHttpPort(), HOST, URI + "?lon=0", f::complete);
        final HttpClientResponse response = FutureHelper.wait(f);
        Assert.assertThat(response.getHeader("content-type"), is(equalTo("application/json")));
        Assert.assertThat(response.statusCode(), is(equalTo(400)));
    }

    @Test
    public void testWrongLat() {
        FutureHelper.wait(connector.startFuture());
        final Future<HttpClientResponse> f = Future.future();
        httpClient.getNow(conf.getHttpPort(), HOST, URI + "?lat=1000&lon=0", f::complete);
        final HttpClientResponse response = FutureHelper.wait(f);
        Assert.assertThat(response.getHeader("content-type"), is(equalTo("application/json")));
        Assert.assertThat(response.statusCode(), is(equalTo(400)));
    }

    @Test
    public void testWrongLon() {
        FutureHelper.wait(connector.startFuture());
        final Future<HttpClientResponse> f = Future.future();
        httpClient.getNow(conf.getHttpPort(), HOST, URI + "?lat=0&lon=1000", f::complete);
        final HttpClientResponse response = FutureHelper.wait(f);
        Assert.assertThat(response.getHeader("content-type"), is(equalTo("application/json")));
        Assert.assertThat(response.statusCode(), is(equalTo(400)));
    }


    @Test
    public void testNoData() {
        FutureHelper.wait(connector.startFuture());
        final Future<HttpClientResponse> responseFuture = Future.future();
        final Future<Buffer> bodyFuture = Future.future();
        httpClient.getNow(conf.getHttpPort(), HOST, URI + "?lat=0&lon=0", r -> {
            r.bodyHandler(bodyFuture::complete);
            responseFuture.complete(r);
        });
        final HttpClientResponse response = FutureHelper.wait(responseFuture);
        Assert.assertThat(response.getHeader("content-type"), is(equalTo("application/json")));
        Assert.assertThat(response.statusCode(), is(equalTo(200)));

        response.bodyHandler(bodyFuture::complete);
        final Buffer buffer = FutureHelper.wait(bodyFuture);
        Assert.assertThat(buffer.toJsonArray().isEmpty(), is(equalTo(true)));
    }

    @Test
    public void testPredefinedPrediction() {
        FutureHelper.wait(connector.startFuture());

        final Stop stop = EntityHelper.randomStop();
        final Location location = stop.getLocation();
        final ProvidedPredictions predictions = new ProvidedPredictions("agency", "route", "stop", "direction", new long[]{TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + TimeUnit.MINUTES.toSeconds(5)});
        storage.addSync(stop, predictions);

        final Future<HttpClientResponse> responseFuture = Future.future();
        final Future<Buffer> bodyFuture = Future.future();
        httpClient.getNow(conf.getHttpPort(), HOST, URI + "?lat=" + location.getLatitude() + "&lon=" + location.getLongitude(), r -> {
            r.bodyHandler(bodyFuture::complete);
            responseFuture.complete(r);
        });
        final HttpClientResponse response = FutureHelper.wait(responseFuture);
        Assert.assertThat(response.getHeader("content-type"), is(equalTo("application/json")));
        Assert.assertThat(response.statusCode(), is(equalTo(200)));

        response.bodyHandler(bodyFuture::complete);
        final Buffer buffer = FutureHelper.wait(bodyFuture);
        final JsonArray array = buffer.toJsonArray();
        Assert.assertThat(array.size(), is(equalTo(1)));
    }
}
