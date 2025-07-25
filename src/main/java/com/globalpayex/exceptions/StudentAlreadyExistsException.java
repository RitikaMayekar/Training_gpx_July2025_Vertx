package com.globalpayex.exceptions;

public class StudentAlreadyExistsException extends RuntimeException{

    public StudentAlreadyExistsException(String msg){
        super(msg);
    }
}
