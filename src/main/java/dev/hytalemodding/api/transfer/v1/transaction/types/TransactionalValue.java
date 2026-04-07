package dev.hytalemodding.api.transfer.v1.transaction.types;

import dev.hytalemodding.api.transfer.v1.transaction.TransactionContext;
import dev.hytalemodding.api.transfer.v1.transaction.base.SnapshotParticipant;

import javax.annotation.Nonnull;

/**
 * Helper class that implements a simple transactional value.
 * Stores a value of the given type, and provides the {@link #getValue()} and {@link #assignValue(T, TransactionContext)} methods
 * for accessing and updating the stored value.
 * @param <T> The type of the stored object.
 */
public class TransactionalValue<T> extends SnapshotParticipant<T> {
    /** The current value stored by this transactional value. */
    protected @Nonnull T value;

    /**
     * Create a new transactional value with the provided starting value.
     *
     * @param startingValue The initial stored value.
     */
    public TransactionalValue(@Nonnull T startingValue) {
        this.value = startingValue;
    }

    /**
     * Fetches the current stored value.
     * @return The current value.
     */
    public @Nonnull T getValue() {
        return this.value;
    }

    /**
     * Stores a new value using the given transaction.
     * @param newValue The new value to store.
     * @param transaction Transaction to use.
     */
    public void assignValue(@Nonnull T newValue, @Nonnull TransactionContext transaction) {
        updateSnapshots(transaction);
        value = newValue;
    }

    @Override
    protected @Nonnull T createSnapshot() {
        // Since we are using reference types, cloning isn't necessary
        return value;
    }

    @Override
    protected void readSnapshot(@Nonnull T snapshot) {
        value = snapshot;
    }
}