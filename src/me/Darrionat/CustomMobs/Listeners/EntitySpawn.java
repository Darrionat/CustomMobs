package me.Darrionat.CustomMobs.Listeners;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import me.Darrionat.CustomMobs.Main;
import me.Darrionat.CustomMobs.Utils.Utils;

public class EntitySpawn implements Listener {
	private Main plugin;

	public EntitySpawn(Main plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onSpawn(CreatureSpawnEvent e) {
		FileConfiguration config = plugin.getConfig();
		LivingEntity entity = e.getEntity();
		String entityType = entity.getType().toString();

		if (e.getSpawnReason() == SpawnReason.SPAWNER && config.getBoolean("SpawnerCustomMobs") == false) {
			return;
		}

		if (config.getConfigurationSection(entityType) == null) {
			return;
		}

		int min = 1;
		int max = 100;
		Random r = new Random();
		int i = r.nextInt(max - min + 1) + min;
		int percent = config.getInt("CustomMobPercent");
		if (i > percent) {
			return;
		}
		if (e.getSpawnReason() != SpawnReason.SPAWNER && e.getSpawnReason() != SpawnReason.SPAWNER_EGG
				&& entityType.equalsIgnoreCase("ZOMBIE")) {
			// Add tag/or something that labels them to give double drops when they die

			if (!entity.getNearbyEntities(2, 2, 2).isEmpty()) {
				return;
			}
			Location loc = entity.getLocation();

			for (int blockCount = 1; blockCount <= 12; blockCount++) {
				loc.add(0, 1, 0);
				Block b = loc.getBlock();
				if (b.getType() != Material.AIR) {
					return;
				}
			}
			loc.add(0, 1, 0);
			int min1 = 1;
			int max1 = 100;
			Random r1 = new Random();
			int i1 = r1.nextInt(max1 - min1 + 1) + min1;
			double percent1 = max1 - config.getDouble("GIANT.spawnChancePercent");
			if (i1 > percent1) {
				Entity giant = entity.getWorld().spawnEntity(entity.getLocation(), EntityType.GIANT);
				entity.remove();
				entity = (LivingEntity) giant;

			}
		}
		AttributeInstance speedInstance = entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
		double speedValue = speedInstance.getValue();
		double speedMultiplier = config.getDouble(entityType + ".speedMultiplier");
		speedInstance.setBaseValue(speedValue * speedMultiplier);
		List<LivingEntity> particleControl = Utils.getParticleList();
		particleControl.add(entity);
		if (entity.getType() == EntityType.GHAST) {
			// Normal heatlh is 10
			entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
		}

	}

}