package br.com.api.ecommerce.core.exceptions;

public class ConflictException extends RuntimeException{

    public ConflictException(String messageKey){
        super(messageKey);
    }
}
