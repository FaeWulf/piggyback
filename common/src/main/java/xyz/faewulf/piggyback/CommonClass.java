package xyz.faewulf.piggyback;

import net.minecraft.SharedConstants;
import xyz.faewulf.piggyback.platform.Services;
import xyz.faewulf.piggyback.util.config.ModConfigs;
import xyz.faewulf.lib.api.v1.config.ConfigHelper;

public class CommonClass {
    public static void init() {

        //Constants.LOG.info("Hello from Common init on {}! we are currently in a {} environment!", Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());
        //Constants.LOG.info("The ID for diamonds is {}", BuiltInRegistries.ITEM.getKey(Items.DIAMOND));

        ConfigHelper.register(xyz.faewulf.piggyback.Constants.MOD_ID, ModConfigs.class);

        //for debug/testing
        if (Services.PLATFORM.isDevelopmentEnvironment()) {
            //GameTestHelper.register("xyz.faewulf.diversity.util.gameTests.entry");
            SharedConstants.IS_RUNNING_IN_IDE = true;
        }

        //load config, moved to util.mixinPlugin.ConditionalMixinPlugin method: onLoad()
    }
}