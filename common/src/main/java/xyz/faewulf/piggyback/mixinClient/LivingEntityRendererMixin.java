package xyz.faewulf.piggyback.mixinClient;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import xyz.faewulf.piggyback.util.config.ModConfigs;

import java.util.List;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {
    protected LivingEntityRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Unique
    private static boolean piggyback$containClientPlayer(AbstractClientPlayer localPlayer1, LocalPlayer localPlayer) {
        List<Entity> entityList = localPlayer1.getPassengers();
        boolean hasLocalPlayer = false;

        for (Entity entity : entityList) {
            if (entity instanceof LocalPlayer localPlayer2) {

                // If is client playuer then break
                if (localPlayer2 == localPlayer) {
                    hasLocalPlayer = true;
                    break;
                }
            }
        }

        return hasLocalPlayer;
    }

    // Translucent for rider (rider render to carrier), carrier is client player
    // livingEntity is the rider renderer
    @ModifyReturnValue(method = "getRenderType", at = @At("RETURN"))
    private RenderType getRenderTypeModifyReturnValue(RenderType original, @Local ResourceLocation resourcelocation, @Local(argsOnly = true) T livingEntity) {
        Entity vehicle = livingEntity.getVehicle();
        LocalPlayer localPlayer = Minecraft.getInstance().player;

        if (localPlayer == null)
            return original;

        // If local player is vehicle
        if (vehicle instanceof LocalPlayer localPlayer1 && localPlayer == localPlayer1) {
            if (Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
                return RenderType.itemEntityTranslucentCull(resourcelocation);
            }
        }

        // If local player is riding that vehicle
        if (livingEntity instanceof AbstractClientPlayer localPlayer1 && piggyback$containClientPlayer(localPlayer1, localPlayer)) {
            if (Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
                return RenderType.itemEntityTranslucentCull(resourcelocation);
            }
        }

        return original;
    }

    // Translucent for rider (rider render to carrier), carrier is client player
    // livingEntity is the rider renderer
    @WrapOperation(
            method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V")
    )
    private void RenderToBufferTranslucentEffectInject(EntityModel instance, PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int i, int color, Operation<Void> original, @Local(argsOnly = true) T livingEntity) {
        Entity vehicle = livingEntity.getVehicle();
        LocalPlayer localPlayer = Minecraft.getInstance().player;

        if (localPlayer == null) {
            original.call(instance, poseStack, vertexConsumer, packedLight, i, color);
            return;
        }

        if (vehicle instanceof LocalPlayer localPlayer1 && localPlayer == localPlayer1) {
            if (Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
                int alpha = (int) (ModConfigs.effect_translucent * 1.0f / 100 * 255); // opacity from 0.0 to 1.0
                // white with desired alpha
                int colorTransparent = (alpha << 24) | 0xFFFFFF;

                original.call(instance, poseStack, vertexConsumer, packedLight, i, colorTransparent);
                return;
            }
        }

        // If local player is riding that vehicle
        if (livingEntity instanceof AbstractClientPlayer localPlayer1 && piggyback$containClientPlayer(localPlayer1, localPlayer)) {
            if (Minecraft.getInstance().options.getCameraType().isFirstPerson()) {

                int alpha = (int) (ModConfigs.effect_translucent_carrier * 1.0f / 100 * 255); // opacity from 0.0 to 1.0
                // white with desired alpha
                int colorTransparent = (alpha << 24) | 0xFFFFFF;

                original.call(instance, poseStack, vertexConsumer, packedLight, i, colorTransparent);
                return;
            }
        }

        original.call(instance, poseStack, vertexConsumer, packedLight, i, color);
    }

}
