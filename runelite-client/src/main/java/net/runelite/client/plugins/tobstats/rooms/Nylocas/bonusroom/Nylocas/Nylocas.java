package net.runelite.client.plugins.tobstats.rooms.Nylocas.bonusroom.Nylocas;

import java.awt.Color;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.MenuEntry;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.Skill;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ClientTick;
import net.runelite.client.events.ConfigChanged;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOpened;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.input.MouseManager;

import net.runelite.client.plugins.tobstats.rooms.Nylocas.bonusroom.Room;
import net.runelite.client.plugins.tobstats.TobstatsPlugin;
import net.runelite.client.plugins.tobstats.TobstatsConfig;



import net.runelite.client.ui.overlay.components.InfoBoxComponent;
import net.runelite.client.util.ColorUtil;

public class Nylocas extends Room
{
	private static final int NPCID_NYLOCAS_PILLAR = 8358;
	private static final int NYLO_MAP_REGION = 13122;
	private static final int BLOAT_MAP_REGION = 13125;

	@Setter
	@Getter
	private static Runnable wave31Callback = null;
	@Setter
	@Getter
	private static Runnable endOfWavesCallback = null;

	@Inject
	private SkillIconManager skillIconManager;
	//@Inject
	//private MouseManager mouseManager;
	//@Inject
	//private TheatreInputListener theatreInputListener;
	@Inject
	private Client client;
	//@Inject
	//private NylocasOverlay nylocasOverlay;
	//@Inject
	//private NylocasAliveCounterOverlay nylocasAliveCounterOverlay;

	@Getter
	private boolean nyloActive;
	private boolean nyloBossAlive;
	private int nyloWave = 0;
	private int varbit6447 = -1;
	@Getter
	private Instant nyloWaveStart;
	//@Getter
	//private NyloSelectionManager nyloSelectionManager;
	@Getter
	private HashMap<NPC, Integer> nylocasPillars = new HashMap<>();
	@Getter
	private HashMap<NPC, Integer> nylocasNpcs = new HashMap<>();
	//@Getter
	//private HashSet<NPC> aggressiveNylocas = new HashSet<>();
	private HashMap<NyloNPC, NPC> currentWave = new HashMap<>();
	private int ticksSinceLastWave = 0;

	@Getter
	private int instanceTimer = 0;
	@Getter
	private boolean isInstanceTimerRunning = false;
	private boolean nextInstance = true;

	@Inject
	protected Nylocas(TobstatsPlugin plugin, TobstatsConfig config)
	{
		super(plugin, config);
	}

	@Override
	public void init()
	{
		/*InfoBoxComponent box = new InfoBoxComponent();
		box.setImage(skillIconManager.getSkillImage(Skill.ATTACK));
		NyloSelectionBox nyloMeleeOverlay = new NyloSelectionBox(box);
		nyloMeleeOverlay.setSelected(config.getHighlightMeleeNylo());

		box = new InfoBoxComponent();
		box.setImage(skillIconManager.getSkillImage(Skill.MAGIC));
		NyloSelectionBox nyloMageOverlay = new NyloSelectionBox(box);
		nyloMageOverlay.setSelected(config.getHighlightMageNylo());

		box = new InfoBoxComponent();
		box.setImage(skillIconManager.getSkillImage(Skill.RANGED));
		NyloSelectionBox nyloRangeOverlay = new NyloSelectionBox(box);
		nyloRangeOverlay.setSelected(config.getHighlightRangeNylo());

		nyloSelectionManager = new NyloSelectionManager(nyloMeleeOverlay, nyloMageOverlay, nyloRangeOverlay);
		nyloSelectionManager.setHidden(!config.nyloOverlay());
		nylocasAliveCounterOverlay.setHidden(!config.nyloAlivePanel());
		nylocasAliveCounterOverlay.setNyloAlive(0);
		nylocasAliveCounterOverlay.setMaxNyloAlive(12);*/

		nyloBossAlive = false;
	}

