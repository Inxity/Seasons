package uk.co.harieo.seasons.core.v1_13_R1.good;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import uk.co.harieo.seasons.plugin.models.Weather;
import uk.co.harieo.seasons.plugin.models.effect.SeasonsPotionEffect;

public class FeelsGood extends SeasonsPotionEffect {

	public FeelsGood() {
		super("Özgürlük!", "Zırh giymiyorken Hız 2 kazanırsın",
				Collections.singletonList(Weather.WARM), true,
				new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
	}

	@Override
	public String getId() {
		return "feels-good";
	}

	public boolean shouldGive(Player player) {
		if (isPlayerCycleApplicable(player)) {
			PlayerInventory inventory = player.getInventory();
			for (ItemStack armor : inventory.getArmorContents()) {
				if (armor != null) { // If player is wearing any form of armour
					return false;
				}
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	public void sendGiveMessage(Player player) {
		sendGiveMessage(player, ChatColor.GREEN
				+ "The sun on your skin without armour gives you energy, Feels Good!");
	}

	@Override
	public void sendRemoveMessage(Player player) {
		sendRemoveMessage(player, ChatColor.YELLOW
				+ "As you leave the world behind, the energising sunlight wears off...");
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player) {
			Player player = (Player) event.getWhoClicked();
			if (shouldGive(player)) {
				giveEffect(player, true);
			} else {
				removeEffect(player, false);
			}
		}
	}
}
