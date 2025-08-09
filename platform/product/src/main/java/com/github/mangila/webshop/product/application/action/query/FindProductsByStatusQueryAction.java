package com.github.mangila.webshop.product.application.action.query;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductQueryRepository;
import com.github.mangila.webshop.product.domain.cqrs.FindProductByStatusQuery;
import com.github.mangila.webshop.shared.QueryAction;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class FindProductsByStatusQueryAction implements QueryAction<FindProductByStatusQuery, List<Product>> {

    private final ProductQueryRepository repository;

    public FindProductsByStatusQueryAction(ProductQueryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Product> execute(@NotNull FindProductByStatusQuery query) {
        return repository.findByStatus(query);
    }
}
