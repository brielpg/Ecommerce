package br.com.api.ecommerce.exceptions;

public class BadRequestException extends RuntimeException{

    public BadRequestException(String messageKey){
        super(messageKey);
    }
}
