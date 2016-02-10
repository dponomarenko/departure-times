package com.uber.departure.times.mock;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.uber.departure.times.clients.DataProviderClient;
import com.uber.departure.times.common.pojo.Location;
import com.uber.departure.times.common.pojo.ProvidedPredictions;
import com.uber.departure.times.common.pojo.Route;
import com.uber.departure.times.common.pojo.Stop;
import com.uber.departure.times.common.pojo.StopId;
import com.uber.departure.times.common.pojo.Stops;

import io.vertx.core.Future;

import static org.mockito.Mockito.when;

/**
 * @author Danila Ponomarenko
 */
@Component
public final class DataProviderMockFactory {

    public static final Set<String> AGENCIES = new HashSet<>(Arrays.asList("reno", "sria", "ccny", "emery", "nova-se",
            "omnitrans", "lametro", "jhu-apl", "charles-river", "charm-city", "ecu", "brooklyn", "actransit", "portland-sc",
            "york-pa", "lasell", "cyride", "wku", "simi-valley", "unitrans", "sct", "foothill", "umn-twin", "stl", "collegetown",
            "sf-mission-bay", "dumbarton", "bronx", "winston-salem", "sf-muni", "oxford-ms", "vista", "rutgers-newark", "seattle-sc",
            "fairfax", "howard", "south-coast", "pgc", "umd", "staten-island", "glendale", "jtafla", "nctd", "chapel-hill", "ucsf",
            "georgia-college", "mit", "ft-worth", "thousand-oaks", "ccrta", "art", "thunderbay", "ttc", "brockton", "indianapolis-air",
            "camarillo", "roosevelt", "radford", "dc-circulator", "rutgers", "mbta", "pvpta", "moorpark", "da", "lametro-rail"));

    public static final String AC_TRANSIT = "actransit";

    public static final Set<String> AC_TRANSIT_ROUTES = new HashSet<>(Arrays.asList("88", "89", "58L", "46L", "232", "BSD", "356", "239", "93", "94",
            "BSN", "95", "97", "98", "11", "99", "12", "14", "51A", "51B", "18", "1", "7", "800", "801", "802", "805", "20", "21", "22", "1R",
            "B", "C", "25", "26", "E", "F", "G", "H", "J", "L", "M", "251", "O", "SB", "P", "376", "S", "U", "V", "W", "Z", "31", "CB", "32",
            "37", "39", "386", "LA", "LC", "40", "45", "46", "47", "48", "49", "275", "314", "52", "54", "57", "NX1", "NX3", "NX2", "NX4", "200",
            "840", "687", "NXC", "60", "62", "NL", "65", "67", "NX", "FS", "691", "210", "72M", "212", "851", "215", "72R", "216", "217", "70",
            "339", "71", "72", "73", "74", "75", "76", "OX", "83", "85", "86"));

    public static final Route AC_TRANSIT_39_ROUTE = new Route("actransit", "39");

