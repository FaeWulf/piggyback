package xyz.faewulf.piggyback;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import xyz.faewulf.piggyback.event_handler.performRiding;

public class Piggyback implements ModInitializer {

    @Override
    public void onInitialize() {
        Constants.LOG.info("Loading");

        loadCommand();
        loadEvent();

        CommonClass.init();

        Constants.LOG.info("Init done");
    }

    private void loadCommand() {
        Constants.LOG.info("Register commands...");
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
        });
    }

    private void loadEvent() {
        Constants.LOG.info("Register events...");
        performRiding.register();
    }

}