	private void startupNyloOverlay()
	{
		/*mouseManager.registerMouseListener(theatreInputListener);

		if (nyloSelectionManager != null)
		{
			overlayManager.add(nyloSelectionManager);
			nyloSelectionManager.setHidden(!config.nyloOverlay());
		}

		if (nylocasAliveCounterOverlay != null)
		{
			overlayManager.add(nylocasAliveCounterOverlay);
			nylocasAliveCounterOverlay.setHidden(!config.nyloAggressiveOverlay());
		}*/
	}

	private void shutdownNyloOverlay()
	{
		/*mouseManager.unregisterMouseListener(theatreInputListener);

		if (nyloSelectionManager != null)
		{
			overlayManager.remove(nyloSelectionManager);
			nyloSelectionManager.setHidden(true);
		}

		if (nylocasAliveCounterOverlay != null)
		{
			overlayManager.remove(nylocasAliveCounterOverlay);
			nylocasAliveCounterOverlay.setHidden(true);
		}*/
	}

	public void load()
	{
		//overlayManager.add(nylocasOverlay);
	}

	public void unload()
	{
		//overlayManager.remove(nylocasOverlay);

		//shutdownNyloOverlay();
		nyloBossAlive = false;
		nyloWaveStart = null;
	}

	private void resetNylo()
	{
		nyloBossAlive = false;
		nylocasPillars.clear();
		nylocasNpcs.clear();
		//aggressiveNylocas.clear();
		setNyloWave(0);
		currentWave.clear();
	}

