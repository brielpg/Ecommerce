package br.com.api.ecommerce.application.interfaces;

public interface AuthorizationProvider {
    String encode(String rawPassword);
    boolean matches(String currentPassword, String userPassword);
    boolean validateAdminUser();
}
