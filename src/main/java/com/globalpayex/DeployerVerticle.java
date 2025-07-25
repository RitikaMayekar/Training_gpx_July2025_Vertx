package com.globalpayex;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class DeployerVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        var config = config();

        var deploymentOptions = new DeploymentOptions();

        deploymentOptions.setConfig(config);

        vertx.deployVerticle(new MyHTTPServerVerticle(),deploymentOptions);
        vertx.deployVerticle(new StatisticsVerticle(),deploymentOptions);
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
        vertx.deployVerticle(new DeployerVerticle(),deploymentOptions);
    }
}
