package br.com.api.ecommerce.domain.exceptions;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String messageKey){
        super(messageKey);
    }
}
