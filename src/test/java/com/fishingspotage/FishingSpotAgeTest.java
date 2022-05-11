package com.fishingspotage;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class FishingSpotAgeTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(FishingSpotAgePlugin.class);
		RuneLite.main(args);
	}
}