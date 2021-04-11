package uk.co.harieo.seasons.core.v1_13_R1.bad;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import uk.co.harieo.seasons.plugin.Seasons;
import uk.co.harieo.seasons.plugin.models.Cycle;
import uk.co.harieo.seasons.plugin.models.Weather;
import uk.co.harieo.seasons.plugin.models.effect.Effect;
import uk.co.harieo.seasons.plugin.models.effect.TickableEffect;

public class SolderingIron extends Effect implements TickableEffect {

	private static final Material[] SOLDERING_ITEMS = {Material.BUCKET, Material.IRON_INGOT, Material.IRON_BLOCK,
			Material.IRON_DOOR, Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS,
			Material.IRON_BOOTS, Material.ANVIL, Material.IRON_NUGGET, Material.IRON_BARS, Material.IRON_TRAPDOOR,
			Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS,
			Material.CHAINMAIL_BOOTS, Material.WATER_BUCKET, Material.LAVA_BUCKET};

	private Map<Player, Integer> secondsPast = new HashMap<>();

	public SolderingIron() {
		super("Aşırı Sıcak", "Elinde demir bir eşya tutarsan hasar alırsın",
				Collections.singletonList(Weather.SCORCHING), false);
	}

	@Override
	public String getId() {
		return "soldering-iron";
	}

	/**
	 * Checks the items in the Player's hands to see if they contain any soldering items If an item is found, they will
	 * be marked to take damage and visa versa
	 *
	 * @param player to be checked
	 */
	private void checkHotbar(Player player) {
		PlayerInventory inventory = player.getInventory();
		boolean containsKey = secondsPast.containsKey(player);

		for (Material material : SOLDERING_ITEMS) {
			boolean containsMaterial = inventory.getItemInMainHand().getType() == material
					|| inventory.getItemInOffHand().getType() == material;
			if (containsMaterial && !containsKey) { // They are holding a soldering item and weren't last time checked
				secondsPast.put(player, 0);
				sendGiveMessage(player, ChatColor.RED + "The iron is soldering hot, don't hold it for too long!");
			}
		}

		if (containsKey) { // They were holding no soldering items but were last time a check was done
			secondsPast.remove(player);
		}
	}

	/**
	 * Delayed call of {@link #checkHotbar(Player)} for events that occur before the fact
	 *
	 * @param player to call the method for
	 */
	private void delayedCheckHotbar(Player player) {
		BukkitRunnable runnable = new BukkitRunnable() {
			@Override
			public void run() {
				checkHotbar(player);
			}
		};
		runnable.runTaskLater(Seasons.getInstance().getPlugin(), 10);
	}

	@Override
	public void onTrigger(World world) {
		secondsPast.clear();
		for (Player player : world.getPlayers()) {
			checkHotbar(player);
		}
	}

	@Override
	public void onTick(Cycle cycle) {
		for (Player player : secondsPast.keySet()) {
			int seconds = secondsPast.get(player);
			if (seconds >= 5) {
				player.damage(1);
			} else {
				secondsPast.replace(player, seconds + 1);
			}
		}
	}

	@EventHandler
	public void onHotbarSwitch(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();
		if (isPlayerCycleApplicable(player)) {
			delayedCheckHotbar(player);
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player) {
			Player player = (Player) event.getWhoClicked();
			if (isPlayerCycleApplicable(player)) {
				delayedCheckHotbar(player);
			}
		}
	}

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (isPlayerCycleApplicable(player)) {
			delayedCheckHotbar(player);
		}
	}

	@EventHandler
	public void onItemPickup(EntityPickupItemEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (isPlayerCycleApplicable(player)) {
				delayedCheckHotbar(player);
			}
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		secondsPast.remove(event.getPlayer());
	}

}