    public static final Set<Stop> AC_TRANSIT_39_ROUTE_STOPS = new HashSet<>(Arrays.asList(
            new Stop(new StopId("actransit", "39", "5194"), new Location(37.7621399, -122.46631)),
            new Stop(new StopId("actransit", "39", "5198"), new Location(37.76191, -122.4737799)),
            new Stop(new StopId("actransit", "39", "7318"), new Location(37.76913, -122.43308)),
            new Stop(new StopId("actransit", "39", "5731"), new Location(37.7887, -122.4019199)),
            new Stop(new StopId("actransit", "39", "5223"), new Location(37.7601699, -122.50878)),
            new Stop(new StopId("actransit", "39", "7252"), new Location(37.7693899, -122.43369)),
            new Stop(new StopId("actransit", "39", "5200"), new Location(37.7616599, -122.47719)),
            new Stop(new StopId("actransit", "39", "5122"), new Location(37.7640899, -122.46428)),
            new Stop(new StopId("actransit", "39", "5195"), new Location(37.76207, -122.4693199)),
            new Stop(new StopId("actransit", "39", "5419"), new Location(37.7750699, -122.41919)),
            new Stop(new StopId("actransit", "39", "5213"), new Location(37.7606, -122.5025699)),
            new Stop(new StopId("actransit", "39", "5216"), new Location(37.76039, -122.5060599)),
            new Stop(new StopId("actransit", "39", "5196"), new Location(37.76199, -122.4695299)),
            new Stop(new StopId("actransit", "39", "5201"), new Location(37.7615299, -122.47985)),
            new Stop(new StopId("actransit", "39", "5234"), new Location(37.77972, -122.3898699)),
            new Stop(new StopId("actransit", "39", "5208"), new Location(37.7611099, -122.4895)),
            new Stop(new StopId("actransit", "39", "6995"), new Location(37.7843, -122.4078199)),
            new Stop(new StopId("actransit", "39", "5209"), new Location(37.7610299, -122.49293)),
            new Stop(new StopId("actransit", "39", "3914"), new Location(37.7654999, -122.45259)),
            new Stop(new StopId("actransit", "39", "5193"), new Location(37.7622299, -122.46669)),
            new Stop(new StopId("actransit", "39", "4510"), new Location(37.7907499, -122.3898399)),
            new Stop(new StopId("actransit", "39", "5417"), new Location(37.7842, -122.4076899)),
            new Stop(new StopId("actransit", "39", "5221"), new Location(37.7603599, -122.5090099)),
            new Stop(new StopId("actransit", "39", "6994"), new Location(37.78879, -122.4021299)),
            new Stop(new StopId("actransit", "39", "4448"), new Location(37.7694099, -122.4294)),
            new Stop(new StopId("actransit", "39", "7219"), new Location(37.7603499, -122.5092)),
            new Stop(new StopId("actransit", "39", "3912"), new Location(37.7649999, -122.45656)),
            new Stop(new StopId("actransit", "39", "4506"), new Location(37.78436, -122.3881399)),
            new Stop(new StopId("actransit", "39", "5205"), new Location(37.7613099, -122.48651)),
            new Stop(new StopId("actransit", "39", "5220"), new Location(37.76195, -122.4705699)),
            new Stop(new StopId("actransit", "39", "7217"), new Location(37.7932299, -122.39654)),
            new Stop(new StopId("actransit", "39", "3909"), new Location(37.7658599, -122.4497999)),
            new Stop(new StopId("actransit", "39", "6996"), new Location(37.7752299, -122.41934)),
            new Stop(new StopId("actransit", "39", "5211"), new Location(37.76074, -122.4993599)),
            new Stop(new StopId("actransit", "39", "3911"), new Location(37.7657499, -122.45013)),
            new Stop(new StopId("actransit", "39", "6997"), new Location(37.7786799, -122.41499)),
            new Stop(new StopId("actransit", "39", "5120"), new Location(37.76423, -122.4610599)),
            new Stop(new StopId("actransit", "39", "5215"), new Location(37.7604899, -122.50583)),
            new Stop(new StopId("actransit", "39", "5224"), new Location(37.7609099, -122.49556)),
            new Stop(new StopId("actransit", "39", "5225"), new Location(37.7608299, -122.49596)),
            new Stop(new StopId("actransit", "39", "3212"), new Location(37.76391, -122.46624)),
            new Stop(new StopId("actransit", "39", "5202"), new Location(37.76155, -122.4811599)),
            new Stop(new StopId("actransit", "39", "5204"), new Location(37.76137, -122.4835599)),
            new Stop(new StopId("actransit", "39", "3913"), new Location(37.7649299, -122.45651)),
            new Stop(new StopId("actransit", "39", "5237"), new Location(37.7796199, -122.38982)),
            new Stop(new StopId("actransit", "39", "5212"), new Location(37.7606799, -122.49915)),
            new Stop(new StopId("actransit", "39", "3915"), new Location(37.7653699, -122.45293)),
            new Stop(new StopId("actransit", "39", "5727"), new Location(37.77854, -122.4148099)),
            new Stop(new StopId("actransit", "39", "5207"), new Location(37.7611699, -122.48972)),
            new Stop(new StopId("actransit", "39", "5197"), new Location(37.7618499, -122.47278)),
            new Stop(new StopId("actransit", "39", "5119"), new Location(37.7643, -122.4608199)),
            new Stop(new StopId("actransit", "39", "5239"), new Location(37.7762099, -122.39423)),
            new Stop(new StopId("actransit", "39", "5203"), new Location(37.7614499, -122.4833)),
            new Stop(new StopId("actransit", "39", "5214"), new Location(37.76052, -122.5028399)),
            new Stop(new StopId("actransit", "39", "4509"), new Location(37.79048, -122.38969)),
            new Stop(new StopId("actransit", "39", "5210"), new Location(37.7609599, -122.4932)),
            new Stop(new StopId("actransit", "39", "5121"), new Location(37.76416, -122.4640299)),
            new Stop(new StopId("actransit", "39", "6992"), new Location(37.7931399, -122.3964)),
            new Stop(new StopId("actransit", "39", "7145"), new Location(37.7846299, -122.38798)),
            new Stop(new StopId("actransit", "39", "5206"), new Location(37.76123, -122.4867599)),
            new Stop(new StopId("actransit", "39", "5199"), new Location(37.7617399, -122.47682)),
            new Stop(new StopId("actransit", "39", "5219"), new Location(37.7620799, -122.47033)),
            new Stop(new StopId("actransit", "39", "5240"), new Location(37.7760599, -122.39436)),
            new Stop(new StopId("actransit", "39", "4447"), new Location(37.7694699, -122.42941)),
            new Stop(new StopId("actransit", "39", "5118"), new Location(37.7644399, -122.45864)),
            new Stop(new StopId("actransit", "39", "5124"), new Location(37.7643699, -122.45802)),
            new Stop(new StopId("actransit", "39", "5123"), new Location(37.7641099, -122.46616))
    ));

