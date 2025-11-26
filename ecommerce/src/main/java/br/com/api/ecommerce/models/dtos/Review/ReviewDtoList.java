package br.com.api.ecommerce.models.dtos.Review;

import br.com.api.ecommerce.models.dtos.Product.ProductDtoList;
import br.com.api.ecommerce.models.dtos.User.UserDtoList;

import java.time.LocalDate;
import java.util.UUID;

public record ReviewDtoList(
        UserDtoList user,
        ProductDtoList product,
        Double rating,
        String review,
        LocalDate timestamp
) {
}
