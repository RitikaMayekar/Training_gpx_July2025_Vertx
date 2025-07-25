package com.globalpayex;

import com.globalpayex.utils.Series;
import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComplexFiboSeriesVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(ComplexFiboSeriesVerticle.class);

    @Override
    public void start() throws Exception {
        var num = config().getInteger("num");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        var series = Series.fiboGenerator(num);
        logger.info("Fibo series for {} is {} ",num,series);
    }
}
