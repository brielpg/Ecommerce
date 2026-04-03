package br.com.api.ecommerce.domain.exceptions;

public class AccessDeniedException extends RuntimeException{

    public AccessDeniedException(String messageKey){
        super(messageKey);
    }
}
