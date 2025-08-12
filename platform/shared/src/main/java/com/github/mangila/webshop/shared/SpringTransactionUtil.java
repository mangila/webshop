package com.github.mangila.webshop.shared;

import com.github.mangila.webshop.shared.exception.ApplicationException;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.function.Supplier;

/**
 * Utility class providing methods for working with Spring transaction synchronizations.
 * This class serves as a simple facade for interacting with the Spring `TransactionSynchronizationManager`.
 */
public final class SpringTransactionUtil {
    private SpringTransactionUtil() {
        throw new ApplicationException("Utility class");
    }

    /**
     * Register a synchronization with the current transaction.
     * Convenience method for {@link TransactionSynchronizationManager#registerSynchronization(TransactionSynchronization)}.
     * Mostly exists for convenience, as the {@link Supplier} can be passed directly.
     */
    public static void registerSynchronization(Supplier<TransactionSynchronization> supplier) {
        TransactionSynchronizationManager.registerSynchronization(supplier.get());
    }
}
