package com.globalpayex;

import com.globalpayex.dao.StudentDAO;
import com.globalpayex.routes.MyAppRouter;
import com.globalpayex.services.StudentsService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

public class StatisticsVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(MyAppRouter.class);


    @Override
    public void start() throws Exception {

        var studentService = new StudentsService(new StudentDAO(vertx,config()));


        vertx.eventBus().<JsonObject>consumer("new.student",message->{
            var newStudent = message.body();
            logger.info("new student {}",newStudent.getString("_id"));

            var studentsFuture = studentService.getAllStudents(new JsonObject());
            studentsFuture.onSuccess(studentSuccessObj->{
                var groupedData = studentSuccessObj.stream()
                        .collect(Collectors.groupingBy(
                                studentJson->studentJson.getString("gender"),
                                Collectors.counting()
                        ));
                logger.info("Student stats as of now {}",groupedData);

            });
            studentsFuture.onFailure(err->{
                logger.error("error occured : {}",err.getMessage());
            });

        });


    }

}
