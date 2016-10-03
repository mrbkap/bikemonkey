package com.orangenorth.bikemonkey;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Reader {
    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(new File("males.txt"));
        Rider[] riders = mapper.treeToValue(node.get("records"), Rider[].class);

        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .appendHours()
                .appendLiteral(":")
                .appendMinutes()
                .appendLiteral(":")
                .appendSeconds()
                .toFormatter();

        Arrays.sort(riders, (r1, r2) -> {
            Duration d1 = formatter.parsePeriod(r1.elapsedtime).toStandardDuration();
            Duration d2 = formatter.parsePeriod(r2.elapsedtime).toStandardDuration();
            return d1.compareTo(d2);
        });

        for (int i = 0; i < riders.length; i++) {
            Rider r = riders[i];
            System.out.format("%d: %s %s - %s\n", i, r.firstname, r.lastname, r.elapsedtime);
        }
    }
}
