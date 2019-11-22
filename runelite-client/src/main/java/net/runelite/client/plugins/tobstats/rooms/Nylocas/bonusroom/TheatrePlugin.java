package net.runelite.client.plugins.tobstats.rooms.Nylocas.bonusroom;

import com.google.inject.Binder;
import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import net.runelite.client.plugins.tobstats.rooms.Nylocas.bonusroom.Nylocas.Nylocas;


/*@PluginDescriptor(
        name = "xz_Theatre",
        description = "All-in-one plugin for Theatre of Blood",
        tags = {"ToB"},
        enabledByDefault = false
)*/

public class TheatrePlugin extends Plugin
{
    private Room[] rooms = null;

    @Inject
    private EventBus eventBus;


    @Inject
    private Nylocas nylocas;


   /* @Override
    public void configure(Binder binder)
    {
        binder.bind(TheatreInputListener.class);
    }*/

    /*@Provides
    TheatreConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(TheatreConfig.class);
    }*/

    @Override
    protected void startUp()
    {
       if (rooms == null)
        {
            rooms = new Room[]{nylocas};
            for (Room room : rooms)
            {
                room.init();
            }
        }

        for(Room room : rooms)
        {
            room.load();
            eventBus.register(room);
        }
    }

    @Override
    protected void shutDown()
    {
        for(Room room : rooms)
        {
            eventBus.unregister(room);
            room.unload();
       }
    }
}