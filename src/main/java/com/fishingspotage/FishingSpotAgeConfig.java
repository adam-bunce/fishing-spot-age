package com.fishingspotage;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("fishingspotage")
public interface FishingSpotAgeConfig extends Config
{
	@ConfigItem(
		keyName = "add colour options for the tile age indicators",
		name = "Welcome Greeting",
		description = "The message to show to the user when they login"
	)
	default String greeting()
	{
		return "Hello";
	}
}
