package br.com.api.ecommerce.services.mappers;

import br.com.api.ecommerce.models.User;
import br.com.api.ecommerce.models.dtos.User.UserDtoCreate;
import br.com.api.ecommerce.models.dtos.User.UserDtoList;
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
                entity.getTimestamp()
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
