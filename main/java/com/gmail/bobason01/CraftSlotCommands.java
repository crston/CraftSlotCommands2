package com.gmail.bobason01;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class CraftSlotCommands extends JavaPlugin implements Listener
{
	public static CraftSlotCommands plugin;

	@Override
	public void onEnable() {
		plugin = this;
		saveDefaultConfig();
		CSCCommand command = new CSCCommand();
		getCommand("craftslotcommands").setExecutor(command);
		getCommand("craftslotcommands").setTabCompleter(command);
		getServer().getPluginManager().registerEvents(this, this);
	}

	private void reload() {
		reloadConfig();
	}


	@EventHandler
	public void inventoryClick(InventoryClickEvent e) {
		if(!(e.getInventory() instanceof CraftingInventory) || e.getInventory().getSize() != 5 || e.getSlotType() == InventoryType.SlotType.ARMOR || e.getSlotType() == InventoryType.SlotType.CONTAINER || e.getSlotType() == InventoryType.SlotType.FUEL || e.getSlotType() == InventoryType.SlotType.OUTSIDE || e.getSlotType() == InventoryType.SlotType.QUICKBAR) return;
		Bukkit.getScheduler().runTaskLater(plugin, () -> {
			e.setCancelled(true);
			Player player = (Player) e.getWhoClicked();

			String cmd = getConfig().getString("crafting-slot." + e.getSlot());
			if(cmd == null || cmd.isEmpty()) return;

			if(cmd.startsWith("*")) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.substring(1));
			else Bukkit.dispatchCommand(player, cmd);
		}, 1L);
	}

	public static class CSCCommand implements CommandExecutor, TabCompleter {
		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			if(sender.hasPermission("csc.admin")) {
				if(args.length > 0 && args[0].equalsIgnoreCase("reload")) {
					CraftSlotCommands.plugin.reload();
					sender.sendMessage(ChatColor.AQUA + "[" + ChatColor.BLUE + "CraftSlotCommands" + ChatColor.AQUA + "] " + ChatColor.GREEN + "Successfully reloaded.");
				}
				else sender.sendMessage(ChatColor.AQUA + "[" + ChatColor.BLUE + "CraftSlotCommands" + ChatColor.AQUA + "] " + ChatColor.GREEN + "Version 2.0");
			}
			else sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");

			return true;
		}

		@Override
		public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
			List<String> list = new ArrayList<>();
			if(args.length == 1) list.add("reload");
			return list;
		}
	}
}