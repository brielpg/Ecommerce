package br.com.api.ecommerce.application.usecases.user;

import br.com.api.ecommerce.application.dtos.User.UserDtoCreate;
import br.com.api.ecommerce.application.dtos.User.UserDtoList;
import br.com.api.ecommerce.application.interfaces.AuthorizationProvider;
import br.com.api.ecommerce.application.interfaces.MessagePublisher;
import br.com.api.ecommerce.application.mappers.AddressMapper;
import br.com.api.ecommerce.application.mappers.UserMapper;
import br.com.api.ecommerce.application.repositories.UserRepository;
import br.com.api.ecommerce.application.usecases.cart.CreateCartUseCase;
import br.com.api.ecommerce.domain.enums.EventTypes;
import br.com.api.ecommerce.domain.exceptions.ConflictException;
import br.com.api.ecommerce.domain.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class CreateUserUseCase {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final AddressMapper addressMapper;
    private final AuthorizationProvider authorization;
    private final CreateCartUseCase createCartUseCase;
    private final MessagePublisher messagePublisher;

    public UserDtoList execute(UserDtoCreate dto) {
        if (repository.existsByEmail(dto.email()))
            throw new ConflictException("exception.user.email.already.registered");

        User user = mapper.toEntity(dto);
        user.setAddresses(addressMapper.toEntityList(dto.addresses(), user));
        user.setPassword(authorization.encode(dto.password()));
        user.setCart(createCartUseCase.execute(user));

        messagePublisher.publishUserEvent(EventTypes.USER_WELCOME, user);

        repository.save(user);
        log.info("Usuário criado com sucesso. ID: {}", user.getId());
        return mapper.toDto(user);
    }
}
