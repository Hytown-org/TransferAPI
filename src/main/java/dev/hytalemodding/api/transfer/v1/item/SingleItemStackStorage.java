package dev.hytalemodding.api.transfer.v1.item;

import com.hypixel.hytale.server.core.inventory.ItemStack;
import dev.hytalemodding.api.transfer.v1.storage.StoragePreconditions;
import dev.hytalemodding.api.transfer.v1.storage.base.SingleSlotStorage;
import dev.hytalemodding.api.transfer.v1.transaction.TransactionContext;
import dev.hytalemodding.api.transfer.v1.transaction.base.SnapshotParticipant;

import javax.annotation.Nonnull;

public abstract class SingleItemStackStorage extends SnapshotParticipant<ItemStack> implements SingleSlotStorage<ItemVariant> {
	/**
	 * Return the stack of this storage. It will be modified directly sometimes to avoid needless copies.
	 * However, any mutation of the stack will directly be followed by a call to {@link #setStack}.
	 * This means that either returning the backing stack directly or a copy is safe.
	 *
	 * @return The current stack.
	 */
	protected abstract ItemStack getStack();

	/**
	 * Set the stack of this storage.
	 */
	protected abstract void setStack(ItemStack stack);

	/**
	 * Return {@code true} if the passed non-blank item variant can be inserted, {@code false} otherwise.
	 */
	protected boolean canInsert(ItemVariant itemVariant, int desired) {
		return true;
	}

	/**
	 * Return {@code true} if the passed non-blank item variant can be extracted, {@code false} otherwise.
	 */
	protected boolean canExtract(ItemVariant itemVariant, int amount) {
		return true;
	}

	/**
	 * Return the maximum capacity of this storage for the passed item variant.
	 * If the passed item variant is blank, an estimate should be returned.
	 *
	 * <p>If the capacity should be limited by the max count of the item, this function must take it into account.
	 * For example, a storage with a maximum count of 4, or less for items that have a smaller max count,
	 * should override this to return {@code Math.min(super.getCapacity(itemVariant), 4);}.
	 *
	 * @return The maximum capacity of this storage for the passed item variant.
	 */
	protected int getCapacity(ItemVariant itemVariant) {
		return itemVariant.getObject() != null ? itemVariant.getObject().getMaxStack() : 100;
	}

	@Override
	public boolean isResourceBlank() {
		return getStack().isEmpty();
	}

	@Override
	public ItemVariant getResource() {
		return new ItemVariant(getStack().getItem());
	}

	@Override
	public long getAmount() {
		return getStack().getQuantity();
	}

	@Override
	public long getCapacity() {
		return getCapacity(getResource());
	}

	@Override
	public long insert(ItemVariant insertedVariant, long maxAmount, @Nonnull TransactionContext transaction) {
		StoragePreconditions.notBlankNotNegative(insertedVariant, maxAmount);

		ItemStack currentStack = getStack();

		if ((currentStack.getItem().equals(insertedVariant.getObject()) || currentStack.isEmpty()) && canInsert(insertedVariant, (int)maxAmount)) {
			int insertedAmount = (int) Math.min(maxAmount, getCapacity(insertedVariant) - currentStack.getQuantity());

			if (insertedAmount > 0) {
				updateSnapshots(transaction);
				currentStack = getStack();

                ItemStack newStack;
				if (currentStack.isEmpty()) {
					newStack = insertedVariant.toStack(insertedAmount);
				} else {
					newStack = currentStack.withQuantity(currentStack.getQuantity() + insertedAmount);
				}

				setStack(newStack);

				return insertedAmount;
			}
		}

		return 0;
	}

	@Override
	public long extract(ItemVariant variant, long maxAmount, @Nonnull TransactionContext transaction) {
		StoragePreconditions.notBlankNotNegative(variant, maxAmount);

		ItemStack currentStack = getStack();

		if (currentStack.getItem().equals(variant.getObject()) && canExtract(variant, (int) maxAmount)) {
			int extracted = (int) Math.min(currentStack.getQuantity(), maxAmount);

			if (extracted > 0) {
				this.updateSnapshots(transaction);
				currentStack = getStack();
				var newStack = currentStack.withQuantity(currentStack.getQuantity() - extracted);
				setStack(newStack);

				return extracted;
			}
		}

		return 0;
	}

	@Nonnull
    @Override
	protected ItemStack createSnapshot() {
		return getStack();
	}

	@Override
	protected void readSnapshot(@Nonnull ItemStack snapshot) {
		setStack(snapshot);
	}

	@Override
	public String toString() {
		return "SingleStackStorage[" + getStack() + "]";
	}
}