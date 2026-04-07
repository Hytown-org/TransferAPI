package dev.hytalemodding.impl.transfer.item;

import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import dev.hytalemodding.api.transfer.v1.item.ItemVariant;
import dev.hytalemodding.api.transfer.v1.item.SingleItemStackStorage;

import java.util.Objects;

public final class ItemContainerSlotWrapper extends SingleItemStackStorage {
    private final ItemContainer container;
    private final short slot;

    public ItemContainerSlotWrapper(
        ItemContainer container,
        short slot
    ) {
        this.container = container;
        this.slot = slot;
    }

    @Override
    protected ItemStack getStack() {
        return container.getItemStack(slot);
    }

    @Override
    protected void setStack(ItemStack stack) {
        container.setItemStackForSlot(slot, stack);
    }

    @Override
    protected boolean canInsert(ItemVariant itemVariant, int desired) {
        if (itemVariant.isBlank())
            return true; // can always insert nothing

        return container.canAddItemStackToSlot(slot, itemVariant.toStack(desired), false, true);
    }

    @Override
    protected boolean canExtract(ItemVariant itemVariant, int desired) {
        if (itemVariant.isBlank())
            return true; // can always extract nothing
        assert itemVariant.getObject() != null;

        // Hytale doesnt have a canRemoveItemStackFromSlot >:(
        var currentStack = container.getItemStack(slot);
        if (currentStack == null)
            return false; // nothing in slot, can't extract

        return currentStack.isStackableWith(itemVariant.toStack(desired));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ItemContainerSlotWrapper) obj;
        return Objects.equals(this.container, that.container) &&
            this.slot == that.slot;
    }

    @Override
    public int hashCode() {
        return Objects.hash(container, slot);
    }

    @Override
    public String toString() {
        return "ItemSlotStorage[" +
            "container=" + container + ", " +
            "slot=" + slot + ']';
    }
}