package br.com.api.ecommerce.domain.exceptions;

public class ConflictException extends RuntimeException{

    public ConflictException(String messageKey){
        super(messageKey);
    }
}
