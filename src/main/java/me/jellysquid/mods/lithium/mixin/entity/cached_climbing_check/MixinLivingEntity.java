package me.jellysquid.mods.lithium.mixin.entity.cached_climbing_check;

import me.jellysquid.mods.lithium.common.block.SectionModCounter;
import me.jellysquid.mods.lithium.common.util.Pos;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.CollisionView;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
    private long lastClimbingUpdate = Long.MAX_VALUE;
    private boolean cachedClimbing = false;

    private MixinLivingEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    /**
     * Invalidates the last climbing update when moved
     */
    @Override
    public void setPosition(double x, double y, double z) {
        if (this.getBlockX() != MathHelper.floor(x) || this.getBlockY() != MathHelper.floor(y) || this.getBlockZ() != MathHelper.floor(z)) {
            this.lastClimbingUpdate = Long.MAX_VALUE;
        }
        super.setPosition(x, y, z);
    }

    @Inject(method = "isClimbing", cancellable = true, at = @At("HEAD"))
    private void useCacheIsClimbing(CallbackInfoReturnable<Boolean> cir) {
        int blockY = this.getBlockY();
        if (!this.world.isOutOfHeightLimit(blockY)) {
            Chunk chunk = (Chunk) ((CollisionView) this.world).getChunkAsView(Pos.ChunkCoord.fromBlockCoord(this.getBlockX()), Pos.ChunkCoord.fromBlockCoord(this.getBlockZ()));
            if (chunk != null) {
                SectionModCounter section = (SectionModCounter) chunk.getSectionArray()[Pos.SectionYIndex.fromBlockCoord(this.world, blockY)];

                if (!ChunkSection.isEmpty((ChunkSection) section)) {
                    if (section.isUnchanged(this.lastClimbingUpdate)) {
                        cir.setReturnValue(this.cachedClimbing);
                    }
                    this.lastClimbingUpdate = section.getModCount();
                } else {
                    this.lastClimbingUpdate = Long.MAX_VALUE;
                }
            }
        }
    }

    @Inject(method = "isClimbing", at = @At("RETURN"))
    private void setCacheIsClimbing(CallbackInfoReturnable<Boolean> cir) {
        this.cachedClimbing = cir.getReturnValueZ();
    }
}
