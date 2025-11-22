package com.stockia.stockia.exceptions;

public class MethodArgumentNotValidException extends RuntimeException {

   public MethodArgumentNotValidException( String message){
     super(message);
   }
}
