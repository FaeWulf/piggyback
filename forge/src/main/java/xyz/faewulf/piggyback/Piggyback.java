package xyz.faewulf.piggyback;


import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import xyz.faewulf.lib.api.v1.config.ConfigScreenHelper;
import xyz.faewulf.lib.util.config.infoScreen.ModInfoScreen;

@Mod(Constants.MOD_ID)
public class Piggyback {

    public Piggyback() {
        Constants.LOG.info("Loading");

        loadCommand();

        CommonClass.init();

        Constants.LOG.info("Init done");
    }

    private void loadCommand() {
        Constants.LOG.info("Register commands...");
    }

    // Client-side
    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            //config
            MinecraftForge.registerConfigScreen(
                    (client, parent) -> {
                        ModInfoScreen modInfoScreen = (ModInfoScreen) ConfigScreenHelper.getConfigScreen(parent, Constants.MOD_ID);
                        modInfoScreen.setUrls(null, Constants.WEBSITE, null, Constants.SOURCE_CODE);
                        return modInfoScreen;
                    }
            );
        }
    }

}