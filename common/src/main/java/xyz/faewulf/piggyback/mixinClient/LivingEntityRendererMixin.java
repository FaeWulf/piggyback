package xyz.faewulf.piggyback.mixinClient;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import xyz.faewulf.piggyback.inter.ICustomPlayerRenderState;
import xyz.faewulf.piggyback.util.config.ModConfigs;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends EntityRenderer<T, S> implements RenderLayerParent<S, M> {
    protected LivingEntityRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Shadow
    public abstract @NotNull M getModel();

    @ModifyVariable(method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", at = @At("STORE"), ordinal = 1)
    private int injectedCustomAlphaValue(int original, @Local(argsOnly = true) S renderState) {

        if (renderState instanceof ICustomPlayerRenderState iCustomPlayerRenderState) {

            // Set translucent for rider
            if (iCustomPlayerRenderState.piggyback$getIsInvisibleWhileRiding()) {
                int alpha = (int) (ModConfigs.effect_translucent * 1.0f / 100 * 255); // opacity from 0.0 to 1.0
                // white with desired alpha
                return (alpha << 24) | 0xFFFFFF;
            }

            // Set translucent for carrier
            if (iCustomPlayerRenderState.piggyback$getIsInvisibleWhileCarrying()) {
                int alpha = (int) (ModConfigs.effect_translucent_carrier * 1.0f / 100 * 255); // opacity from 0.0 to 1.0
                // white with desired alpha
                return (alpha << 24) | 0xFFFFFF;
            }

        }

        return original;
    }

    @WrapOperation(
            method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/RenderType;IIILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V")
    )
    private void cancelRenderIfValueIsZero(SubmitNodeCollector instance, Model model, Object o, PoseStack poseStack, RenderType renderType, int i, int i1, int i2, TextureAtlasSprite textureAtlasSprite, int i3, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, Operation<Void> original, @Local(argsOnly = true) S renderState) {

        if (renderState instanceof ICustomPlayerRenderState iCustomPlayerRenderState) {

            // Prevent render call for rider
            if (iCustomPlayerRenderState.piggyback$getIsInvisibleWhileRiding() && ModConfigs.effect_translucent == 0) {
                return;
            }

            // Prevent render call for carrier
            if (iCustomPlayerRenderState.piggyback$getIsInvisibleWhileCarrying() && ModConfigs.effect_translucent_carrier == 0) {
                return;
            }

        }

        original.call(instance, model, o, poseStack, renderType, i, i1, i2, textureAtlasSprite, i3, crumblingOverlay);
    }
}
