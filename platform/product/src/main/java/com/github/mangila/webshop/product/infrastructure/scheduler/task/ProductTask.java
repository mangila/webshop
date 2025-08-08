package com.github.mangila.webshop.product.infrastructure.scheduler.task;

public interface ProductTask {

    void execute();

    ProductTaskKey key();

}
