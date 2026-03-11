package br.com.api.ecommerce.core.exceptions;

public class AccessDeniedException extends RuntimeException{

    public AccessDeniedException(String messageKey){
        super(messageKey);
    }
}
