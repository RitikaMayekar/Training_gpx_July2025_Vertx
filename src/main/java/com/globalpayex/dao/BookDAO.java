package com.globalpayex.dao;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.ext.mongo.MongoClient;

import java.util.List;

public class BookDAO {

    private MongoClient mongoClient;

    private static final String COLLECTION_NAME = "books";

    public BookDAO(Vertx vertx, JsonObject config){
        this.mongoClient = MongoClient.createShared(vertx,config);
    }


//    public Future<List<JsonObject>> getBooks(JsonObject queryParams, JsonObject projection) {
//
//        var sort = new JsonObject().put("author",1);
//
//        return this.mongoClient.findWithOptions(
//                COLLECTION_NAME,
//                queryParams,
//                new FindOptions().setFields(projection),
//                new FindOptions().setSort()
//        );
//    }
}
