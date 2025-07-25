package com.globalpayex;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

public class TemperatureVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(FiboandFileCopyVerticle.class);


    @Override
    public void start() throws Exception {
        ArrayList<Integer> temperatures = new ArrayList<>();
        Random temp = new Random();

        vertx.setPeriodic(5000,(id)->{
            temperatures.add(temp.nextInt(30));
            logger.info("Temperatures list : {}",temperatures.toString());
        });

        vertx.setPeriodic(10000,(id)->{
            var average = temperatures.stream().collect(Collectors.averagingInt(tmp -> tmp));
            logger.info("Average Temperature : {}",average.toString());
        });
    }

    public static void main(String[] args) {
        Vertx.vertx()
                .deployVerticle(new TemperatureVerticle());
    }
}
