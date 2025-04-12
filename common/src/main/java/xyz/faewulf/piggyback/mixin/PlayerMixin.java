package xyz.faewulf.piggyback.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.piggyback.util.config.ModConfigs;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }
//
//    // For realistic piggyback mechanic
//    @Inject(method = "aiStep", at = @At("TAIL"))
//    private void tickInjectRemovePassengerOnInterruption(CallbackInfo callbackInfo)
//    {
//        if(!ModConfigs.falling_dismount || this.level().isClientSide)
//            return;
//
//        if(this.isVehicle() && !this.isCrouching() && this.fallDistance > 0.5F)
//            if(this.getFirstPassenger() != null)
//                this.getFirstPassenger().stopRiding();
//    }
}
