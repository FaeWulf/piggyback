package xyz.faewulf.piggyback.event_handler;

import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import xyz.faewulf.piggyback.Constants;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class performRiding {
    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
//        InteractionResult interactionResult = xyz.faewulf.piggyback.event.performRiding.run(event.getLevel(), event.getEntity(), event.getHand(), event.getTarget(), null);
//
//        if (interactionResult.consumesAction())
//            event.setCanceled(true);
    }
}
