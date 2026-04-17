package br.com.api.ecommerce.application.usecases.user;

import br.com.api.ecommerce.application.dtos.User.UserDtoList;
import br.com.api.ecommerce.application.mappers.UserMapper;
import br.com.api.ecommerce.application.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class GetAllUsersUseCase {
    private final UserRepository repository;
    private final UserMapper mapper;

    public Page<UserDtoList> execute(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDto);
    }
}
