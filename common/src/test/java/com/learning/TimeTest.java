package com.learning;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @ClassName TimeTest
 * @Description TODO
 * @Author hufei
 * @Date 2023/6/21 11:08
 * @Version 1.0
 */
public class TimeTest {

    private final boolean SORT_BY_REGION = false;

    @Test
    void localZoneTest() {
        System.out.println(getZoneIdAndItsOffSet(null, null));
    }

    @Test
    void allZoneTest() {

        Map<String, String> sortedMap = new LinkedHashMap<>();

        Map<String, String> allZoneIdsAndItsOffSet = getAllZoneIdsAndItsOffSet();

        //sort map by key
        if (SORT_BY_REGION) {
            allZoneIdsAndItsOffSet.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEachOrdered(e -> sortedMap.put(e.getKey(), e.getValue()));
        } else {
            // sort by value, descending order
            allZoneIdsAndItsOffSet.entrySet().stream()
                    .sorted(Map.Entry.<String, String>comparingByValue().reversed())
                    .forEachOrdered(e -> sortedMap.put(e.getKey(), e.getValue()));
        }

        // print map
        sortedMap.forEach((k, v) ->
        {
            String out = String.format("%35s (UTC%s) %n", k, v);
            System.out.print(out);
        });

        System.out.println("\nTotal Zone IDs " + sortedMap.size());
    }

    private Map<String, String> getAllZoneIdsAndItsOffSet() {

        Map<String, String> result = new HashMap<>();

        LocalDateTime localDateTime = LocalDateTime.now();

        for (String zoneId : ZoneId.getAvailableZoneIds()) {

            ZoneId id = ZoneId.of(zoneId);

            result.put(id.toString(), getZoneIdAndItsOffSet(localDateTime, id));
        }

        return result;
    }

    private String getZoneIdAndItsOffSet(LocalDateTime now, ZoneId zoneId) {

        LocalDateTime localDateTime = Optional.ofNullable(now).orElse(LocalDateTime.now());

        ZoneId id = Optional.ofNullable(zoneId).orElse(ZoneId.systemDefault());

        // LocalDateTime -> ZonedDateTime
        ZonedDateTime zonedDateTime = localDateTime.atZone(id);

        // ZonedDateTime -> ZoneOffset
        ZoneOffset zoneOffset = zonedDateTime.getOffset();

        //replace Z to +00:00
        return zoneOffset.getId().replaceAll("Z", "+00:00");
    }
}
