package net.runelite.client.plugins.playertile;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.BasicStroke;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.ChatMessageType;
import net.runelite.api.events.ChatMessage;
//import net.runelite.client.plugins.tileindicators;

public class PlayerTileOverlay extends Overlay {


    private final Client client;
    private final PlayerTileConfig config;


    @Inject
    private PlayerTileOverlay(Client client, PlayerTileConfig config) {
        this.client = client;
        this.config = config;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
        setPriority(OverlayPriority.MED);
    }

    //WorldPoint getWorldLocation();
    @Override
    public Dimension render(Graphics2D graphics) {

        //if (config.highlightTile()) {
            // If we are not moving then check config if render!
          /*  if (client.getSelectedSceneTile() != null)
            {
                renderTile(graphics, client.getSelectedSceneTile().getLocalLocation(), config.highlightHoveredColor());

            }*/
            //this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Tile is highlighted", null);

        if (!config.highlightStandingTile()) {
            WorldPoint world = client.getLocalPlayer().getWorldLocation();
            LocalPoint localWorld = LocalPoint.fromWorld(client, world);
            LocalPoint localEndPoint = client.getLocalDestinationLocation();

            if (localWorld.distanceTo(localEndPoint) != 0) {
                renderTile(graphics, client.getLocalPlayer().getWorldLocation(), config.highlightTileColor());
            }
        }
        else {
            renderTile(graphics, client.getLocalPlayer().getWorldLocation(), config.highlightTileColor());
        }


        return null;
    }

    private void renderTile(final Graphics2D graphics, final WorldPoint pos, final Color color) {
        if (pos == null) {
            //this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "World pos is null", null);
            return;
        }
        LocalPoint lp = LocalPoint.fromWorld(client, pos);
        final Polygon poly = Perspective.getCanvasTileAreaPoly(client, lp, 1);

        if (poly == null) {
            //this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Poly is null", null);
            return;
        }
        OverlayUtil.renderPolygon(graphics, poly, color);
        /*graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 255));
        graphics.setStroke(new BasicStroke(1));
        graphics.draw(poly);*/
        graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), config.transparency()));
        graphics.fill(poly);


    }
}

