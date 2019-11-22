package net.runelite.client.plugins.tobstats.rooms.Nylocas.bonusroom;

import javax.inject.Inject;
import javax.inject.Singleton;
//import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.plugins.tobstats.TobstatsPlugin;
import net.runelite.client.plugins.tobstats.TobstatsConfig;

@Singleton
public abstract class Room
{
	protected final TobstatsPlugin plugin;
	protected final TobstatsConfig config;

	/*@Inject
	protected OverlayManager overlayManager;
	 //not adding overlay in this class in load because not every room should always have an overlay
	*/
	@Inject
	protected Room(TobstatsPlugin plugin, TobstatsConfig config)
	{

		this.plugin = plugin;
		this.config = config;
	}

	public void init()
	{
	}

	public void load()
	{
	}

	public void unload()
	{
	}
}