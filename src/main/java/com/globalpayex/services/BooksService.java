package com.globalpayex.services;

import com.globalpayex.dao.BookDAO;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.List;

public class BooksService {

    private BookDAO bookDAO;

    public BooksService(BookDAO bookDAO){
        this.bookDAO = bookDAO;
    }

//    public Future<List<JsonObject>> getAllBookDetails(JsonObject queryparams){
//        Future <List<JsonObject>> booksFuture;
//
//        var projection = new JsonObject().put("authors",0);
//
//        if(queryparams.containsKey("price")){
//            booksFuture = this.bookDAO.getBooks(queryparams,projection);
//        }
//        else {
//           booksFuture = this.bookDAO.getBooks(new JsonObject(),new JsonObject());
//        }
////
//
//
//
//    }

}
