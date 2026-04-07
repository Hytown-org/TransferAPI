package dev.hytalemodding.impl.transfer.item;

import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.inventory.container.SimpleItemContainer;
import dev.hytalemodding.api.transfer.v1.item.ItemVariant;
import dev.hytalemodding.api.transfer.v1.storage.SlottedStorage;
import dev.hytalemodding.api.transfer.v1.storage.StorageView;
import dev.hytalemodding.api.transfer.v1.transaction.TransactionContext;
import dev.hytalemodding.api.transfer.v1.transaction.base.SnapshotParticipant;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public final class ItemContainerWrapper extends SnapshotParticipant<ItemContainer> implements SlottedStorage<ItemVariant> {
    private final ItemContainer container;

    public ItemContainerWrapper(ItemContainer container) {
        this.container = container;
    }

    @Override
    public long insert(ItemVariant resource, long maxAmount, @Nonnull TransactionContext transaction) {
        updateSnapshots(transaction);
        return ItemContainerUtil.insert(container, resource, maxAmount);
    }

    @Override
    public long extract(ItemVariant resource, long maxAmount, @Nonnull TransactionContext transaction) {
        updateSnapshots(transaction);
        return ItemContainerUtil.extract(container, resource, maxAmount);
    }

    @Override
    public int getSlotCount() {
        return container.getCapacity();
    }

    @Override
    public ItemContainerSlotWrapper getSlot(int slot) {
        return new ItemContainerSlotWrapper(container, (short) slot);
    }

    @Override
    @Nonnull
    public Iterator<StorageView<ItemVariant>> iterator() {
        var slots = container.getCapacity();
        var items = new ItemContainerSlotWrapper[slots];
        for (short i = 0; i < slots; ++i) {
            items[i] = getSlot(i);
        }
        return Arrays.stream(items).map(s -> (StorageView<ItemVariant>) s).iterator();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ItemContainerWrapper) obj;
        return Objects.equals(this.container, that.container);
    }

    @Override
    public int hashCode() {
        return Objects.hash(container);
    }

    @Override
    public String toString() {
        return "ItemContainerWrapper[" +
            "container=" + container + ']';
    }

    @Nonnull
    @Override
    protected ItemContainer createSnapshot() {
        var container = new SimpleItemContainer(this.container.getCapacity());
        for (short i = 0; i < this.container.getCapacity(); ++i) {
            container.setItemStackForSlot(i, this.container.getItemStack(i));
        }
        return container;
    }

    @Override
    protected void readSnapshot(@Nonnull ItemContainer snapshot) {
        for (short i = 0; i < snapshot.getCapacity(); ++i) {
            container.setItemStackForSlot(i, snapshot.getItemStack(i));
        }
    }
}