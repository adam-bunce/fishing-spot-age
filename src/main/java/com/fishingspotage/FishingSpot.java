package com.fishingspotage;

import lombok.AllArgsConstructor;
import lombok.Value;
import net.runelite.api.coords.WorldPoint;

import java.time.Instant;

@AllArgsConstructor
@Value
public class FishingSpot {
    private final WorldPoint location;
    private final Instant time;

}
