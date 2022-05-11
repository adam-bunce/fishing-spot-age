package com.fishingspotage;

import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.*;
import java.awt.*;
import javax.inject.Inject;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import net.runelite.api.Point;
import static net.runelite.api.coords.LocalPoint.fromWorld;
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


        // Plugin toggle on/off order makes one of them appear on top
        // TODO make it so tiem effects saturation of the colour
            for (int id : plugin.fishingSpots.keySet()){
                // this would cause the thing to not render on certain spots
//                if (client.getCachedNPCs()[id] == null){
//
//                    System.out.println("CHECK IF THIS BREAKS IT");
//                    break;
//                }
                // Cannot invoke "net.runelite.api.NPC.getCanvasTilePoly()" because "net.runelite.api.Client.getCachedNPCs()[id]" is null


                Polygon poly = null;

                if (client.getCachedNPCs()[id] != null){
                    poly = client.getCachedNPCs()[id].getCanvasTilePoly();
                }

                // Polygon poly = client.getCachedNPCs()[id].getCanvasTilePoly();

                if (poly != null)
                {

                    // System.out.println(plugin.fishingSpots.get(id).time);
                    long spotAge = Duration.between(plugin.fishingSpots.get(id).getTime(), Instant.now()).toMillis();
                    LocalPoint hold = fromWorld(this.client, client.getCachedNPCs()[id].getWorldLocation());

                    long minutes = TimeUnit.MILLISECONDS.toMinutes(spotAge);
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(spotAge);

                    seconds = seconds%60;


                    //TODO add font adjusted in config
                    // TODO redo the timings, maybe get an excel sheet going and


                    // TODO move overlay up so its compatible with the default fishing plugin
                    // analyse fishing spot age
                    // 15 sec

                   // spot movement not observed yet
                    // because its negative 1 i get rollover to some insanley high int
//                    if (plugin.fishingSpots.get(id).getTime().equals(Instant.ofEpochMilli(-1))) {
                    if (plugin.fishingSpots.get(id).getTime().equals(Instant.ofEpochMilli(-1))) {
                        OverlayUtil.renderPolygon(graphics, poly, new Color (15, 0, 255) );
                        Point textloc = Perspective.getCanvasTextLocation(this.client, graphics, hold, "Unknown", 0);
                        OverlayUtil.renderTextLocation(graphics, textloc, "Unknown", new Color (255, 255, 255, 230));
                    }
                    else{
                        Point textloc = Perspective.getCanvasTextLocation(this.client, graphics, hold, String.valueOf(minutes ) + ":" + String.valueOf(seconds), 0);
                        OverlayUtil.renderTextLocation(graphics, textloc, String.valueOf(minutes ) + ":" + String.valueOf(seconds), new Color (255, 255, 255, 230));


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
                        else if (spotAge < 180000)
                        {
                            OverlayUtil.renderPolygon(graphics, poly, new Color (232, 121, 13));
                        }
                        // 2min +
                        else if (spotAge > 180001)
                        {
                            OverlayUtil.renderPolygon(graphics, poly,Color.RED);
                        }
                    }


                }

            }



        return null;
    }
}
