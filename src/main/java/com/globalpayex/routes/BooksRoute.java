package com.globalpayex.routes;

import com.globalpayex.MyHTTPServerVerticle;
import com.globalpayex.dao.BookDAO;
import com.globalpayex.dao.StudentDAO;
import com.globalpayex.domain.Book;
import com.globalpayex.services.BooksService;
import com.globalpayex.services.StudentsService;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BooksRoute {

    private static final Logger logger = LoggerFactory.getLogger(BooksRoute.class);

    private static BooksService booksService;

    public static Router init(Router router,Vertx vertx, JsonObject config){

        booksService = new BooksService(new BookDAO(vertx,config));


        router.get("/books")
                .handler(BooksRoute::displayBooksDetails);

//        router.get("/books/:id")
//                .handler(BooksRoute::displaySpecificBooksDetails);
//
//        router.post("/books")
//                .handler(BooksRoute::addNewBook);

        return router;

    }

    private static void displayBooksDetails(RoutingContext routingContext) {

        var queryParamObj = new JsonObject();
        var queryprice = routingContext.queryParam("price");

        if(!queryprice.isEmpty()){
            queryParamObj.put("book_details.price",new JsonObject().put("$gte",queryprice.get(0)));

        }

        var displayBooksFuture = booksService.getAllBookDetails(queryParamObj);

        displayBooksFuture.onSuccess(
                booksObj -> {
                    routingContext
                            .response()
                            .putHeader("content-type","application/json")
                            .end(new JsonArray(booksObj).encode());
                }
        );

        displayBooksFuture.onFailure(
                err -> {
                    routingContext.response()
                            .setStatusCode(400)
                            .end(err.getMessage());
                }
        );


    }

    //    private static final ArrayList<Book> books = new ArrayList<>(
//            Arrays.asList(
//                new Book(1, "Vertx Basics", 400, 600),
//                new Book(2, "Vertx Basics 1 ", 400, 600)
//            )
//    );

//    public static long lastUsedBookId = 2;


//    private static void addNewBook(RoutingContext routingContext) {
//        var newBook = routingContext.body().asJsonObject();
//
//        //.asPojo ->  to convert the JsonObject into POJO -> provided by library : jackson databind
//        // POJO is any java bean / class having private attributes getter setter functions, no arg constructor
//
//        //JsonObject.mapFrom -> used for converting POJO into JsonObject
//        // here book which is a new Book object is a pojo
//        // so to convert it to Json object .mapfrom is used
//
//        var book = routingContext.body().asPojo(Book.class);
//        book.setId(++lastUsedBookId);
//        books.add(book);
//
//        routingContext
//                .response()
//                .setStatusCode(201)
//                .end(JsonObject.mapFrom(book).encode());
//
//    }


    //    private static void displaySpecificBooksDetails(RoutingContext routingContext) {
//        int id;
//        try {
//            id = Integer.parseInt(routingContext.pathParam("id"));
//        }
//        catch (NumberFormatException e){
//            routingContext
//                    .response()
//                    .setStatusCode(400)
//                    .end("Bad Request");
//            return;
//        }
//        var specificBook = books.stream()
//                .filter(book -> book.getId()==id)
//                .collect(Collectors.toList());
//
//        if(specificBook.isEmpty()){
//            routingContext.response()
//                    .setStatusCode(404)
//                    .end("Book not found");
//        }else{
//            routingContext
//                    .response()
//                        .putHeader("content-type", "application/json")
//                        .end(JsonObject.mapFrom(specificBook.get(0)).encode());
//            }
//
//    }


}
