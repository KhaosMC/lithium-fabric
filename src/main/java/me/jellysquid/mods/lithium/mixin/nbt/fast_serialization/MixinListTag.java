package me.jellysquid.mods.lithium.mixin.nbt.fast_serialization;

import com.google.common.collect.Lists;
import me.jellysquid.mods.lithium.common.nbt.TagFIO;
import me.jellysquid.mods.lithium.common.nbt.io.NbtIn;
import me.jellysquid.mods.lithium.common.nbt.io.NbtOut;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.PositionTracker;
import net.minecraft.nbt.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ListTag.class)
public class MixinListTag implements TagFIO {
    @Shadow
    private List<Tag> value;

    @Shadow
    private byte type;

    @Override
    public void serialize(NbtOut out) {
        if (this.value.isEmpty()) {
            this.type = 0;
        } else {
            this.type = this.value.get(0).getType();
        }

        out.writeByte(this.type);
        out.writeInt(this.value.size());

        for (Tag tag : this.value) {
            ((TagFIO) tag).serialize(out);
        }
    }

    @Override
    public void deserialize(NbtIn in, int level, PositionTracker positionTracker) {
        positionTracker.add(296L);

        if (level > 512) {
            throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
        }

        this.type = in.readByte();

        int count = in.readInt();

        if (this.type == 0 && count > 0) {
            throw new RuntimeException("Missing type on ListTag");
        }

        positionTracker.add(32L * (long)count);

        this.value = Lists.newArrayListWithCapacity(count);

        for(int i = 0; i < count; ++i) {
            Tag tag = Tag.createTag(this.type);
            ((TagFIO) tag).deserialize(in, level + 1, positionTracker);

            this.value.add(tag);
        }

    }
}
