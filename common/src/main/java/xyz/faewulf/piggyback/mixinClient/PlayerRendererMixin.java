package xyz.faewulf.piggyback.mixinClient;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.piggyback.util.config.ModConfigs;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public PlayerRendererMixin(EntityRendererProvider.Context context, PlayerModel<AbstractClientPlayer> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(method = "setModelProperties", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/player/PlayerRenderer;getArmPose(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/client/model/HumanoidModel$ArmPose;", ordinal = 1))
    private void setModelPoseInject(AbstractClientPlayer clientPlayer, CallbackInfo ci, @Local PlayerModel<AbstractClientPlayer> playermodel) {

        if(!ModConfigs.hide_rider)
            return;

        Entity vehicle = clientPlayer.getVehicle();
        LocalPlayer localPlayer = Minecraft.getInstance().player;

        if(vehicle instanceof LocalPlayer localPlayer1 && localPlayer == localPlayer1) {

            // The player is in first-person mode
            if (Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
                playermodel.leftLeg.visible = false;
                playermodel.rightLeg.visible = false;
                playermodel.leftPants.visible = false;
                playermodel.rightPants.visible = false;
            }
        }
    }
}
