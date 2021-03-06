package com.uber.departure.times.clients.nextbus;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.uber.departure.times.clients.DataProviderClient;
import com.uber.departure.times.clients.nextbus.parser.AgencyTagsParser;
import com.uber.departure.times.clients.nextbus.parser.PredictionsParser;
import com.uber.departure.times.clients.nextbus.parser.RouteTagsParser;
import com.uber.departure.times.clients.nextbus.parser.StopsParser;
import com.uber.departure.times.common.pojo.ProvidedPredictions;
import com.uber.departure.times.common.pojo.Route;
import com.uber.departure.times.common.pojo.StopId;
import com.uber.departure.times.common.pojo.Stops;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author Danila Ponomarenko
 */
@Component
public final class NextBusHttpClient implements DataProviderClient {
    private static final Logger logger = LoggerFactory.getLogger(NextBusHttpClient.class);

    private final HttpClient httpClient;
    private final NextBusClientConfiguration conf;

    @Autowired
    private StopsParser stopsParser;
    @Autowired
    private RouteTagsParser routeTagsParser;
    @Autowired
    private AgencyTagsParser agencyTagsParser;
    @Autowired
    private PredictionsParser predictionsParser;

    @Autowired
    public NextBusHttpClient(@NotNull Vertx vertx, @NotNull NextBusClientConfiguration conf) {
        this.conf = conf;
        httpClient = Objects.requireNonNull(vertx, "vertx")
                .createHttpClient(new HttpClientOptions().setConnectTimeout(conf.getConnectionTimeout()));
    }

    protected static final String COMMAND = "command=";

    private static final String COMMAND_AGENCY_LIST = "agencyList";

    @NotNull
    @Override
    public Future<Collection<String>> getAgencyTags() {
        final Future<Collection<String>> result = Future.future();
        httpClient.getNow(conf.getNextBusPort(), conf.getNextBusHost(), conf.getNextBusFeedURI() + '?' + COMMAND + COMMAND_AGENCY_LIST,
                r -> responseHandler(r, result, agencyTagsParser)
        );
        return result;
    }

    private static final String COMMAND_ROUTE_LIST = "routeList";

    @NotNull
    @Override
    public Future<Collection<String>> getRouteTags(@NotNull String agencyTag) {
        final Future<Collection<String>> result = Future.future();
        httpClient.getNow(conf.getNextBusPort(), conf.getNextBusHost(), conf.getNextBusFeedURI() + '?' + COMMAND + COMMAND_ROUTE_LIST + "&a=" + agencyTag,
                r -> responseHandler(r, result, routeTagsParser)
        );
        return result;
    }

    private static final String COMMAND_ROUTE_CONFIG = "routeConfig";

    @NotNull
    @Override
    public Future<Stops> getStops(@NotNull Route route) {
        final Future<Stops> result = Future.future();
        httpClient.getNow(conf.getNextBusPort(), conf.getNextBusHost(), conf.getNextBusFeedURI() + '?' + COMMAND + COMMAND_ROUTE_CONFIG + "&a=" + route.getAgencyTag() + "&r=" + route.getRouteTag() + "&terse",
                r -> responseHandler(r, result, b -> stopsParser.parse(route, b))
        );
        return result;
    }

    private static final String COMMAND_PREDICTIONS = "predictions";

    @NotNull
    @Override
    public Future<ProvidedPredictions> predict(@NotNull StopId stopId) {
        final Future<ProvidedPredictions> result = Future.future();
        httpClient.getNow(conf.getNextBusPort(), conf.getNextBusHost(), conf.getNextBusFeedURI() + '?' + COMMAND + COMMAND_PREDICTIONS + "&a=" + stopId.getAgencyId() + "&r=" + stopId.getRouteId() + "&s=" + stopId.getStopId(),
                r -> responseHandler(r, result, b -> predictionsParser.parse(stopId, b))
        );
        return result;
    }


    private <T> void responseHandler(@NotNull HttpClientResponse response, @NotNull Future<T> f, @NotNull Function<Buffer, T> responseParser) {
        final int code = response.statusCode();
        if (code != 200) {
            logger.error("unexpected status-code:" + code + " message:" + response.statusMessage(), new Exception());
            f.fail("unexpected status-code:" + code);
        } else {
            response.bodyHandler(b -> {
                try {
                    f.complete(responseParser.apply(b));
                } catch (Exception e) {
                    logger.error("unexpected response: " + b.toString(), e);
                    f.fail(e);
                }
            });
        }
    }
}
