package xyz.faewulf.piggyback.mixinClient;

import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import xyz.faewulf.piggyback.inter.ICustomPlayerRenderState;

@Mixin(PlayerRenderState.class)
public abstract class PlayerRenderStateMixin extends HumanoidRenderState implements ICustomPlayerRenderState {

    @Unique
    private boolean piggyback$isInvisibleWhileRiding = false;

    @Unique
    private boolean piggyback$isInvisibleWhileCarrying = false;

    @Override
    public boolean piggyback$getIsInvisibleWhileRiding() {
        return this.piggyback$isInvisibleWhileRiding;
    }

    @Override
    public void piggyback$setIsInvisibleWhileRiding(boolean value) {
        this.piggyback$isInvisibleWhileRiding = value;
    }

    @Override
    public boolean piggyback$getIsInvisibleWhileCarrying() {
        return this.piggyback$isInvisibleWhileCarrying;
    }


    @Override
    public void piggyback$setIsInvisibleWhileCarrying(boolean value) {
        this.piggyback$isInvisibleWhileCarrying = value;
    }
}
