package com.uber.departure.times.rest;

import java.util.Objects;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.uber.departure.times.common.VerticleBean;
import com.uber.departure.times.hub.service.prediction.PredictionsClient;
import com.uber.departure.times.pojo.Location;
import com.uber.departure.times.pojo.StopsPredictions;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * @author Danila Ponomarenko
 */
public final class RestRouter extends VerticleBean<RestConfiguration> implements Handler<HttpServerRequest> {
    private static final Logger logger = LoggerFactory.getLogger(RestRouter.class);

    private final PredictionsClient client;
    private final Router router;

    public RestRouter(@NotNull Vertx vertx, @NotNull RestConfiguration conf) {
        super(vertx, conf);
        this.router = initRoutes(Router.router(vertx));
        this.client = new PredictionsClient(vertx.eventBus());
    }

    private Router initRoutes(@NotNull Router router) {
        router.get("/departure-times/api/v1/predictions").handler(this::stops);
        return router;
    }

    @Override
    public void handle(HttpServerRequest event) {
        router.accept(event);
    }

    private void stops(RoutingContext ctx) {
        final HttpServerRequest request = ctx.request();
        final Check<Location> check = checkLonLat(request.getParam("lon"), request.getParam("lat"));

        final HttpServerResponse response = ctx.response();
        if (!check.success()) {
            handleFailure(request, response, check.getError());
            return;
        }

        final Future<StopsPredictions> future = client.get(check.getResult());

        future.setHandler(r -> {
            if (r.succeeded()) {
                handleSuccess(response, r.result());
            } else {
                //noinspection ThrowableResultOfMethodCallIgnored
                handleFailure(request, response, "Internal error " + r.cause());
            }
        });
    }

    private void handleSuccess(@NotNull HttpServerResponse response, @NotNull StopsPredictions predictions) {
        //noinspection unchecked
        response.putHeader("content-type", "application/json")
                .setStatusCode(200)
                .end(predictions.toJsonArray().encode());
    }

    private static final String ERROR_FIELD = "error";
    private static final String UUID_FIELD = "issue";

    private void handleFailure(@NotNull HttpServerRequest request, @NotNull HttpServerResponse response, @NotNull String error) {
        final String issue = UUID.randomUUID().toString();
        final JsonObject json = new JsonObject();
        json.put(ERROR_FIELD, error);
        json.put(UUID_FIELD, issue);
        response.putHeader("content-type", "application/json")
                .setStatusCode(400)
                .end(json.encode());

        logger.error("API_ERROR " + issue + " request: " + request.uri());
    }

    @NotNull
    private Check<Location> checkLonLat(@Nullable String lonStr, @Nullable String latStr) {
        final StringBuilder sb = new StringBuilder();
        if (lonStr == null || latStr == null) {
            if (lonStr == null) {
                sb.append("'lon' param not found; ");
            }
            if (latStr == null) {
                sb.append("'lat' param not found; ");
            }
            return new Check<>(sb.toString());
        }

        final Double lon = parseDouble(lonStr);
        final Double lat = parseDouble(latStr);
        if (lon == null || lat == null) {
            if (lon == null) {
                sb.append("failed to parse 'lon'; ");
            }
            if (lat == null) {
                sb.append("failed to parse 'lat'; ");
            }
            return new Check<>(sb.toString());
        }

        if (!Location.validateLatitude(lat)) {
            sb.append("'lat' is not valid latitude; ");
        }

        if (!Location.validateLongitude(lon)) {
            sb.append("'lon' is not valid longitude; ");
        }

        return sb.length() == 0 ? new Check<>(new Location(lat, lon)) : new Check<>(sb.toString());
    }

    @Nullable
    private Double parseDouble(@NotNull String str) {
        try {
            return Double.valueOf(str);
        } catch (NumberFormatException ignore) {
            return null;
        }
    }

    private static class Check<T> {
        private final String error;
        private final T result;

        public Check(@NotNull String error) {
            this(Objects.requireNonNull(error), null);
        }

        public Check(@NotNull T result) {
            this(null, Objects.requireNonNull(result));
        }

        private Check(@Nullable String error, @Nullable T result) {
            this.error = error;
            this.result = result;
        }

        public String getError() {
            return error;
        }

        public T getResult() {
            return result;
        }

        public boolean success() {
            return error == null;
        }
    }
}
