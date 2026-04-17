package br.com.api.ecommerce.application.usecases.product;

import br.com.api.ecommerce.application.dtos.Product.ProductDtoList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@RequiredArgsConstructor
public class GetBestSellersUseCase {
    private final GetAllProductsUseCase getAllProductsUseCase;

    public List<ProductDtoList> execute(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "purchaseCount"));

        Page<ProductDtoList> products = getAllProductsUseCase.execute(pageable);

        return products.getContent();
    }
}
