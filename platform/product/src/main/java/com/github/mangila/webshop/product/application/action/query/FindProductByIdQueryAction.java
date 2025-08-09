package com.github.mangila.webshop.product.application.action.query;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductQueryRepository;
import com.github.mangila.webshop.product.domain.cqrs.FindProductByIdQuery;
import com.github.mangila.webshop.shared.JavaOptionalUtil;
import com.github.mangila.webshop.shared.QueryAction;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class FindProductByIdQueryAction implements QueryAction<FindProductByIdQuery, Product> {

    private final ProductQueryRepository repository;

    public FindProductByIdQueryAction(ProductQueryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product execute(@NotNull FindProductByIdQuery query) {
        return repository.findById()
                .andThen(optionalProduct -> JavaOptionalUtil.orElseThrowResourceNotFound(optionalProduct, Product.class, query.id()))
                .apply(query);
    }
}
