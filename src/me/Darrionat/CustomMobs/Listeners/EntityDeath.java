package me.Darrionat.CustomMobs.Listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftZombie;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Darrionat.CustomMobs.Main;
import me.Darrionat.CustomMobs.Utils.Utils;

public class EntityDeath implements Listener {
	private Main plugin;

	public EntityDeath(Main plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		// If custom mob, potential to drop paper
		// Double drops as well
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
		particleControl.remove(entity); // Not sure if this has any effect?
		if (config.getBoolean("DoubleDrops")) {
			ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
			drops.addAll(e.getDrops());
			e.getDrops().addAll(drops);
		}
		if (config.getBoolean("CustomPaper.Enabled") == false) {
			return;
		}

		if (entity.getType() != EntityType.GIANT) {
			int min = 1;
			int max = 100;
			Random r = new Random();
			int i = r.nextInt(max - min + 1) + min;
			int percent = config.getInt("CustomPaper.Percent");
			if (i > percent) {
				return;
			}
		}

		// Custom Paper
		ItemStack paper = new ItemStack(Material.PAPER, 1);
		ItemMeta meta = paper.getItemMeta();
		List<String> lore = new ArrayList<String>();
		meta.setDisplayName(Utils.chat(config.getString("CustomPaper.Name")));
		for (String line : config.getStringList("CustomPaper.Lore")) {
			lore.add(Utils.chat(line));
		}
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setLore(lore);
		paper.setItemMeta(meta);
		if (config.getBoolean("CustomPaper.Glowing")) {
			paper.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
		}
		e.getDrops().add(paper);

		if (entity.getType() == EntityType.GIANT) {
			ItemStack dragonEgg = new ItemStack(Material.DRAGON_EGG, 1);
			ItemMeta eggMeta = dragonEgg.getItemMeta();
			List<String> eggLore = new ArrayList<String>();
			eggMeta.setDisplayName(Utils.chat(config.getString("DragonEgg.Name")));
			for (String line : config.getStringList("DragonEgg.Lore")) {
				eggLore.add(Utils.chat(line));
			}
			eggMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			eggMeta.setLore(eggLore);
			dragonEgg.setItemMeta(eggMeta);
			if (config.getBoolean("DragonEgg.Glowing")) {
				dragonEgg.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
			}
			e.getDrops().add(dragonEgg);
			spawnBabyZombies(e.getEntity().getLocation());

		}
	}

	public void spawnBabyZombies(Location loc) {
		FileConfiguration config = plugin.getConfig();
		if (!config.getBoolean("BabiesFromGiant")) {
			return;
		}
		for (int i = 1; i <= 10; i++) {
			CraftZombie zombie = (CraftZombie) loc.getWorld().spawn(loc, Zombie.class);
			zombie.setBaby(true);
		}
		

	}
}
