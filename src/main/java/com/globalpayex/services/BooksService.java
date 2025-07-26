package com.globalpayex.services;

import com.globalpayex.dao.BookDAO;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

public class BooksService {

    private BookDAO bookDAO;

    public BooksService(BookDAO bookDAO){
        this.bookDAO = bookDAO;
    }

    public Future<List<JsonObject>> getAllBookDetails(JsonObject queryparams){

        var arrayOfGTEcondition = new JsonArray();

        var projection = new JsonObject().put("authors",0);

        if(queryparams.containsKey("price")){
            arrayOfGTEcondition.add(new JsonObject().put("book_details.price",queryparams.getString("price")));
        }
        var query = new JsonObject();

        var queryValue = new JsonObject().put("$gte",arrayOfGTEcondition.getValue(0));
        if(!arrayOfGTEcondition.isEmpty()){
            query.put("book_details.price",queryValue);
        }

        var bookFuture = this.bookDAO.getBooks(
                query,
                projection,
                new JsonObject().put("title",1)
        );

        return bookFuture;
    }

}
