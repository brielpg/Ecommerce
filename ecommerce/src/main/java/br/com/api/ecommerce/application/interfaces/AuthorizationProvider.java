package br.com.api.ecommerce.application.interfaces;

public interface AuthorizationProvider {
    String encodePassword(String rawPassword);
    boolean matches(String currentPassword, String userPassword);
    boolean validateAdminUser();
}
