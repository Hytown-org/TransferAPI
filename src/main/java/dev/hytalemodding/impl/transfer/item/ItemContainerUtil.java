package dev.hytalemodding.impl.transfer.item;

import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import dev.hytalemodding.api.transfer.v1.item.ItemVariant;

public class ItemContainerUtil {
    public static long extractSlot(ItemContainer container, short slot, ItemVariant variant, long amount) {
        if (variant.isBlank()) return amount;

        assert variant.getObject() != null;
        var t = container.removeItemStackFromSlot(slot, new ItemStack(variant.getObject().getId(), (int) amount), (int) amount, false , true);
        if (!t.succeeded()) return 0;
        return amount - (t.getRemainder() != null ? t.getRemainder().getQuantity() : 0);
    }

    public static long insertSlot(ItemContainer container, short slot, ItemVariant variant, long amount) {
        if (variant.isBlank()) return amount;

        assert variant.getObject() != null;
        var t = container.addItemStackToSlot(slot, new ItemStack(variant.getObject().getId(), (int) amount), false , true);
        if (!t.succeeded()) return 0;
        return amount - (t.getRemainder() != null ? t.getRemainder().getQuantity() : 0);
    }

    public static long extract(ItemContainer container, ItemVariant variant, long amount) {
        if (variant.isBlank()) return amount;

        assert variant.getObject() != null;
        var t = container.removeItemStack(new ItemStack(variant.getObject().getId(), (int) amount), false, true);
        if (!t.succeeded()) return 0;
        return amount - (t.getRemainder() != null ? t.getRemainder().getQuantity() : 0);
    }

    public static long insert(ItemContainer container, ItemVariant variant, long amount) {
        if (variant.isBlank()) return amount;

        assert variant.getObject() != null;
        var t = container.addItemStack(new ItemStack(variant.getObject().getId(), (int) amount), false, false, true);
        if (!t.succeeded()) return 0;
        return amount - (t.getRemainder() != null ? t.getRemainder().getQuantity() : 0);
    }
}