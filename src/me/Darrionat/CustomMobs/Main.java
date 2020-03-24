package me.Darrionat.CustomMobs;

import org.bukkit.plugin.java.JavaPlugin;

import me.Darrionat.CustomMobs.Listeners.EndermanHit;
import me.Darrionat.CustomMobs.Listeners.EntityDamage;
import me.Darrionat.CustomMobs.Listeners.EntityDeath;
import me.Darrionat.CustomMobs.Listeners.EntitySpawn;
import me.Darrionat.CustomMobs.Listeners.GiantHit;
import me.Darrionat.CustomMobs.Listeners.MagmaCubeHit;
import me.Darrionat.CustomMobs.Listeners.SkeletonStrayShoot;
import me.Darrionat.CustomMobs.Listeners.SlimeHit;
import me.Darrionat.CustomMobs.Listeners.SpiderHit;
import me.Darrionat.CustomMobs.Listeners.ZombieHit;
import me.Darrionat.CustomMobs.Utils.GiantAI;
import me.Darrionat.CustomMobs.Utils.Utils;

public class Main extends JavaPlugin {

	public void onEnable() {
		new EntityDamage(this);
		new EntitySpawn(this);
		new EntityDeath(this);
		new EndermanHit(this);
		new GiantHit(this);
		new MagmaCubeHit(this);
		new SkeletonStrayShoot(this);
		new SlimeHit(this);
		new SpiderHit(this);
		new ZombieHit(this);
		new GiantAI(this);
		GiantAI giantTimer = new GiantAI(this);
		giantTimer.startTimer();
		giantTimer.giantHitTimerTimer();
		Utils.ParticleControl(this);
		saveDefaultConfig();
	}

	public void onDisable() {

	}

}
