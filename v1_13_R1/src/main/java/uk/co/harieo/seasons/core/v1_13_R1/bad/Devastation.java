package uk.co.harieo.seasons.core.v1_13_R1.bad;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import uk.co.harieo.seasons.plugin.models.Weather;
import uk.co.harieo.seasons.plugin.models.effect.Effect;

public class Devastation extends Effect {

	public Devastation() {
		super("Yıkım", "Canını doğal yollarla yenileyemezsin, canını yakmamaya çalış!",
				Collections.singletonList(Weather.STORMY), false);
	}

	@Override
	public String getId() {
		return "devastation";
	}

	@Override
	public void onTrigger(World world) { }

	@EventHandler
	public void onRegeneration(EntityRegainHealthEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (isPlayerCycleApplicable(player) && !player.hasPotionEffect(PotionEffectType.REGENERATION)) {
				event.setCancelled(true);
			}
		}
	}
}