    public static final StopId AC_TRANSIT_39_5194_STOP_ID = new StopId("actransit", "39", "5194");

    public static final ProvidedPredictions AC_TRANSIT_39_5194_STOP_ID_PREDICTIONS = new ProvidedPredictions(
            "SF Muni",
            "N-Judah",
            "Judah St & 28th Ave",
            "Outbound to Ocean Beach via Downtown",
            new long[]{1455030113583L, 1455030569429L, 1455030689153L, 1455031194016L, 1455031893559L}
    );

    public static final StopId EMPTY_STOP_ID = EntityHelper.randomStopId();

    @Bean
    public DataProviderClient createClient() {
        final DataProviderClient mock = Mockito.mock(DataProviderClient.class, i -> Future.failedFuture("stub"));
        when(mock.getAgencyTags()).thenAnswer(i -> Future.succeededFuture(AGENCIES));
        when(mock.getRouteTags(AC_TRANSIT)).then(i -> Future.succeededFuture(AC_TRANSIT_ROUTES));
        when(mock.getRouteTags(AC_TRANSIT)).then(i -> Future.succeededFuture(AC_TRANSIT_ROUTES));
        when(mock.getStops(AC_TRANSIT_39_ROUTE)).then(i -> Future.succeededFuture(new Stops(AC_TRANSIT_39_ROUTE_STOPS)));
        when(mock.predict(AC_TRANSIT_39_5194_STOP_ID)).then(i -> Future.succeededFuture(AC_TRANSIT_39_5194_STOP_ID_PREDICTIONS));
        when(mock.predict(EMPTY_STOP_ID)).then(i -> Future.succeededFuture(null));
        return mock;
    }
}
