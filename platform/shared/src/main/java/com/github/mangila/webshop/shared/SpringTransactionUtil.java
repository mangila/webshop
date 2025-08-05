package com.github.mangila.webshop.shared;

import com.github.mangila.webshop.shared.exception.ApplicationException;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.function.Supplier;

public final class SpringTransactionUtil {
    private SpringTransactionUtil() {
        throw new ApplicationException("Utility class");
    }

    public static void registerSynchronization(Supplier<TransactionSynchronization> supplier) {
        TransactionSynchronizationManager.registerSynchronization(supplier.get());
    }
}
