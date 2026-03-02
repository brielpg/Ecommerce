package br.com.api.ecommerce.services.interfaces;

import br.com.api.ecommerce.models.User;
import br.com.api.ecommerce.models.enums.EventTypes;

public interface MessagePublisher {

    void publishUserEvent(EventTypes eventType, User user);
}
