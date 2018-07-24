package uk.co.harieo.seasons;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import uk.co.harieo.seasons.commands.ChangeCommand;
import uk.co.harieo.seasons.commands.SeasonsCommand;
import uk.co.harieo.seasons.configuration.SeasonsConfig;
import uk.co.harieo.seasons.configuration.SeasonsWorlds;
import uk.co.harieo.seasons.effects.bad.*;
import uk.co.harieo.seasons.effects.good.*;
import uk.co.harieo.seasons.models.Cycle;
import uk.co.harieo.seasons.models.effect.Effect;

public class Seasons extends JavaPlugin {

	public static final String PREFIX =
			ChatColor.GOLD + ChatColor.BOLD.toString() + "Seasons" + ChatColor.GRAY + "∙ " + ChatColor.RESET;
	public static final Random RANDOM = new Random();

	private static FileConfiguration CONFIG;
	private static SeasonsWorlds WORLD_HANDLER;
	private static JavaPlugin INSTANCE;
	private static final List<Effect> EFFECTS = new ArrayList<>();

	@Override
	public void onEnable() {
		INSTANCE = this;

		saveDefaultConfig();
		CONFIG = getConfig();
		new SeasonsConfig(CONFIG); // Load settings
		WORLD_HANDLER = new SeasonsWorlds(CONFIG); // Load saved worlds

		new WorldTicker().runTaskTimer(this, 0, 20); // Begin the cycles

		ChangeCommand changeCommand = new ChangeCommand();
		Bukkit.getPluginCommand("season").setExecutor(new SeasonsCommand());
		Bukkit.getPluginCommand("changeday").setExecutor(changeCommand);
		Bukkit.getPluginCommand("changeweather").setExecutor(changeCommand);
		Bukkit.getPluginCommand("changeseason").setExecutor(changeCommand);

		// Register effects
		addEffects(new Devastation(), new FeelsGood(), new FluffyCoat(), new Frostbite(), new HoldOntoYourHat(),
				new HotSand(), new Icy(), new PrimitiveHeating(), new Revitalized(), new SolderingIron(),
				new StrongCurrent(), new Sweating(), new TheShivers(), new WarmingStew(), new WetMud(),
				new WindInYourBoots());
	}

	@Override
	public void onDisable() {
		WORLD_HANDLER.saveAllWorlds();
	}

	private void addEffects(Effect... effects) {
		EFFECTS.addAll(Arrays.asList(effects));
	}

	public static JavaPlugin getPlugin() {
		return INSTANCE;
	}

	public static FileConfiguration getSeasonsConfig() {
		return CONFIG;
	}

	public static List<Cycle> getCycles() {
		return WORLD_HANDLER.getParsedCycles();
	}

	/**
	 * Retrieves the {@link Cycle} specific to a specified world
	 *
	 * @param world to find the cycle of
	 * @return the {@link Cycle} instance or null if none exists
	 */
	public static Cycle getWorldCycle(World world) {
		for (Cycle cycle : WORLD_HANDLER.getParsedCycles()) {
			if (cycle.getWorld().equals(world)) {
				return cycle;
			}
		}

		return null;
	}

	public static List<Effect> getEffects() {
		return EFFECTS;
	}
}
