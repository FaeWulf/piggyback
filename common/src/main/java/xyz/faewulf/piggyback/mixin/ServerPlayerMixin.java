package xyz.faewulf.piggyback.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.piggyback.util.config.ModConfigs;


@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {

    public ServerPlayerMixin(Level world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    // Apply effects
    @Inject(method = "tick", at = @At("HEAD"))
    private void tickInjectApplySlow(CallbackInfo ci) {
        if (!this.getPassengers().isEmpty() && ModConfigs.slow_carry) {
            this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 3, 0, false, false));
        }

        if (!this.getPassengers().isEmpty() && ModConfigs.hunger_carry) {
            this.causeFoodExhaustion(0.005F);
        }
    }
    }

    // For realistic piggyback mechanic
    @Inject(method = "tick", at = @At("TAIL"))
    private void tickInjectRemovePassengerOnInterruption(CallbackInfo callbackInfo)
    {
        if(!ModConfigs.falling_dismount)
            return;

        if(this.isVehicle() && !this.isCrouching() && !this.isFallFlying() && this.fallDistance > 2F)
            if(this.getFirstPassenger() != null)
                this.getFirstPassenger().stopRiding();
    }

    // Modify to get top most passenger
    @ModifyVariable(method = "startRiding", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private Entity startRidingModifyParameter(Entity entity) {

        Entity lastPassenger = entity;

        while (lastPassenger instanceof Player player && player.getFirstPassenger() instanceof Player playerEntity) {
            lastPassenger = playerEntity;
        }

        if(lastPassenger != null) {
            return lastPassenger;
        }

        return entity;
    }

    // Send sync packet to prevent client desync from riding multiple players.
    @Inject(method = "startRiding", at = @At("RETURN"))
    private void startRidingInjectSyncPacket(Entity entity, boolean force, CallbackInfoReturnable<Boolean> cir) {
        if(entity instanceof ServerPlayer playerEntity && cir.getReturnValue()) {
            playerEntity.connection.send(new ClientboundSetPassengersPacket(playerEntity));
        }
    }
}
