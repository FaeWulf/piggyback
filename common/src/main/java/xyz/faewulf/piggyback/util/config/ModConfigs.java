package xyz.faewulf.piggyback.util.config;

import xyz.faewulf.piggyback.Constants;
import xyz.faewulf.lib.util.config.Entry;
import xyz.faewulf.lib.util.config.ModConfig;

@ModConfig(mod_id = Constants.MOD_ID)
public class ModConfigs {

    @Entry(category = "general", name = "Disable ability to ride", group = "Server side")
    public static boolean disable_ride_function = false;

    @Entry(category = "general", name = "Hide rider in 1st person", group = "Client side")
    public static boolean hide_rider = false;

    @Entry(category = "general", name = "Weighted Carrying", group = "Server side")
    public static boolean slow_carry = false;

    @Entry(category = "general", name = "Carrying Causes Hunger", group = "Server side")
    public static boolean hunger_carry = false;

    @Entry(category = "general", name = "Dismount on falling", group = "Server side")
    public static boolean falling_dismount = false;
}