package br.com.api.ecommerce.application.interfaces;

import br.com.api.ecommerce.domain.models.User;
import br.com.api.ecommerce.domain.enums.EventTypes;

public interface MessagePublisher {
    void publishUserEvent(EventTypes eventType, User user);
}
