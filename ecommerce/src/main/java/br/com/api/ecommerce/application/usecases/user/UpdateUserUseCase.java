package br.com.api.ecommerce.application.usecases.user;

import br.com.api.ecommerce.application.dtos.User.UserDtoList;
import br.com.api.ecommerce.application.dtos.User.UserDtoUpdate;
import br.com.api.ecommerce.application.interfaces.AuthorizationProvider;
import br.com.api.ecommerce.application.mappers.UserMapper;
import br.com.api.ecommerce.application.repositories.UserRepository;
import br.com.api.ecommerce.application.usecases.address.CreateAddressesUseCase;
import br.com.api.ecommerce.domain.exceptions.BadRequestException;
import br.com.api.ecommerce.domain.exceptions.ConflictException;
import br.com.api.ecommerce.domain.models.Address;
import br.com.api.ecommerce.domain.models.User;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UpdateUserUseCase {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final AuthorizationProvider authorization;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final CreateAddressesUseCase createAddressesUseCase;

    public UserDtoList execute(UserDtoUpdate dto) {
        User user = getUserByIdUseCase.execute(dto.id());

        if (!authorization.matches(dto.currentPassword(), user.getPassword()))
            throw new BadRequestException("exception.user.current.password.invalid");

        if (dto.email() != null && !dto.email().equals(user.getEmail())) {
            if (repository.existsByEmail(dto.email()))
                throw new ConflictException("exception.user.email.already.registered");
            user.setEmail(dto.email());
        }

        if (dto.name() != null) user.setName(dto.name());
        if (dto.phone() != null) user.setPhone(dto.phone());
        if (dto.birthDate() != null) user.setBirthDate(dto.birthDate());

        if (dto.newPassword() != null && !dto.newPassword().isBlank()) {
            user.setPassword(authorization.encodePassword(dto.newPassword()));
        }

        if (dto.addresses() != null) {
            user.getAddresses().clear();
            List<Address> newAddresses = createAddressesUseCase.execute(dto.addresses(), user);
            user.getAddresses().addAll(newAddresses);
        }

        repository.save(user);
        return mapper.toDto(user);
    }
}
