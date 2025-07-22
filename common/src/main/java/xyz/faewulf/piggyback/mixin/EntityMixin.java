package xyz.faewulf.piggyback.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.commands.CommandSource;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityAccess;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin implements Nameable, EntityAccess, CommandSource {

    @Shadow
    private EntityDimensions dimensions;
    @Shadow
    private Level level;

    @Shadow
    public abstract boolean hasPassenger(Entity passenger);

    @Shadow
    @Nullable
    public abstract Entity getVehicle();

    @Inject(method = "positionRider(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity$MoveFunction;)V", at = @At("TAIL"))
    private void updatePassengerPositionInject(Entity passenger, Entity.MoveFunction positionUpdater, CallbackInfo ci) {
        if (this.hasPassenger(passenger) && ((Entity) (Object) this) instanceof Player player) {
            float h = 0.6F;
            float f = Mth.sin(player.yBodyRot * 0.017453292F);
            float g = Mth.cos(player.yBodyRot * 0.017453292F);

            float height = (float) (this.dimensions.height() * 0.2f);

            // Change passenger yaw based on vehicle's yaw
            // Head yaw will be clamped to prevent 360* heads rotate, since it very... disturbing
            float baseYaw = player.yBodyRot;
            float yawDifference = Mth.wrapDegrees(passenger.getYRot() - baseYaw);
            float clamped = Mth.clamp(yawDifference, -100.0F, 100.0F);
            passenger.setYRot(baseYaw + clamped);
            passenger.setYBodyRot(player.yBodyRot);

            // Set position
            passenger.setPos(player.getX() + (h * f), player.getY() + height, player.getZ() - (h * g));
        }
    }

    // Prevent client desync
    @Inject(method = "removePassenger", at = @At("TAIL"))
    private void onRemovePassenger(Entity passenger, CallbackInfo callbackInfo) {
        Entity entity = (Entity) (Object) this;

        if (!this.level.isClientSide && entity instanceof ServerPlayer serverPlayer)
            serverPlayer.connection.send(new ClientboundSetPassengersPacket(entity));
    }

    // Inject at the head of unRide to prevent vehicle from being saved with the player
    @Inject(method = "unRide", at = @At("HEAD"))
    private void onUnRide(CallbackInfo ci) {

        if (this.level.isClientSide)
            return;

        Entity vehicle = this.getVehicle();
        if (vehicle instanceof Player) {
            ((ServerPlayer) (Object) this).stopRiding();
        }
    }

    // Make player ridable
    @WrapOperation(
            method = "startRiding(Lnet/minecraft/world/entity/Entity;Z)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityType;canSerialize()Z")
    )
    private boolean startRidingAllowRidingPlayersInject(EntityType instance, Operation<Boolean> original) {
        if (instance == EntityType.PLAYER) {
            return true;
        } else {
            return original.call(instance);
        }
    }
}
