package uk.co.harieo.seasons.core.v1_13_R1.good;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import uk.co.harieo.seasons.plugin.Seasons;
import uk.co.harieo.seasons.plugin.models.Weather;
import uk.co.harieo.seasons.plugin.models.effect.Effect;

public class WarmingStew extends Effect {

	private static final Material[] STEWS = {Material.BEETROOT_SOUP, Material.MUSHROOM_STEW, Material.RABBIT_STEW};

	public WarmingStew() {
		super("Ilık Bir Çorba", "Çorba içtiğin zaman 10 saniye boyunca Yenilenme 1 kazanırsın",
				Collections.singletonList(Weather.COLD), true);
	}

	@Override
	public String getId() {
		return "warming-stew";
	}

	@Override
	public void onTrigger(World world) { }

	@EventHandler
	public void onConsume(PlayerItemConsumeEvent event) {
		Player player = event.getPlayer();
		if (isEnabled()) {
			for (Material material : STEWS) {
				if (event.getItem().getType() == material) {
					player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 10, 0));
					sendGiveMessage(player, ChatColor.GREEN
							+ "That hit the spot, the delicious stew makes your wounds a little more bearable...");
				}
			}
		}
	}

}
