package xyz.faewulf.piggyback.mixinClient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.piggyback.inter.ICustomPlayerRenderState;
import xyz.faewulf.piggyback.util.config.ModConfigs;

import java.util.List;

@Mixin(AvatarRenderer.class)
public abstract class PlayerRendererMixin<AvatarlikeEntity extends Avatar & ClientAvatarEntity> extends LivingEntityRenderer<AvatarlikeEntity, AvatarRenderState, PlayerModel> {

    public PlayerRendererMixin(EntityRendererProvider.Context context, PlayerModel model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Unique
    // To check if localPlayer riding localPlayer1 (Tower stacking is also count)
    private static boolean piggyback$containClientPlayer(AbstractClientPlayer localPlayer1, LocalPlayer localPlayer) {
        List<Entity> entityList = localPlayer1.getPassengers();
        boolean hasLocalPlayer = false;

        for (Entity entity : entityList) {
            if (entity instanceof LocalPlayer localPlayer2) {

                // If is client player then break
                if (localPlayer2 == localPlayer) {
                    hasLocalPlayer = true;
                    break;
                }
            }
        }

        return hasLocalPlayer;
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At("TAIL"))
    private void setModelPoseInject(AvatarlikeEntity avatar, AvatarRenderState avatarRenderState, float f, CallbackInfo ci) {

        // reset value
        ((ICustomPlayerRenderState) avatarRenderState).piggyback$setIsInvisibleWhileRiding(false);
        ((ICustomPlayerRenderState) avatarRenderState).piggyback$setIsInvisibleWhileCarrying(false);

        if (ModConfigs.effect_translucent_carrier == 100 && ModConfigs.effect_translucent == 100)
            return;

        Entity vehicle = avatar.getVehicle();
        LocalPlayer localPlayer = Minecraft.getInstance().player;

        // If this player is riding client player
        if (vehicle instanceof LocalPlayer localPlayer1 && localPlayer == localPlayer1) {
            if (Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
                ((ICustomPlayerRenderState) avatarRenderState).piggyback$setIsInvisibleWhileRiding(true);
            }
        }

        // If this player is carrying client player
        if (avatar instanceof AbstractClientPlayer localPlayer1 && piggyback$containClientPlayer(localPlayer1, localPlayer)) {
            if (Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
                ((ICustomPlayerRenderState) avatarRenderState).piggyback$setIsInvisibleWhileCarrying(true);
            }
        }

    }
}
