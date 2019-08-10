package mcjty.rftoolsstorage.storage;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * A list of items that can be coupled to a storage card
 */
public class StorageEntry {

    private final ItemStackHandler items;
    private final UUID uuid;
    private int version;

    public StorageEntry(CompoundNBT nbt, @Nullable IStorageListener listener) {
        int slots = nbt.getInt("slots");
        items = createHandler(slots, listener);
        items.deserializeNBT(nbt.getCompound("Items"));
        uuid = nbt.getUniqueId("UUID");
        version = nbt.getInt("version");
    }

    public StorageEntry(int size, UUID uuid, @Nullable IStorageListener listener) {
        items = createHandler(size, listener);
        this.uuid = uuid;
        this.version = 1;
    }

    private ItemStackHandler createHandler(int size, IStorageListener listener) {
        return new ItemStackHandler(size) {
            @Override
            protected void onContentsChanged(int slot) {
                if (listener != null) {
                    listener.entryChanged(StorageEntry.this);
                }
                version++;
                super.onContentsChanged(slot);
            }
        };
    }

    public ItemStackHandler getHandler() {
        return items;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getVersion() {
        return version;
    }

    public CompoundNBT write() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("slots", items.getSlots());
        nbt.putInt("version", version);
        nbt.put("Items", items.serializeNBT());
        nbt.putUniqueId("UUID", uuid);
        return nbt;
    }
}
