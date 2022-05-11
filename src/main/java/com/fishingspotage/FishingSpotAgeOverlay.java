package com.fishingspotage;

import jdk.vm.ci.meta.Local;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.*;


import java.awt.*;
import javax.inject.Inject;

import net.runelite.client.ui.overlay.components.ProgressBarComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;

import net.runelite.api.Point;

import static net.runelite.api.coords.LocalPoint.fromScene;
import static net.runelite.api.coords.LocalPoint.fromWorld;
import static net.runelite.api.coords.WorldPoint.toLocalInstance;
import net.runelite.client.ui.overlay.components.TextComponent;
public class FishingSpotAgeOverlay extends Overlay {


    private final FishingSpotAgePlugin plugin;
    private final FishingSpotAgeConfig config;
    private final Client client;

    private final TextComponent textComponent = new TextComponent();

    @Inject
    public FishingSpotAgeOverlay(FishingSpotAgePlugin plugin, FishingSpotAgeConfig config, Client client)
    {
        this.plugin = plugin;
        this.config = config;
        this.client = client;
        setPosition(OverlayPosition.DYNAMIC); // this fixes the polygon beging shifted right like 15 tiles
        setLayer(OverlayLayer.ABOVE_SCENE);

    }


    @Override
    public Dimension render(Graphics2D graphics)
    {

        // TODO make it so tiem effects saturation of the colour

            // add conditional render colours based on the time the associated "npc" has

            // need to loop through the fish spots and then do this for each id in the list

            for (int id : plugin.fishingSpots.keySet()){
                if (client.getCachedNPCs()[id] == null){
                    break;
                }

                Polygon poly = client.getCachedNPCs()[id].getCanvasTilePoly();

                if (poly != null)
                {

                    // System.out.println(plugin.fishingSpots.get(id).time);
                    long spotAge = Duration.between(plugin.fishingSpots.get(id).getTime(), Instant.now()).toMillis();


                    LocalPoint hold = fromWorld(this.client, client.getCachedNPCs()[id].getWorldLocation());


                    Point textloc = Perspective.getCanvasTextLocation(this.client, graphics, hold, String.valueOf(spotAge ), 0);


                    // text overlay
                    OverlayUtil.renderTextLocation(graphics, textloc, String.valueOf(spotAge ), new Color (155, 0, 0, 230));

                    // TODO redo the timings, maybe get an excel sheet going and
                    // analyse fishing spot age
                    // 15 sec
                    if (spotAge < 15000)
                    {
                        OverlayUtil.renderPolygon(graphics, poly, new Color (102, 255, 102) );
                    }
                    // 1 min
                    else if (spotAge < 60000)
                    {
                        OverlayUtil.renderPolygon(graphics, poly, new Color(255, 250, 119));
                    }
                    // 2 min
                    else if (spotAge < 120000)
                    {
                        OverlayUtil.renderPolygon(graphics, poly, new Color (232, 121, 13));
                    }
                    // 2min +
                    else
                    {
                        OverlayUtil.renderPolygon(graphics, poly,Color.RED);
                    }

                }

            }

//
//            Polygon poly = client.getCachedNPCs()[13840].getCanvasTilePoly();
//
//            if (poly != null)
//            {
//                OverlayUtil.renderPolygon(graphics, poly,Color.MAGENTA );
//            }

        return null;
    }
}
