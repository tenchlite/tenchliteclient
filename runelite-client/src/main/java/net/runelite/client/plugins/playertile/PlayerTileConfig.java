package net.runelite.client.plugins.playertile;

import java.awt.Color;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("playertile")
public interface PlayerTileConfig extends Config
{
    @Alpha
    @ConfigItem(
            keyName = "highlightCurrentColor",
            name = "Color of current tile",
            description = "Configures the highlight color of current tile"
    )
    default Color highlightTileColor()
    {
        return Color.GRAY;
    }

    @ConfigItem(
            keyName = "highlightStandingTile",
            name = "Highlight if standing",
            description = "Highlights tile if not moving"
    )
    default boolean highlightStandingTile()
    {
        return true;
    }

    @Alpha
    @ConfigItem(
            keyName = "adjustOpacity",
            name = "Opacity Fill",
            description = "Configure opacity of inner tile"
    )
    default int transparency() { return 50; }
}