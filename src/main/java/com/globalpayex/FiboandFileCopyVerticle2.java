package com.globalpayex;

import com.globalpayex.utils.Series;
import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.file.FileSystem;

import java.util.Arrays;

public class FiboandFileCopyVerticle2 extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(FiboandFileCopyVerticle2.class);

    private Future<Buffer> readFileAsync(String path, FileSystem fileSystem){
        Promise<Buffer> promise = Promise.promise();
        fileSystem.readFile(path, (rf)->{
            if(rf.succeeded()){
                promise.complete(rf.result());
            }
            else{
                promise.fail(rf.cause().getMessage());
            }
        });
        return promise.future();
    }
    private Future<Integer> writeFileAsync(String path, Buffer content, int element,FileSystem fileSystem){
        Promise<Integer> promise = Promise.promise();
        fileSystem.writeFile(path,content, writeAsyncResult -> {
            if(writeAsyncResult.succeeded()){
                promise.complete(element);
            }
            else{
                promise.fail(writeAsyncResult.cause().getMessage());
            }
        });
        return promise.future();
    }



    @Override
    public void start() throws Exception {

        var list = Arrays.asList(2, 3, 4, 5,6);

        var filesystem = vertx.fileSystem();

        var srcpath = "src/main/java/com/globalpayex/FiboandFileCopyVerticle.java";
        var destpath = "src/main/java/com/globalpayex/constantfiles/";


        list.forEach((num) -> {

            var readFuture = readFileAsync(srcpath,filesystem);

            var finalFuture = readFuture.compose(
                    buffer -> {
                return writeFileAsync(destpath+num+".txt",buffer,num,filesystem);
            }
            );

            finalFuture.onSuccess((result)->{
                logger.info("Copy success for {}",num);
            });
            finalFuture.onFailure(err->{
                logger.error(err.getMessage());
            });

            // this will become blocking hence shifted a new worker thread
            /* try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            var series = Series.fiboGenerator(num);
            logger.info("Fibo series for {} is {} ",num,series); */

            // a way to not block the event loop thread while performing any complex task :
            /* DeploymentOptions deploymentOptions = new DeploymentOptions();
            deploymentOptions.setConfig(new JsonObject()
                    .put("num",num))
                    .setThreadingModel(ThreadingModel.WORKER);

            vertx.deployVerticle(new ComplexFiboSeriesVerticle(),deploymentOptions); */


            //a way to skip crating a worker verticle manually :
            var seriesFuture = vertx.executeBlocking(()->{
                try {
                    //.sleep is just to simulate long running code
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                var series = Series.fiboGenerator(num);
                logger.info("Fibo series {} ",series);


                return series;
            },false);

            seriesFuture.onSuccess((series)->{
                logger.info("Fibo series {} ",series);
            });
        });
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        //used when only single instance of the event loop verticle is enough to handle the tasks
        vertx.deployVerticle(new FiboandFileCopyVerticle2());

        // if you want to scale the tasks being handled and handle more tasks then increase instances of the event loop verticle
        // every verticle will still have 1 single event loop thread
//        var deploymentoptions = new DeploymentOptions()
//                .setInstances(2);
//        vertx.deployVerticle("com.globalpayex.FiboandFileCopyVerticle2",deploymentoptions);

        logger.info("Fibo and File Verticle has been deployed ");
    }

}
