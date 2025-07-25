package com.globalpayex.routes;

import com.globalpayex.domain.Book;
import io.vertx.core.Vertx;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class MyAppRouter {


    private static final Logger logger = LoggerFactory.getLogger(MyAppRouter.class);

    public static Router init(Vertx vertx, JsonObject config){

        var router = Router.router(vertx);

        //this ensures vertx collect and parses request data

        router.post().handler(BodyHandler.create());

        router.get("/hello/:username")
                .handler(MyAppRouter::displayUsername);

        router.get("/download")
                .handler((routingContext -> MyAppRouter.downloadMP3(vertx,routingContext)));

        router = BooksRoute.init(router,vertx,config);
        router = StudentsRoute.init(router,vertx,config);

        return router;
    }

    private static void downloadMP3(Vertx vertx, RoutingContext routingContext) {
        var filepath = "/home/gpxpwrusr/Downloads/sampleMusic.mp3";

        var fileFuture = vertx.fileSystem()
                .open(filepath,
                new OpenOptions().setRead(true));

        var reponse = routingContext
                .response()
                .setStatusCode(200)
                .putHeader("Content-Type","audio/mp3")
                .putHeader("Content-Disposition","attachment")
                .setChunked(true);

        fileFuture.onSuccess((asyncFile)->{
            //internally handles the streaming backpressure
            //backpressure implementation in short code
            asyncFile.pipeTo(reponse);

            //manual backpressure implementation:
//            asyncFile.handler(buffer -> {
//
//                reponse.write(buffer);
//                if(reponse.writeQueueFull()){
//                    asyncFile.pause();
//                }
//                reponse.drainHandler(v -> asyncFile.resume());
//            });
//            asyncFile.endHandler(v->{
//                reponse.end();
//            });
        }
        );

        fileFuture.onFailure(err->{
            logger.error(err.getMessage());
        });

    }

    static void displayUsername(RoutingContext routingContext){
        var uname = routingContext.pathParam("username");
        routingContext
                .response()
                .end("hey there "+uname);
    }


}
