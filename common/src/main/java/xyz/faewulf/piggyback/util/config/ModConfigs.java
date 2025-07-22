package xyz.faewulf.piggyback.util.config;

import xyz.faewulf.lib.util.config.Entry;
import xyz.faewulf.lib.util.config.ModConfig;
import xyz.faewulf.lib.util.config.SliderEntry;
import xyz.faewulf.piggyback.Constants;

@ModConfig(mod_id = Constants.MOD_ID)
public class ModConfigs {

    @Entry(category = "general", name = "Disable ability to ride", group = "Server side")
    public static boolean disable_ride_function = false;

    @SliderEntry(min = 0, max = 100)
    @Entry(category = "general", name = "Translucent carrier in 1st person view", group = "Client side", info = "0 to disable render player model")
    public static int effect_translucent_carrier = 100;

    @SliderEntry(min = 0, max = 100)
    @Entry(category = "general", name = "Translucent rider in 1st person view", group = "Client side", info = "0 to disable render player model")
    public static int effect_translucent = 100;

    @Entry(category = "general", name = "Weighted Carrying", group = "Server side")
    public static boolean slow_carry = false;

    @Entry(category = "general", name = "Carrying Causes Hunger", group = "Server side")
    public static boolean hunger_carry = false;

    @Entry(category = "general", name = "Dismount on falling", group = "Server side")
    public static boolean falling_dismount = false;
}