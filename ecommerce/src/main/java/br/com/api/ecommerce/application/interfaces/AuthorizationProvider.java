package br.com.api.ecommerce.application.interfaces;

import br.com.api.ecommerce.application.dtos.Auth.AuthDtoLogin;
import br.com.api.ecommerce.application.dtos.Auth.AuthReturnToken;

public interface AuthorizationProvider {
    String encode(String rawPassword);
    boolean matches(String currentPassword, String userPassword);
    boolean validateAdminUser();
    AuthReturnToken login(AuthDtoLogin dto);
}
