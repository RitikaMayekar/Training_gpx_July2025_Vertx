package com.globalpayex;

import com.globalpayex.utils.Series;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import org.jgroups.util.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class FiboandFileCopyVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(FiboandFileCopyVerticle.class);

    @Override
    public void start() throws Exception {

        var list = Arrays.asList(2, 3, 4, 5);

        var filesystem = vertx.fileSystem();

        var srcpath = "src/main/java/com/globalpayex/MultiOperationsVerticle.java";
        var destpath = "src/main/java/com/globalpayex/constantfiles/";


        list.forEach((num) -> {
            //another way to copy content
//            filesystem.copy(srcpath,destpath+num+".txt",(wr)->{
//                if(wr.succeeded()){
//                    logger.info("copied to new file");
//                }
//                else{
//                    logger.error(wr.cause().getMessage());
//                }
//            });

            filesystem.readFile(srcpath,(ar)->{
                if(ar.succeeded()){
                    var content = ar.result();
                    filesystem.writeFile(destpath+num+".txt",content,(wr)->{
                        logger.info("Copied content to {} file",num+".txt");
                    });
                }
            });

            var series = Series.fiboGenerator(num);
            logger.info("Fibo series for {} is {} ",num,series);
        });
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(new FiboandFileCopyVerticle());

        logger.info("Fibo and File Verticle has been deployed ");
    }

}
