package me.Darrionat.CustomMobs.Listeners;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Darrionat.CustomMobs.Main;
import me.Darrionat.CustomMobs.Utils.Utils;

public class GiantHit implements Listener {
	private Main plugin;

	public GiantHit(Main plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onHit(EntityDamageByEntityEvent e) {
		FileConfiguration config = plugin.getConfig();
		if (!(e.getDamager() instanceof LivingEntity)) {
			return;
		}
		LivingEntity entity = (LivingEntity) e.getDamager();
		String entityType = entity.getType().toString();
		if (config.getConfigurationSection(entityType) == null) {
			return;
		}
		if (entity.getType() != EntityType.GIANT) {
			return;
		}
		List<LivingEntity> particleControl = Utils.getParticleList();
		if (!particleControl.contains(entity)) {
			return;
		}
		int min = 1;
		int max = 100;
		Random r = new Random();
		int i = r.nextInt(max - min + 1) + min;
		int percent = config.getInt(entityType + ".weaknessPercent");
		if (i > percent) {
			return;
		}
		((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,  5 * 20, 1));
	}
}
