package com.globalpayex;

import com.globalpayex.routes.MyAppRouter;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyHTTPServerVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(MyHTTPServerVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        var port = config().getInteger("port");
        vertx
                .createHttpServer()
                .requestHandler(MyAppRouter.init(vertx,config()))
                .listen(port,(ar)->{
                    if(ar.succeeded()){
                        var mongoClient = MongoClient.createShared(vertx,config());
                        logger.info("Connected to mongo db");
                        logger.info("mongo object : "+mongoClient);
                        logger.info("Server started on port {}",port);
                        startPromise.complete();
                    }
                    else{
                        logger.error(ar.cause().getMessage());
                        startPromise.fail(ar.cause().getMessage());
                    }
                });
    }


    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        DeploymentOptions deploymentOptions = new DeploymentOptions();
        deploymentOptions.setConfig(
                new JsonObject()
                        .put("port",8085)
                        .put("connection_string","mongodb+srv://ritika1368:wlqNgrMMLofQUlHS@cluster0.xcp8u8s.mongodb.net/")
                        .put("db_name","college_db")
                        .put("useObjectId",true)
        );


        var deployFuture = vertx.deployVerticle(new MyHTTPServerVerticle(),deploymentOptions);

        deployFuture.onSuccess(id->{
            logger.info("Verticle Deployed ");
        });
        deployFuture.onFailure(err->{
            logger.error(err.getMessage());
            vertx.close();
        });

    }
}
