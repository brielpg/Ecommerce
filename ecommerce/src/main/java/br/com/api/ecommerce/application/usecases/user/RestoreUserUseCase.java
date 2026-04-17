package br.com.api.ecommerce.application.usecases.user;

import br.com.api.ecommerce.application.interfaces.MessagePublisher;
import br.com.api.ecommerce.application.repositories.UserRepository;
import br.com.api.ecommerce.domain.enums.EventTypes;
import br.com.api.ecommerce.domain.models.User;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class RestoreUserUseCase {
    private final UserRepository repository;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final MessagePublisher messagePublisher;

    public void execute(UUID id) {
        User user = getUserByIdUseCase.execute(id);
        user.setActive(true);
        messagePublisher.publishUserEvent(EventTypes.USER_REACTIVATED, user);
        repository.save(user);
    }
}
