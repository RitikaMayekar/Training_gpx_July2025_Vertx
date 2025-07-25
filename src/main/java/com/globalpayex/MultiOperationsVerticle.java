package com.globalpayex;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MultiOperationsVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(MultiOperationsVerticle.class);

    @Override
    public void start() throws Exception {
        //init code for this verticle
        var a = 9;
        var b = 8;
        var sum = a+b;
        var multiply = a*b;

        var filesystem = vertx.fileSystem();
        var path = "src/main/java/com/globalpayex/MultiOperationsVerticle.java";

        filesystem.readFile(path,(ar)->{
            if(ar.succeeded()){
                var content = ar.result().toString();
                logger.info(content);
            }
            else{
                logger.error(ar.cause().getMessage());
            }
        });


        //simulation
//        Thread.sleep(30000);
        //Never ever block the event loop thread of vertx ->
        // that is -> do not write long running IO code in the event loop thread

        logger.info("Sum of {} and {} is {} : ",a,b,sum);
        logger.info("Product of {} and {} is {} : ",a,b,multiply);
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(new MultiOperationsVerticle());

        logger.info("Multi operation Verticle has been deployed ");
    }
}
