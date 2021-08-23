package me.jellysquid.mods.lithium.mixin.entity.cached_climbing_check;

import me.jellysquid.mods.lithium.common.block.SectionModCounter;
import me.jellysquid.mods.lithium.common.util.Pos;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.CollisionView;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    private BlockPos climbingCachePos = null;
    private boolean climbingCacheResult = false;
    private long climbingCacheSectionModCount = Long.MAX_VALUE;

    private LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(
            method = "isClimbing()Z",
            cancellable = true,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;getBlockPos()Lnet/minecraft/util/math/BlockPos;",
                    shift = At.Shift.BEFORE
            )
    )
    private void useCacheIsClimbing(CallbackInfoReturnable<Boolean> cir) {
        BlockPos blockPos = this.getBlockPos();
        int blockY = blockPos.getY();
        if (this.climbingCachePos != null) {
            if (blockPos.equals(this.climbingCachePos) && !this.world.isOutOfHeightLimit(blockY)) {
                Chunk chunk = (Chunk) ((CollisionView) this.world).getChunkAsView(Pos.ChunkCoord.fromBlockCoord(this.getBlockX()), Pos.ChunkCoord.fromBlockCoord(this.getBlockZ()));
                if (chunk != null) {
                    SectionModCounter section = (SectionModCounter) chunk.getSectionArray()[Pos.SectionYIndex.fromBlockCoord(this.world, blockY)];
                    if (!ChunkSection.isEmpty((ChunkSection) section)) {
                        if (section.isUnchanged(this.climbingCacheSectionModCount)) {
                            cir.setReturnValue(this.climbingCacheResult);
                            return;
                        }
                    }
                }
            }
            this.resetClimbingCache();
        }
    }

    private void resetClimbingCache() {
        this.climbingCachePos = null;
        this.climbingCacheResult = false;
        this.climbingCacheSectionModCount = Long.MAX_VALUE;
    }

    @Inject(
            method = "isClimbing()Z",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Ljava/util/Optional;of(Ljava/lang/Object;)Ljava/util/Optional;",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            )
    )
    private void setCachedClimbing(CallbackInfoReturnable<Boolean> cir) {
        this.setCacheIsClimbing(true);
    }

    @Inject(
            method = "isClimbing()Z",
            at = @At(
                    value = "RETURN",
                    ordinal = 3,
                    shift = At.Shift.BEFORE
            )
    )
    private void setCachedNotClimbing(CallbackInfoReturnable<Boolean> cir) {
        this.setCacheIsClimbing(false);
    }

    private void setCacheIsClimbing(boolean climbing) {
        this.climbingCacheResult = climbing;
        BlockPos climbingCachePos = this.getBlockPos();
        this.climbingCachePos = climbingCachePos;
        this.climbingCacheSectionModCount = -1;

        if (!this.world.isOutOfHeightLimit(climbingCachePos)) {
            Chunk chunk = this.world.getChunk(Pos.ChunkCoord.fromBlockCoord(this.getBlockX()), Pos.ChunkCoord.fromBlockCoord(this.getBlockZ()));
            if (chunk != null) {
                SectionModCounter section = (SectionModCounter) chunk.getSectionArray()[Pos.SectionYIndex.fromBlockCoord(this.world, climbingCachePos.getY())];

                if (!ChunkSection.isEmpty((ChunkSection) section)) {
                    this.climbingCacheSectionModCount = section.getModCount();
                }
            }
        }
    }
}
