package com.gmail.bobason01;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.util.Vector;

import java.util.Objects;

public class CraftSlotItemsListener implements Listener {

    private ItemStack i0, i1, i2, i3, i4;
    public CraftSlotItemsListener(FileConfiguration config) {
        i0 = ItemBuilder.build(Objects.requireNonNull(config.getConfigurationSection("slot-item.0")));
        i1 = ItemBuilder.build(Objects.requireNonNull(config.getConfigurationSection("slot-item.1")));
        i2 = ItemBuilder.build(Objects.requireNonNull(config.getConfigurationSection("slot-item.2")));
        i3 = ItemBuilder.build(Objects.requireNonNull(config.getConfigurationSection("slot-item.3")));
        i4 = ItemBuilder.build(Objects.requireNonNull(config.getConfigurationSection("slot-item.4")));
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {
        if(e.getInventory() instanceof CraftingInventory && e.getInventory().getSize() == 5)
            removeItems(e.getView());

        Bukkit.getScheduler().runTaskLater(CraftSlotCommands.plugin, () -> {
            if(e.getWhoClicked().getOpenInventory().getTopInventory() instanceof CraftingInventory &&
                    e.getWhoClicked().getOpenInventory().getTopInventory().getSize() == 5)
                addItems(e.getWhoClicked().getOpenInventory());
        }, 10);
    }
    @EventHandler
    public void inventoryClose(InventoryCloseEvent e) {
        if(e.getInventory() instanceof CraftingInventory && e.getInventory().getSize() == 5)
            removeItems(e.getView());

        Bukkit.getScheduler().runTaskLater(CraftSlotCommands.plugin, () -> {
            if(e.getPlayer().getOpenInventory().getTopInventory() instanceof CraftingInventory &&
                    e.getPlayer().getOpenInventory().getTopInventory().getSize() == 5)
                addItems(e.getPlayer().getOpenInventory());
        }, 10);
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        if(e.getPlayer().getOpenInventory().getTopInventory() instanceof CraftingInventory &&
                e.getPlayer().getOpenInventory().getTopInventory().getSize() == 5)
            addItems(e.getPlayer().getOpenInventory());
    }
    @EventHandler
    public void playerLeave(PlayerQuitEvent e)
    { playerLeaveFunction(e.getPlayer()); }
    public void playerLeaveFunction(Player p)
    { removeItems(p.getOpenInventory()); }

    @EventHandler
    public void playerDeath(PlayerDeathEvent e) {
        e.getDrops().remove(i1); e.getDrops().remove(i2);
        e.getDrops().remove(i3); e.getDrops().remove(i4);
        e.getEntity().closeInventory(); playerLeaveFunction(e.getEntity());
    }

    private void pickup(Item item, Player p) {
        item.setTicksLived(1);
        p.getInventory().addItem(item.getItemStack());
        p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.2f, 1.4f);
        item.setVelocity(p.getLocation().toVector().subtract(item.getLocation().toVector()).normalize().add(new Vector(0, 0.1, 0)).multiply(0.5));
        Bukkit.getScheduler().runTaskLater(CraftSlotCommands.plugin, item::remove, 8);
    }
    private void addItems(InventoryView inventory) {
        if(inventory.getItem(0) == null || Objects.requireNonNull(inventory.getItem(0)).getType().isAir())
            inventory.setItem(0, i0);
        if(inventory.getItem(1) == null || Objects.requireNonNull(inventory.getItem(1)).getType().isAir())
            inventory.setItem(1, i1);
        if(inventory.getItem(2) == null || Objects.requireNonNull(inventory.getItem(2)).getType().isAir())
            inventory.setItem(2, i2);
        if(inventory.getItem(3) == null || Objects.requireNonNull(inventory.getItem(3)).getType().isAir())
            inventory.setItem(3, i3);
        if(inventory.getItem(4) == null || Objects.requireNonNull(inventory.getItem(4)).getType().isAir())
            inventory.setItem(4, i4);
        if(inventory.getItem(1) != null && !Objects.requireNonNull(inventory.getItem(1)).getType().isAir() &&
                inventory.getItem(2) != null && !Objects.requireNonNull(inventory.getItem(2)).getType().isAir() &&
                inventory.getItem(3) != null && !Objects.requireNonNull(inventory.getItem(3)).getType().isAir() &&
                inventory.getItem(4) != null && !Objects.requireNonNull(inventory.getItem(4)).getType().isAir())
            inventory.setItem(0, i0);
    }
    private void removeItems(InventoryView inventory) {
        for(int i = 0; i < 5; i++)
            inventory.setItem(i, null);
    }
    public void reload(FileConfiguration config) {
        i0 = ItemBuilder.build(Objects.requireNonNull(config.getConfigurationSection("slot-item.0")));
        i1 = ItemBuilder.build(Objects.requireNonNull(config.getConfigurationSection("slot-item.1")));
        i2 = ItemBuilder.build(Objects.requireNonNull(config.getConfigurationSection("slot-item.2")));
        i3 = ItemBuilder.build(Objects.requireNonNull(config.getConfigurationSection("slot-item.3")));
        i4 = ItemBuilder.build(Objects.requireNonNull(config.getConfigurationSection("slot-item.4")));
    }
}