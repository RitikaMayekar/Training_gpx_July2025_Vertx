package com.globalpayex.dao;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.ext.mongo.MongoClient;

import java.util.List;

public class StudentDAO {

    private MongoClient mongoClient;

    private static final String COLLECTION_NAME = "students";

    public StudentDAO(Vertx vertx, JsonObject config){
        this.mongoClient = MongoClient.createShared(vertx,config);
    }

    public Future<List<JsonObject>> findAll(JsonObject query, JsonObject projection)
    {
        return this.mongoClient.findWithOptions(
                COLLECTION_NAME,
                query,
                new FindOptions().setFields(projection)
        );
    }

    public Future<JsonObject> findOne(String sid){

        var query = new JsonObject().put("_id",sid);
        return this.mongoClient.findOne(COLLECTION_NAME,query,null);
    }

    public Future<String> insert(JsonObject newStudentData) {
        return this.mongoClient.insert(COLLECTION_NAME,newStudentData);
    }

    public Future<Long> count(JsonObject query){
        return this.mongoClient.count(COLLECTION_NAME,query);
    }

}


