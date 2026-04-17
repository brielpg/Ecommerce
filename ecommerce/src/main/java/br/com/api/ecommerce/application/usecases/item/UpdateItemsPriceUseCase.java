package br.com.api.ecommerce.application.usecases.item;

import br.com.api.ecommerce.application.repositories.ItemRepository;
import br.com.api.ecommerce.domain.models.Item;
import br.com.api.ecommerce.domain.models.Product;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class UpdateItemsPriceUseCase {
    private final ItemRepository repository;

    public void execute(Product product) {
        List<Item> items = repository.findAllByProductId(product.getId());

        for (Item item : items) {
            item.updateSubTotal();
            item.getCart().recalculateTotal();
        }

        repository.saveAll(items);
    }
}
