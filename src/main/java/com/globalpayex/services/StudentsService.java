package com.globalpayex.services;

import com.globalpayex.dao.StudentDAO;
import com.globalpayex.exceptions.StudentAlreadyExistsException;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.stream.Collectors;

public class StudentsService {

    private StudentDAO studentDAO;

    public StudentsService(StudentDAO studentDAO){
        this.studentDAO = studentDAO;
    }

    public Future<JsonObject> validateStudent(JsonObject data){
        var validateFuture =  this.studentDAO.findAll(
                new JsonObject()
                        .put("username",data.getString("username"))
                        .put("password",data.getString("password"))
                ,
                new JsonObject().put("_id",1).put("username",1)
        );

        var transformedFuture = validateFuture.map(
            studentJsonObjects->{
                    if(studentJsonObjects.isEmpty()){
                        return null;
                    }
                    return studentJsonObjects.get(0);
                });
        return transformedFuture;
    }

    public Future<List<JsonObject>> getAllStudents(JsonObject queryParams){

        var orConditions = new JsonArray();

        if(queryParams.containsKey("gender")){
            orConditions.add(new JsonObject().put("gender",queryParams.getString("gender")));
        }
        if(queryParams.containsKey("country")){
            orConditions.add(new JsonObject().put("address.country",queryParams.getString("country")));
        }

        var query = new JsonObject();
        if(!orConditions.isEmpty()){
                query.put("$or",orConditions);
            }
        
    Future<List<JsonObject>> studentFuture;
    //    var studentFuture = this.studentDAO.findAll(query, new JsonObject());

        if(queryParams.isEmpty()){
            studentFuture = this.studentDAO.findAll(new JsonObject(),
                    new JsonObject().put("_id",1).put("username",1));
        }
        else{
            studentFuture = this.studentDAO.findAll(queryParams,
                    new JsonObject().put("_id",1).put("username",1));
        }

        var transformedFuture = studentFuture.map(
            studentJsonObjects->{
            return studentJsonObjects
                    .stream()
                    .map(studentJsonObj->{
                        var username = studentJsonObj.getString("username");
                        studentJsonObj.put("username",username.toUpperCase());
                        return studentJsonObj;
                    })
                    .collect(Collectors.toList());
        });

        return transformedFuture;
    }

    public Future<JsonObject> getStudentsById(String sid){

        var getStudentByIdFuture = this.studentDAO.findOne(sid);

        var transformedFuture = getStudentByIdFuture
                .map(studentJsonObject -> {
                    if(studentJsonObject != null){
                        studentJsonObject.put("username",studentJsonObject.getString("username").toUpperCase());

                    }
                    return studentJsonObject;
                });

       return transformedFuture;
    }

    public Future<JsonObject> addNewStudent(JsonObject newStudentData) {

        var isexisting = this.studentDAO.count(new JsonObject().put("username", newStudentData.getString("username")));

        var finalFuture = isexisting.compose(stdcount -> {
            if (stdcount == 0) {
                var addedStudentFuture = this.studentDAO.insert(newStudentData);

                var transformedObject = addedStudentFuture
                        .map(newStudentId -> {
                            newStudentData.put("_id", newStudentId);
                            return newStudentData;
                        });
                return transformedObject;
            }
            return Future.failedFuture(
                    new StudentAlreadyExistsException(
                            String.format("Student with %s Already Exists", newStudentData.getString("username"))));


        });
        return finalFuture;
    }

}
