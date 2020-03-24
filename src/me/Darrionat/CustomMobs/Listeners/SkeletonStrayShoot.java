package me.Darrionat.CustomMobs.Listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.Vector;

import me.Darrionat.CustomMobs.Main;
import me.Darrionat.CustomMobs.Utils.Utils;

public class SkeletonStrayShoot implements Listener {
	private Main plugin;

	public SkeletonStrayShoot(Main plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onShoot(EntityShootBowEvent e) {
		FileConfiguration config = plugin.getConfig();
		LivingEntity entity = e.getEntity();
		String entityType = entity.getType().toString();
		if (config.getConfigurationSection(entityType) == null) {
			return;
		}
		if (entity.getType() != EntityType.SKELETON && entity.getType() != EntityType.STRAY) {
			return;
		}
		List<LivingEntity> particleControl = Utils.getParticleList();
		if (!particleControl.contains(entity)) {
			return;
		}
		Arrow arrow = (Arrow) e.getProjectile();
		Vector direction = entity.getLocation().getDirection();
		direction.multiply(config.getDouble("SKELETON.arrowSpeedMultiplier"));
		arrow.setVelocity(direction);
	}
}
