package me.Darrionat.CustomMobs.Utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitScheduler;

import me.Darrionat.CustomMobs.Main;
import net.minecraft.server.v1_15_R1.EntityInsentient;

public class GiantAI implements Listener {
	private Main plugin;

	public GiantAI(Main plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	public void startTimer() {
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					World world = p.getWorld();
					for (Entity entity : world.getEntities()) {
						if (entity.getType() != EntityType.GIANT) {
							continue;
						}
						List<LivingEntity> particleControl = Utils.getParticleList();
						if (!particleControl.contains(entity)) {
							continue;
						}

						for (Entity nearby : entity.getNearbyEntities(25, 25, 25)) {

							if (nearby instanceof Villager) {
								followPlayer((LivingEntity) nearby, (LivingEntity) entity);
								break;
							}
							if (nearby instanceof Player) {
								if (p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR) {
									continue;
								}
								followPlayer((LivingEntity) nearby, (LivingEntity) entity);
								break;
							}
							continue;
						}
					}
				}
			}
		}, 0L, 20L);
	}

	@EventHandler
	public void onHit(EntityDamageByEntityEvent e) {
		Entity entity = e.getEntity();
		Entity damager = e.getDamager();
		if (entity.getType() != EntityType.GIANT) {
			return;
		}
		List<LivingEntity> particleControl = Utils.getParticleList();
		if (!particleControl.contains(entity)) {
			return;
		}
		if (!(damager instanceof LivingEntity)) {
			if (!(damager instanceof Arrow)) {
				return;
			}
			Arrow arrow = (Arrow) e.getDamager();
			if (arrow.getShooter() == null) {
				return;
			}
			if (!(arrow.getShooter() instanceof LivingEntity)) {
				return;
			}
			LivingEntity shooter = (LivingEntity) arrow.getShooter();
			if (shooter instanceof Player) {
				Player p = (Player) shooter;
				if (p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR) {
					return;
				}
			}
			followPlayer(shooter, (LivingEntity) entity);
			return;

		}
		if (damager instanceof Player) {
			Player p = (Player) damager;
			if (p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR) {
				return;
			}
		}

		followPlayer((LivingEntity) damager, (LivingEntity) entity);

	}

	public void followPlayer(LivingEntity followEntity, LivingEntity entity) {
		final LivingEntity e = entity;
		final LivingEntity p = followEntity;
		final float f = (float) 0.55;
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				if (entity.isDead()) {
					List<LivingEntity> particleControl = Utils.getParticleList();
					if (particleControl.contains(entity)) {
						particleControl.remove(entity);
					}
					return;
				}
				((EntityInsentient) ((CraftEntity) e).getHandle()).getNavigation().a(p.getLocation().getX(),
						p.getLocation().getY(), p.getLocation().getZ(), f);
			}
		}, 0 * 20, 10L);
	}

	public void giantHitTimerTimer() {

		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				for (World world : Bukkit.getServer().getWorlds()) {
					for (Entity entity : world.getEntities()) {
						if (entity.getType() != EntityType.GIANT) {
							continue;
						}
						List<LivingEntity> particleControl = Utils.getParticleList();
						if (!particleControl.contains(entity)) {
							continue;
						}
						for (Entity nearby : entity.getNearbyEntities(1, 15, 1)) {
							if (!(nearby instanceof LivingEntity)) {
								continue;
							}
							if (!(nearby instanceof Damageable)) {
								continue;
							}

							nearby = (LivingEntity) nearby;
							((Damageable) nearby).damage(plugin.getConfig().getDouble("GIANT.baseDamage")
									* plugin.getConfig().getDouble("GIANT.damageMultiplier"));
							break;

						}
					}
				}
			}
		}, 0, 20L);
	}

}
