package me.Darrionat.CustomMobs.Listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.Darrionat.CustomMobs.Main;
import me.Darrionat.CustomMobs.Utils.Utils;

public class EntityDamage implements Listener {
	private Main plugin;

	public EntityDamage(Main plugin) {
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
		List<LivingEntity> particleControl = Utils.getParticleList();
		if (!particleControl.contains(entity)) {
			return;
		}
		double damageMultiplier = config.getDouble(entityType + ".damageMultiplier");
		double damage = e.getDamage();
		double newDamage = damageMultiplier * damage;
		e.setDamage(newDamage);
	}

	@EventHandler
	public void onGiantHit(EntityDamageByEntityEvent e) {
		FileConfiguration config = plugin.getConfig();
		if (!(e.getEntity() instanceof LivingEntity)) {
			return;
		}
		LivingEntity entity = (LivingEntity) e.getEntity();
		String entityType = entity.getType().toString();
		if (config.getConfigurationSection(entityType) == null) {
			return;
		}
		List<LivingEntity> particleControl = Utils.getParticleList();
		if (!particleControl.contains(entity)) {
			return;
		}
		if (entity.getType() != EntityType.GIANT) {
			return;
		}
		Entity damager = e.getDamager();
		if (damager instanceof Projectile) {
			e.setCancelled(true);
		}

	}
}