	private void setNyloWave(int wave)
	{
		nyloWave = wave;

		if (wave == NylocasWave.MAX_WAVE && wave31Callback != null)
		{
			wave31Callback.run();
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged change)
	{
		/*if (change.getKey().equals("nyloOverlay"))
		{
			nyloSelectionManager.setHidden(!config.nyloOverlay());
		}
		if (change.getKey().equals("nyloAliveCounter"))
		{
			nylocasAliveCounterOverlay.setHidden(!config.nyloAlivePanel());
		}*/
	}

	@Subscribe
	public void onNpcSpawned(NpcSpawned npcSpawned)
	{
		NPC npc = npcSpawned.getNpc();
		switch (npc.getId())
		{
			case NPCID_NYLOCAS_PILLAR:
				nyloActive = true;
				if (nylocasPillars.size() > 3)
				{
					nylocasPillars.clear();
				}
				if (!nylocasPillars.keySet().contains(npc))
				{
					nylocasPillars.put(npc, 100);
				}
				break;
			case 8342: case 8343: case 8344: case 8345: case 8346: case 8347:
			case 8348: case 8349: case 8350: case 8351: case 8352: case 8353:
				if (nyloActive)
				{
					nylocasNpcs.put(npc, 52);
					//nylocasAliveCounterOverlay.setNyloAlive(nylocasNpcs.size());

					NyloNPC nyloNPC = matchNpc(npc);
					if (nyloNPC != null)
					{
						currentWave.put(nyloNPC, npc);
						if (currentWave.size() > 2)
						{
							matchWave();
						}
					}
				}
				break;
			case NpcID.NYLOCAS_VASILIAS:
			case NpcID.NYLOCAS_VASILIAS_8355:
			case NpcID.NYLOCAS_VASILIAS_8356:
			case NpcID.NYLOCAS_VASILIAS_8357:
				nyloBossAlive = true;
				isInstanceTimerRunning = false;
				break;
		}
	}

	private void matchWave()
	{
		HashSet<NyloNPC> potentialWave;
		Set<NyloNPC> currentWaveKeySet = currentWave.keySet();
		for (int wave = nyloWave + 1; wave <= NylocasWave.MAX_WAVE; wave++)
		{
			boolean matched = true;
			potentialWave = NylocasWave.waves.get(wave).getWaveData();
			for (NyloNPC nyloNpc : potentialWave)
			{
				if (!currentWaveKeySet.contains(nyloNpc))
				{
					matched = false;
					break;
				}
			}

			if (matched)
			{
				setNyloWave(wave);
				for (NyloNPC nyloNPC : potentialWave)
				{
					/*if (nyloNPC.isAggressive())
					{
						aggressiveNylocas.add(currentWave.get(nyloNPC));
					}*/
				}
				currentWave.clear();
				return;
			}
		}
	}

	private NyloNPC matchNpc(NPC npc)
	{
		WorldPoint p = WorldPoint.fromLocalInstance(client, npc.getLocalLocation());
		Point point = new Point(p.getRegionX(), p.getRegionY());
		NylocasSpawnPoint spawnPoint = NylocasSpawnPoint.getLookupMap().get(point);
		if (spawnPoint == null)
		{
			return null;
		}
		NylocasType nylocasType = NylocasType.getLookupMap().get(npc.getId());
		if (nylocasType == null)
		{
			return null;
		}
		return new NyloNPC(nylocasType, spawnPoint);
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned npcDespawned)
	{
		NPC npc = npcDespawned.getNpc();
		switch (npc.getId())
		{
			case NPCID_NYLOCAS_PILLAR:
				if (nylocasPillars.keySet().contains(npc))
				{
					nylocasPillars.remove(npc);
				}
				if (nylocasPillars.size() < 1)
				{
					nyloWaveStart = null;
					nyloActive = false;
				}
				break;
			case 8342: case 8343: case 8344: case 8345: case 8346: case 8347:
			case 8348: case 8349: case 8350: case 8351: case 8352: case 8353:
				if (nylocasNpcs.remove(npc) != null)
				{
					//nylocasAliveCounterOverlay.setNyloAlive(nylocasNpcs.size());
				}
				//aggressiveNylocas.remove(npc);
				if (nyloWave == NylocasWave.MAX_WAVE && nylocasNpcs.size() == 0 && endOfWavesCallback != null)
				{
					endOfWavesCallback.run();
				}
				break;
			case NpcID.NYLOCAS_VASILIAS:
			case NpcID.NYLOCAS_VASILIAS_8355:
			case NpcID.NYLOCAS_VASILIAS_8356:
			case NpcID.NYLOCAS_VASILIAS_8357:
				nyloBossAlive = false;
				break;
		}
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		int[] varps = client.getVarps();
		int newVarbit6447 = client.getVarbitValue(varps, 6447);
		if (isInNyloRegion() && newVarbit6447 != 0 && newVarbit6447 != varbit6447)
		{
			nyloWaveStart = Instant.now();
			/*if (nylocasAliveCounterOverlay != null)
			{
				nylocasAliveCounterOverlay.setNyloWaveStart(nyloWaveStart);
			}*/
		}

		varbit6447 = newVarbit6447;
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}

		if (isInNyloRegion())
		{
			startupNyloOverlay();
		}
		else
		{
			/*if (!nyloSelectionManager.isHidden())
			{
				shutdownNyloOverlay();
				resetNylo();
			}*/

			isInstanceTimerRunning = false;
		}

		nextInstance = true;
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (nyloActive)
		{
			for (Iterator<NPC> it = nylocasNpcs.keySet().iterator(); it.hasNext();)
			{
				NPC npc = it.next();
				int ticksLeft = nylocasNpcs.get(npc);

				if (ticksLeft < 0)
				{
					it.remove();
					continue;
				}
				nylocasNpcs.replace(npc, ticksLeft - 1);
			}

			for (NPC pillar : nylocasPillars.keySet())
			{
				int healthPercent = pillar.getHealthRatio();
				if (healthPercent > -1)
				{
					nylocasPillars.replace(pillar, healthPercent);
				}
			}

			/*if (config.nyloStallMessage() && (instanceTimer + 1) % 4 == 1 && nyloWave < NylocasWave.MAX_WAVE && ticksSinceLastWave < 2)
			{
				if (nylocasAliveCounterOverlay.getNyloAlive() >= nylocasAliveCounterOverlay.getMaxNyloAlive())
				{
					client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Stalled wave <col=EF1020>" +
							nyloWave + "/" + NylocasWave.MAX_WAVE + " <col=00>Time:<col=EF1020> " + nylocasAliveCounterOverlay.getFormattedTime() +
							" <col=00>Nylos alive<col=EF1020> " + nylocasAliveCounterOverlay.getNyloAlive() + "/" + nylocasAliveCounterOverlay.getMaxNyloAlive(), "");
				}
			}*/

			ticksSinceLastWave = Math.max(0, ticksSinceLastWave - 1);
		}
		instanceTimer = (instanceTimer + 1) % 4;
	}

