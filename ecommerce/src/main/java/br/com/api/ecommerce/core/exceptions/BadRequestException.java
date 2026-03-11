package br.com.api.ecommerce.core.exceptions;

public class BadRequestException extends RuntimeException{

    public BadRequestException(String messageKey){
        super(messageKey);
    }
}
