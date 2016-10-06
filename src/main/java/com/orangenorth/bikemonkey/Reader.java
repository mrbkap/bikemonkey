package com.orangenorth.bikemonkey;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

public class Reader {
    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String[] filenames;
        if (args.length == 0) {
            filenames = new String[] { "males.txt" };
        } else {
            filenames = args;
        }

        Vector<Rider> riders = new Vector<Rider>();
        for (int i = 0; i < filenames.length; i++) {
            JsonNode node = mapper.readTree(new File(filenames[i]));
            Rider[] records = mapper.treeToValue(node.get("records"), Rider[].class);
            riders.addAll(Arrays.asList(records));
        }

        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .appendHours()
                .appendLiteral(":")
                .appendMinutes()
                .appendLiteral(":")
                .appendSeconds()
                .toFormatter();

        Collections.sort(riders, (r1, r2) -> {
            Duration d1 = formatter.parsePeriod(r1.elapsedtime).toStandardDuration();
            Duration d2 = formatter.parsePeriod(r2.elapsedtime).toStandardDuration();
            return d1.compareTo(d2);
        });

        for (int i = 0; i < riders.size(); i++) {
            Rider r = riders.get(i);
            System.out.format("%d: [%s] %s %s - %s\n", i + 1, r.bib, r.firstname, r.lastname, r.elapsedtime);
        }
    }
}
