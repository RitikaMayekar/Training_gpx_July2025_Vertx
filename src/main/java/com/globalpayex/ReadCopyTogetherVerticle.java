package com.globalpayex;

import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.file.FileSystem;

import java.util.Scanner;


public class ReadCopyTogetherVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(FiboandFileCopyVerticle.class);

    private static final String filepath = "src/main/java/com/globalpayex/FiboandFileCopyVerticle2.java";

    private Future<Buffer> readFileAsync() {
        Promise<Buffer> promise = Promise.promise();
        vertx
                .fileSystem()
                .readFile(filepath, ar -> {
                    if (ar.succeeded()) {
                        promise.complete(ar.result());
                    } else {
                        promise.fail(ar.cause().getMessage());
                    }
                });
        return promise.future();
    }


    private Future<Void> copyFileAsync() {
        var destDirPath = "/Users/mehulchopra/Documents/gp-temp";
        Promise<Void> promise = Promise.promise();
        vertx
                .fileSystem()
                .copy(
                        filepath,
                        destDirPath + "/MultiOperationsVerticle.java",
                        (ar) -> {
                            if (ar.succeeded()) {
                                promise.complete();
                            } else {
                                promise.fail(ar.cause().getMessage());
                            }
                        }
                );
        return promise.future();
    }


    @Override
    public void start() throws Exception {
        var filesystem = vertx.fileSystem();

        var filepath = config().getString("filepath");
        var destpath = config().getString("destpath");


        Future<Buffer> f1 = filesystem
                .readFile(filepath);

        Future<Void> f2 = filesystem
                .copy(filepath,destpath);

        var compositeFuture = Future.all(f1,f2);

        compositeFuture.onSuccess(handler->{

            var f1Success = handler.resultAt(0);
            logger.info("F1 result"+f1Success);

            var f2Success = handler.resultAt(1);
            logger.info("F1 result"+f2Success);

        });

        compositeFuture.onFailure(err->{
            logger.error(err.getMessage());
        });
    }

    public static void main(String[] args) {

        try(Scanner sc = new Scanner(System.in)) {
            System.out.println("Enter a destination path : ");
            var filepath = sc.nextLine();
            var destpath = "src/main/java/com/globalpayex/constantfiles/copiedMultiOp.txt";

            Vertx vertx = Vertx.vertx();

            DeploymentOptions deploymentOptions = new DeploymentOptions()
                    .setConfig(new JsonObject()
                            .put("filepath", filepath)
                            .put("destpath",destpath)
                    );

            vertx.deployVerticle(new ReadCopyTogetherVerticle(),deploymentOptions);

            logger.info("Read and Copy Together Verticle has been deployed ");
        }
    }
}
