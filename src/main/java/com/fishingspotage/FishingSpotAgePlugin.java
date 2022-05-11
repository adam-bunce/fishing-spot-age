package com.fishingspotage;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcDespawned;
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
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Fishing Spot Age stopped!");
		overlayManager.remove(overlay);
	}

	@Subscribe
	public void onNpcSpawned(NpcSpawned npcSpawned)
	{

		System.out.println(npcSpawned.getNpc().getName());
		if (npcSpawned.getNpc().getName().equals("Rod Fishing spot") || npcSpawned.getNpc().getName().equals("Fishing spot")){
			System.out.println("Rod Fishing Spot Spawned");
			System.out.println(npcSpawned.getNpc().getIndex());
			System.out.println();

			FishingSpot spot =new FishingSpot(npcSpawned.getNpc().getWorldLocation(), Instant.ofEpochMilli(-1));

			fishingSpots.put(npcSpawned.getNpc().getIndex(), spot);

		}
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned npcDespawned){
		System.out.println(npcDespawned.getNpc().getName());
		if (npcDespawned.getNpc().getName().equals("Rod Fishing spot") || npcDespawned.getNpc().getName().equals("Fishing spot")){
			System.out.println("Rod Fishing Spot Despawned");
			System.out.println(npcDespawned.getNpc().getIndex());
			System.out.println();

			FishingSpot spot =new FishingSpot(npcDespawned.getNpc().getWorldLocation(), Instant.now());
			fishingSpots.remove(npcDespawned.getNpc().getIndex(), spot);

		}
	}


	// check if any of the spots have moved, if they have reset their timer otherwise check
	// if the colour of their tile needs to be adjusted
	@Subscribe
	public void onGameTick(GameTick gameTick){
		for (Integer id : fishingSpots.keySet())
		{
			if (client.getCachedNPCs()[id] != null)
			{

				if (!(client.getCachedNPCs()[id].getWorldLocation().equals(fishingSpots.get(id).getLocation())))
				{

					System.out.println("SPOT MOVED after: " + fishingSpots.get(id).getTime() );
					FishingSpot fishingSpot = new FishingSpot(client.getCachedNPCs()[id].getWorldLocation(), Instant.now());
					fishingSpots.put(id, fishingSpot);
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
