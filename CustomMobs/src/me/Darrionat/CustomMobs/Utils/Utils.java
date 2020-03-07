package me.Darrionat.CustomMobs.Utils;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Utils {

	public static String chat(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

	static List<LivingEntity> particleList = new ArrayList<LivingEntity>();

	public static List<LivingEntity> getParticleList() {
		return particleList;
	}

	@SuppressWarnings("deprecation")
	public static void ParticleControl(JavaPlugin plugin) {
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleAsyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				try {
					for (LivingEntity entity : particleList) {
						if (entity == null) {
							particleList.remove(entity);
							break;
						}
						if (entity.getHealth() == 0) {
							particleList.remove(entity);
							break;
						}
						if (entity.isDead()) {
							particleList.remove(entity);
							break;
						}

						Location loc = entity.getLocation();
						entity.getWorld().spawnParticle(Particle.SPELL_MOB, loc.add(0, 1.5, 0), 0, 1, 0, 0, 1);
					}
				} catch (ConcurrentModificationException exe) {
					return;
				}
			}
		}, 0L, 1L);
	}

}