	@Subscribe
	protected void onClientTick(ClientTick event)
	{
		/*List<Player> players = client.getPlayers();
		for (Player player : players)
		{
			if (player.getWorldLocation() != null)
			{
				LocalPoint lp = player.getLocalLocation();

				WorldPoint wp = WorldPoint.fromRegion(player.getWorldLocation().getRegionID(),5,33,0);
				LocalPoint lp1 = LocalPoint.fromWorld(client, wp.getX(), wp.getY());
				if (lp1 != null)
				{
					Point base = new Point(lp1.getSceneX(), lp1.getSceneY());
					Point point = new Point(lp.getSceneX() - base.getX(), lp.getSceneY() - base.getY());

					if (isInBloatRegion() && point.getX() == -1 && (point.getY() == -1 || point.getY() == -2 || point.getY() == -3) && nextInstance)
					{
						client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Nylo instance timer started", "");
						instanceTimer = 3;
						isInstanceTimerRunning = true;
						nextInstance = false;
					}
				}
			}
		}*/
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded entry)
	{
		if (!nyloActive)
		{
			return;
		}

		/*if (config.nyloRecolorMenu() && entry.getOption().equals("Attack"))
		{
			MenuEntry[] entries = client.getMenuEntries();
			MenuEntry toEdit = entries[entries.length - 1];

			String target = entry.getTarget();
			String strippedTarget = "";
			int idx = target.indexOf('>');
			if (idx != -1)
			{
				target = target.substring(idx + 1);
				idx = target.indexOf('<');
				strippedTarget = target.substring(0, idx);
			}

			switch (strippedTarget)
			{
				case "Nylocas Hagios":
					toEdit.setTarget(ColorUtil.prependColorTag(strippedTarget, Color.CYAN));
					break;
				case "Nylocas Ischyros":
					toEdit.setTarget(ColorUtil.prependColorTag(strippedTarget, new Color(255, 188, 188)));
					break;
				case "Nylocas Toxobolos":
					toEdit.setTarget(ColorUtil.prependColorTag(strippedTarget, Color.GREEN));
					break;
			}
			client.setMenuEntries(entries);
		}*/
	}

	@Subscribe
	public void onMenuOpened(MenuOpened menu)
	{
		/*if (!config.nyloRecolorMenu() || !nyloActive || nyloBossAlive)
		{
			return;
		}

		// filter all entries with examine
		client.setMenuEntries(Arrays.stream(menu.getMenuEntries()).filter(s -> !s.getOption().equals("Examine")).toArray(MenuEntry[]::new));
	*/}

	boolean isInNyloRegion()
	{
		return client.isInInstancedRegion() && client.getMapRegions().length > 0 && client.getMapRegions()[0] == NYLO_MAP_REGION;
	}

	private boolean isInBloatRegion()
	{
		return client.isInInstancedRegion() && client.getMapRegions().length > 0 && client.getMapRegions()[0] == BLOAT_MAP_REGION;
	}


}