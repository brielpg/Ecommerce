package br.com.api.ecommerce.core.exceptions;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String messageKey){
        super(messageKey);
    }
}
