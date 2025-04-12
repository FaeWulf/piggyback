package xyz.faewulf.piggyback.event;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import xyz.faewulf.piggyback.util.config.ModConfigs;

public class performRiding {

    public static InteractionResult run(Level level, Player player, InteractionHand hand, Entity entity, HitResult hitResult) {

        //if not enable in config file
        if (ModConfigs.disable_ride_function)
            return InteractionResult.PASS;

        //if not mainhand
        if ((entity.getType() == EntityType.PLAYER)
                && hand == InteractionHand.MAIN_HAND
                && entity.isCrouching()
                && player.getItemInHand(hand).isEmpty()
                && hitResult == null
        ) {
            if (!level.isClientSide) {
                player.startRiding(entity, false);

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }
}
