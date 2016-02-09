package com.uber.departure.times.clients.nextbus.client;

import java.util.Collection;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import com.uber.departure.times.clients.ProviderClient;
import com.uber.departure.times.clients.nextbus.NextBusClientConfiguration;
import com.uber.departure.times.clients.nextbus.client.parser.AgencyTagsParser;
import com.uber.departure.times.clients.nextbus.client.parser.PredictionsParser;
import com.uber.departure.times.clients.nextbus.client.parser.RouteTagsParser;
import com.uber.departure.times.clients.nextbus.client.parser.StopsParser;
import com.uber.departure.times.common.VerticleBean;
import com.uber.departure.times.pojo.Route;
import com.uber.departure.times.pojo.StopPredictions;
import com.uber.departure.times.pojo.Stops;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author Danila Ponomarenko
 */
public final class NextBusHttpClient extends VerticleBean<NextBusClientConfiguration> implements ProviderClient {
    private static final Logger logger = LoggerFactory.getLogger(NextBusHttpClient.class);

    protected final HttpClient httpClient;

    protected static final String COMMAND = "command=";

    public NextBusHttpClient(@NotNull Vertx vertx, @NotNull NextBusClientConfiguration conf) {
        super(vertx, conf);
        httpClient = vertx.createHttpClient();
    }

    private static final String COMMAND_AGENCY_LIST = "agencyList";

    @NotNull
    @Override
    public Future<Collection<String>> getAgencyTags() {
        final Future<Collection<String>> result = Future.future();
        httpClient.getNow(conf.getNextBusFeedURI() + '?' + COMMAND + COMMAND_AGENCY_LIST,
                r -> responseHandler(r, result, AgencyTagsParser.PARSER)
        );
        return result;
    }

    private static final String COMMAND_ROUTE_LIST = "routeList";

    @NotNull
    @Override
    public Future<Collection<String>> getRouteTags(@NotNull String agencyTag) {
        final Future<Collection<String>> result = Future.future();
        httpClient.getNow(conf.getNextBusFeedURI() + '?' + COMMAND + COMMAND_ROUTE_LIST + "&a=" + agencyTag,
                r -> responseHandler(r, result, RouteTagsParser.PARSER)
        );
        return result;
    }

    private static final String COMMAND_ROUTE_CONFIG = "routeConfig";

    @NotNull
    @Override
    public Future<Stops> getStops(@NotNull Route route) {
        final Future<Stops> result = Future.future();
        httpClient.getNow(conf.getNextBusFeedURI() + '?' + COMMAND + COMMAND_ROUTE_CONFIG + "&a=" + route.getAgencyTag() + "&r=" + route.getRouteTag() + "&terse",
                r -> responseHandler(r, result, b -> StopsParser.PARSER.parse(route, b))
        );
        return result;
    }

    private static final String COMMAND_PREDICTIONS = "predictions";

    @NotNull
    @Override
    public Future<StopPredictions> predict(@NotNull String agencyTag, @NotNull String routeTag, @NotNull String stopTag) {
        final Future<StopPredictions> result = Future.future();
        httpClient.getNow(conf.getNextBusFeedURI() + '?' + COMMAND + COMMAND_PREDICTIONS + "&a=" + agencyTag + "&r=" + routeTag + "&s=" + stopTag,
                r -> responseHandler(r, result, b -> PredictionsParser.PARSER.parse(agencyTag, routeTag, stopTag, b))
        );
        return result;
    }


    private <T> void responseHandler(@NotNull HttpClientResponse response, @NotNull Future<T> f, @NotNull Function<Buffer, T> responseParser) {
        final int code = response.statusCode();
        if (code != 200) {
            logger.error("unexpected status-code:" + code + " message:" + response.statusMessage(), new Exception());
            f.fail("unexpected status-code:" + code);
        } else {
            response.bodyHandler(b -> f.complete(responseParser.apply(b)));
        }
    }
}
