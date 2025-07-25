package com.globalpayex.routes;

import com.globalpayex.dao.StudentDAO;
import com.globalpayex.exceptions.StudentAlreadyExistsException;
import com.globalpayex.services.StudentsService;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StudentsRoute {

    private static final Logger logger = LoggerFactory.getLogger(BooksRoute.class);

    private static StudentsService studentsService;

    public static Router init(Router router, Vertx vertx, JsonObject config){

        studentsService = new StudentsService(new StudentDAO(vertx,config));


        router.get("/students")
                .handler(StudentsRoute::getAllStudents);

        router.get("/students/:id")
                .handler(StudentsRoute::getSpecificStudent);

        router.post("/addStudent")
                .handler((routingContext -> addNewStudent(vertx,routingContext)));

        router.post("/validateStudent")
                .handler(StudentsRoute::validateStudent);

        return router;
    }

    private static void validateStudent(RoutingContext routingContext) {
        var studentData = routingContext.body().asJsonObject();
        var validateFuture = studentsService.validateStudent(studentData);

        validateFuture.onSuccess(
                studentJsonObject -> {
                    routingContext.response()
                            .putHeader("Content-Type","application/json")
                            .setStatusCode(200)
                            .end(studentJsonObject.encode());
                }
        );

        validateFuture.onFailure(err -> {
                    routingContext.response()
                            .setStatusCode(500)
                            .end("Something went wrong");
                }
        );

    }

    private static void addNewStudent(Vertx vertx,RoutingContext routingContext) {
        var newStudentData = routingContext.body().asJsonObject();

        var addingNewStudentFuture = studentsService.addNewStudent(newStudentData);

        addingNewStudentFuture.onSuccess( newStudent -> {

                    vertx.eventBus()
                            .publish("new.student", new JsonObject().put("_id", newStudentData.getString("_id")));


                    routingContext.response()
                            .putHeader("Content-Type","application/json")
                            .setStatusCode(201)
                            .end(newStudentData.encode());
                });

        addingNewStudentFuture.onFailure(err -> {
            if(err instanceof StudentAlreadyExistsException){
                routingContext.response()
                        .setStatusCode(400)
                        .end(err.getMessage());
            }else {
                routingContext.response()
                        .setStatusCode(500)
                        .end("Something went wrong");
            }
        });
    }


    private static void getAllStudents(RoutingContext routingContext) {

        JsonObject queryParams = new JsonObject();

        var gender = routingContext.queryParam("gender");
        var country = routingContext.queryParam("country");

        if(!gender.isEmpty()){
            queryParams.put("gender",gender.get(0));
        }
        if(!country.isEmpty()){
            queryParams.put("country",country.get(0));
        }

        var findAllStudentsFuture = studentsService.getAllStudents(queryParams);

        findAllStudentsFuture.onSuccess(studentJsonObject ->
                routingContext.response()
                        .putHeader("Content-Type","applicaation/json")
                        .end(new JsonArray(studentJsonObject).encode())
        );

        findAllStudentsFuture.onFailure(err -> {
                    routingContext.response()
                            .setStatusCode(500)
                            .end("Something went wrong");
                }
                );


    }

    private static void getSpecificStudent(RoutingContext routingContext){
        var getStudentById = studentsService.getStudentsById(routingContext.pathParam("id"));

        getStudentById.onSuccess(studentJsonObject->{
            if(studentJsonObject==null){
                routingContext.response()
                        .setStatusCode(404)
                        .end("Student not found");
            }
            else{
                routingContext.response()
                        .putHeader("Content-Type","application/json")
                        .end(studentJsonObject.encode());
            }
        });



    }

}
