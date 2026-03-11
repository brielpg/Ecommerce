package br.com.api.ecommerce.core.mappers;

import br.com.api.ecommerce.core.models.User;
import br.com.api.ecommerce.core.dtos.User.UserDtoCreate;
import br.com.api.ecommerce.core.dtos.User.UserDtoList;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDtoList toDto(User entity) {
        if (entity == null) return null;

        return new UserDtoList(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getBirthDate(),
                entity.getActive(),
                entity.getCreatedAt()
        );
    }

    public User toEntity(UserDtoCreate dto) {
        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPhone(dto.phone());
        user.setBirthDate(dto.birthDate());

        return user;
    }
}
