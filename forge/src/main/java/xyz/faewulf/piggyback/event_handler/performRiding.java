package xyz.faewulf.piggyback.event_handler;

import net.minecraft.world.InteractionResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.faewulf.piggyback.Constants;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class performRiding {
    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
//        InteractionResult interactionResult = xyz.faewulf.piggyback.event.performRiding.run(event.getLevel(), event.getEntity(), event.getHand(), event.getTarget(), null);
//        if (interactionResult.consumesAction()) {
//            event.setCanceled(true);
//        }
    }
}
