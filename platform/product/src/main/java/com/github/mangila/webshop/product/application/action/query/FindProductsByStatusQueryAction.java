package com.github.mangila.webshop.product.application.action.query;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductQueryRepository;
import com.github.mangila.webshop.product.domain.cqrs.FindProductByStatusQuery;
import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.QueryAction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindProductsByStatusQueryAction implements QueryAction<FindProductByStatusQuery, List<Product>> {

    private final ProductQueryRepository repository;

    public FindProductsByStatusQueryAction(ProductQueryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Product> query(FindProductByStatusQuery query) {
        Ensure.notNull(query, FindProductByStatusQuery.class);
        return repository.findByStatus(query);
    }
}
