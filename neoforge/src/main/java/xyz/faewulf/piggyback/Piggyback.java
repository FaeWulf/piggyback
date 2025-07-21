package xyz.faewulf.piggyback;

import net.neoforged.fml.common.Mod;

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

}