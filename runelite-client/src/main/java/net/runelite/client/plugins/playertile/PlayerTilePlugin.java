package net.runelite.client.plugins.playertile;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
        name = "Player Tile",
        description = "Highlight the tile you are currently ON",
        tags = {"highlight", "overlay"},
        enabledByDefault = false
)
public class PlayerTilePlugin extends Plugin
{
    @Inject
    private OverlayManager overlayManager;

    @Inject
    private PlayerTileOverlay overlay;

    @Provides
    PlayerTileConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(PlayerTileConfig.class);
    }

    @Override
    protected void startUp() throws Exception
    {
        overlayManager.add(overlay);
    }

    @Override
    protected void shutDown() throws Exception
    {
        overlayManager.remove(overlay);
    }


}
