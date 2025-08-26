package br.com.api.ecommerce.exceptions;

public class ConflictException extends RuntimeException{

    public ConflictException(String messageKey){
        super(messageKey);
    }
}
