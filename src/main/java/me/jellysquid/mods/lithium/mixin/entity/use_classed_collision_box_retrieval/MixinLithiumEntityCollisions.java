package me.jellysquid.mods.lithium.mixin.entity.use_classed_collision_box_retrieval;

import me.jellysquid.mods.lithium.common.entity.LithiumEntityCollisions;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.world.EntityView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(LithiumEntityCollisions.class)
public class MixinLithiumEntityCollisions {
    @Redirect(method = "getEntityCollisions", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/EntityView;getEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Ljava/util/List;"))
    private static List<Entity> getEntities(EntityView view, Entity entity, Box selection) {
        return LithiumEntityCollisions.getEntitiesWithCollisionBoxForEntity(view, selection, entity);
    }
}
