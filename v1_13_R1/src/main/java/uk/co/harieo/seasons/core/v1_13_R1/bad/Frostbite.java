package uk.co.harieo.seasons.core.v1_13_R1.bad;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import uk.co.harieo.seasons.plugin.Seasons;
import uk.co.harieo.seasons.plugin.models.Cycle;
import uk.co.harieo.seasons.plugin.models.Weather;
import uk.co.harieo.seasons.plugin.models.effect.Effect;
import uk.co.harieo.seasons.plugin.models.effect.TickableEffect;

public class Frostbite extends Effect implements TickableEffect {

	public Frostbite() {
		super("Donma", "Eğer bütün zırhlarını kuşanmamışsan 10 saniyede bir kere hasar alırsın",
				Arrays.asList(Weather.FREEZING, Weather.SNOWY), false);
		setIgnoreRoof(false);
	}

	@Override
	public String getId() {
		return "frostbite";
	}

	private int secondsPast = 0;
	private boolean active = false; // Allows grace period when frostbite first turns on

	@Override
	public void onTrigger(World world) {
		secondsPast = 0;
	}

	/**
	 * Damages all players in a world if they are missing any armour
	 *
	 * @param world to damage players in
	 */
	private void damage(World world) {
		for (Player player : world.getPlayers()) {
			if (isPlayerCycleApplicable(player) && player.getHealth() > 1) {
				List<ItemStack> armour = Arrays.asList(player.getInventory().getArmorContents());
				if (armour.contains(null)) {
					player.damage(1);
				}
			}
		}
	}

	@Override
	public void onTick(Cycle cycle) {
		if (active) {
			if (secondsPast >= Seasons.getInstance().getSeasonsConfig().getSecondsPerDamage()) {
				damage(cycle.getWorld());
				secondsPast = 0;
			} else {
				secondsPast++;
			}
		} else {
			if (secondsPast >= 10) {
				active = true;
				secondsPast = 0;
			} else {
				secondsPast++;
			}
		}
	}
}
