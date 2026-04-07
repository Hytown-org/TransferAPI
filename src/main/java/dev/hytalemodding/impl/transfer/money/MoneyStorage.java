package dev.hytalemodding.impl.transfer.money;

import dev.hytalemodding.api.transfer.v1.storage.base.FixedVariantStorage;

public class MoneyStorage extends FixedVariantStorage<Money> {
    public static final long MAX_BALANCE = Long.MAX_VALUE / 2;

    public MoneyStorage(long amount) {
        super.amount = amount;
    }

    @Override
    protected Money getBlankVariant() {
        return Money.NONE;
    }

    @Override
    protected Money getAllowedVariant() {
        return Money.INSTANCE;
    }

    @Override
    public Money getResource() {
        return getAllowedVariant();
    }

    @Override
    public long getCapacity() {
        return MAX_BALANCE;
    }

    @Override
    protected long getCapacity(Money variant) {
        return getCapacity();
    }
}