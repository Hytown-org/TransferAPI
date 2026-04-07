package dev.hytalemodding.impl.transfer.money;

import dev.hytalemodding.api.transfer.v1.storage.TransferVariant;

public abstract class Money implements TransferVariant<Object> {
    private Money() {}

    public static final Money INSTANCE = new Money() {
        @Override
        public boolean isBlank() {
            return false;
        }

        @Override
        public Money getObject() {
            return this;
        }
    };

    public static final Money NONE = new Money() {
        @Override
        public boolean isBlank() {
            return true;
        }

        @Override
        public Money getObject() {
            return this;
        }
    };
}