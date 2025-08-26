package br.com.api.ecommerce.exceptions;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String messageKey){
        super(messageKey);
    }
}
