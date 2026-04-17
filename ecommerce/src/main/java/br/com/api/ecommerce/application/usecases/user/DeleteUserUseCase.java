package br.com.api.ecommerce.application.usecases.user;

import br.com.api.ecommerce.application.interfaces.MessagePublisher;
import br.com.api.ecommerce.application.repositories.UserRepository;
import br.com.api.ecommerce.domain.enums.EventTypes;
import br.com.api.ecommerce.domain.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class DeleteUserUseCase {
    private final UserRepository repository;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final MessagePublisher messagePublisher;

    public void execute(UUID id) {
        User user = getUserByIdUseCase.execute(id);
        user.setActive(false);
        messagePublisher.publishUserEvent(EventTypes.USER_DEACTIVATED, user);
        repository.save(user);
        log.info("Usuário desativado com sucesso: {}", id);
    }
}
