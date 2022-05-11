package com.fishingspotage;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@PluginDescriptor(
	name = "Fishing Spot Age"
)
public class FishingSpotAgePlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private FishingSpotAgeConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject FishingSpotAgeOverlay overlay;

	final Map<Integer , FishingSpot> fishingSpots= new HashMap<>();



	@Override
	protected void startUp() throws Exception
	{
		log.info("Fishing Spot Age started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Fishing Spot Age stopped!");
	}

	// on spawn set the time to -1 then if its negative 1 make it brown or smth then change it only on move events
	@Subscribe
	public void onNpcSpawned(NpcSpawned npcSpawned)
	{

		System.out.println(npcSpawned.getNpc().getName());
		if (npcSpawned.getNpc().getName().equals("Rod Fishing spot") || npcSpawned.getNpc().getName().equals("Fishing spot")){
			System.out.println("Rod Fishing Spot Spawned");
			System.out.println(npcSpawned.getNpc().getIndex());
			System.out.println();
			// fishingSpots.put(npcSpawned.getNpc().getIndex(), npcSpawned.getNpc().getWorldLocation());
			FishingSpot spot =new FishingSpot(npcSpawned.getNpc().getWorldLocation(), Instant.now());

			overlayManager.add(overlay);
			fishingSpots.put(npcSpawned.getNpc().getIndex(), spot);

			// overlayManager.add(overlay);
		}
	}

	// check if any of the spots have moved, if they have reset their timer otherwise check
	// if the colour of their tile needs to be adjusted
	@Subscribe
	public void onGameTick(GameTick gameTick){

		// check if youre in a fishing area before doing this to save on computnig pwoer

		// [id, worldlocation]
		for (Integer id : fishingSpots.keySet())
		{
			if (client.getCachedNPCs() != null)
			{

				// this is weird it does the opposite of what i thought maybe im just braindead though
				// why didnt != work wtf
				if (!(client.getCachedNPCs()[id].getWorldLocation().equals(fishingSpots.get(id).getLocation())))
				{

					//  TODO rest spot time on relog as it will no longer be accurate
					System.out.println("SPOT MOVED");
					FishingSpot fishingSpot = new FishingSpot(client.getCachedNPCs()[id].getWorldLocation(), Instant.now());
					fishingSpots.put(id, fishingSpot);

					overlayManager.add(overlay);
					System.out.println(fishingSpots);
				}
			}
		}
	}

	@Provides
	FishingSpotAgeConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(FishingSpotAgeConfig.class);
	}
}
