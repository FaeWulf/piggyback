package xyz.faewulf.piggyback.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.piggyback.event.performRiding;

@Mixin(value = Player.class, priority = 1)
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

    @Inject(method = "interactOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;", ordinal = 0), cancellable = true)
    private void Piggyback$interactOnInject(Entity entityToInteractOn, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if((Object) this instanceof Player player) {
            performRiding.run(this.level(), player, hand, entityToInteractOn, null);
        }
    }
}
