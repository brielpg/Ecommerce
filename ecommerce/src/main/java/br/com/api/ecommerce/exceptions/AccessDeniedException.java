package br.com.api.ecommerce.exceptions;

public class AccessDeniedException extends RuntimeException{

    public AccessDeniedException(String messageKey){
        super(messageKey);
    }
}
