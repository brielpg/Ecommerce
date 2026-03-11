package br.com.api.ecommerce.core.ports;

import br.com.api.ecommerce.core.models.User;
import br.com.api.ecommerce.core.models.enums.EventTypes;

public interface MessagePublisher {

    void publishUserEvent(EventTypes eventType, User user);
}
