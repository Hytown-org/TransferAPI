package dev.hytalemodding.api.transfer.v1.item;

import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import dev.hytalemodding.api.transfer.v1.storage.TransferVariant;

import javax.annotation.Nullable;

public record ItemVariant(@Nullable Item item) implements TransferVariant<Item> {
    @Override
    public boolean isBlank() {
        return item == null;
    }

    @Override
    public Item getObject() {
        return item;
    }

    public ItemStack toStack(int amount) {
        if (isBlank()) return ItemStack.EMPTY;
        return new ItemStack(item.getId(), amount);
    }
}