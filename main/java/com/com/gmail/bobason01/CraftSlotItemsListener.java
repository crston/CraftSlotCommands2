package com.gmail.bobason01;

import com.destroystokyo.paper.event.player.PlayerRecipeBookClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.InventoryView;

import java.util.Objects;

public class CraftSlotItemsListener implements Listener {

    private static ItemStack i0;
    private static ItemStack i1;
    private static ItemStack i2;
    private static ItemStack i3;
    private static ItemStack i4;
    public CraftSlotItemsListener(FileConfiguration config) {
        i0 = ItemBuilder.build(Objects.requireNonNull(config.getConfigurationSection("slot-item.0")));
        i1 = ItemBuilder.build(Objects.requireNonNull(config.getConfigurationSection("slot-item.1")));
        i2 = ItemBuilder.build(Objects.requireNonNull(config.getConfigurationSection("slot-item.2")));
        i3 = ItemBuilder.build(Objects.requireNonNull(config.getConfigurationSection("slot-item.3")));
        i4 = ItemBuilder.build(Objects.requireNonNull(config.getConfigurationSection("slot-item.4")));
    }

    @EventHandler
    public void recipeClick(PlayerRecipeBookClickEvent e) {
        if (e.getPlayer().getOpenInventory().getType() == InventoryType.CRAFTING) e.setCancelled(true);
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked().getOpenInventory().getType() == InventoryType.CRAFTING && e.getWhoClicked().getGameMode() != GameMode.CREATIVE)
            removeItems(e.getWhoClicked().getOpenInventory());

        Bukkit.getScheduler().runTaskLater(CraftSlotCommands.plugin, () -> {
            if (e.getWhoClicked().getOpenInventory().getTopInventory() instanceof CraftingInventory &&
                    e.getWhoClicked().getOpenInventory().getType() == InventoryType.CRAFTING && e.getWhoClicked().getGameMode() != GameMode.CREATIVE && !(e.getWhoClicked().isDead()))
                addItems(e.getWhoClicked().getOpenInventory());
        }, 1L);
    }

    @EventHandler
    public void inventoryClose(InventoryCloseEvent e) {
        if (e.getPlayer().getOpenInventory().getType() == InventoryType.CRAFTING && e.getPlayer().getGameMode() != GameMode.CREATIVE)
            removeItems(e.getPlayer().getOpenInventory());

        Bukkit.getScheduler().runTaskLater(CraftSlotCommands.plugin, () -> {
            if (e.getPlayer().getOpenInventory().getTopInventory() instanceof CraftingInventory &&
                    e.getPlayer().getOpenInventory().getType() == InventoryType.CRAFTING && e.getPlayer().getGameMode() != GameMode.CREATIVE && !(e.getPlayer().isDead()))
                addItems(e.getPlayer().getOpenInventory());
        }, 1L);
    }

    @EventHandler
    public void inventoryOpen(InventoryOpenEvent e) {
        if (e.getPlayer().getOpenInventory().getType() == InventoryType.CRAFTING && e.getPlayer().getGameMode() != GameMode.CREATIVE)
            removeItems(e.getPlayer().getOpenInventory());

        Bukkit.getScheduler().runTaskLater(CraftSlotCommands.plugin, () -> {
            if (e.getPlayer().getOpenInventory().getTopInventory() instanceof CraftingInventory &&
                    e.getPlayer().getOpenInventory().getType() == InventoryType.CRAFTING && e.getPlayer().getGameMode() != GameMode.CREATIVE && !(e.getPlayer().isDead()))
                removeItems(e.getPlayer().getOpenInventory());
        }, 1L);
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        if (e.getPlayer().getOpenInventory().getTopInventory() instanceof CraftingInventory &&
                e.getPlayer().getOpenInventory().getType() == InventoryType.CRAFTING && e.getPlayer().getGameMode() != GameMode.CREATIVE && !(e.getPlayer().isDead())) {
            removeItems(e.getPlayer().getOpenInventory());

            Bukkit.getScheduler().runTaskLater(CraftSlotCommands.plugin, () -> {
                if (e.getPlayer().getOpenInventory().getTopInventory() instanceof CraftingInventory &&
                        e.getPlayer().getOpenInventory().getType() == InventoryType.CRAFTING && e.getPlayer().getGameMode() != GameMode.CREATIVE && !(e.getPlayer().isDead()))
                    addItems(e.getPlayer().getOpenInventory());
            }, 1L);
        }
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent e) {
        if (e.getPlayer().getOpenInventory().getTopInventory() instanceof CraftingInventory &&
                e.getPlayer().getOpenInventory().getType() == InventoryType.CRAFTING && e.getPlayer().getGameMode() != GameMode.CREATIVE) {
            removeItems(e.getPlayer().getOpenInventory());

            Bukkit.getScheduler().runTaskLater(CraftSlotCommands.plugin, () -> {
                if (e.getPlayer().getOpenInventory().getTopInventory() instanceof CraftingInventory &&
                        e.getPlayer().getOpenInventory().getType() == InventoryType.CRAFTING && e.getPlayer().getGameMode() != GameMode.CREATIVE)
                    removeItems(e.getPlayer().getOpenInventory());
            }, 1L);
        }
    }



    @EventHandler
    public void playerDeath(PlayerDeathEvent e) {
        if (e.getEntity().getOpenInventory().getTopInventory() instanceof CraftingInventory &&
                e.getEntity().getOpenInventory().getType() == InventoryType.CRAFTING && e.getEntity().getGameMode() != GameMode.CREATIVE)
            removeItems(e.getEntity().getOpenInventory());

        Bukkit.getScheduler().runTaskLater(CraftSlotCommands.plugin, () -> {
            if (e.getEntity().getOpenInventory().getTopInventory() instanceof CraftingInventory &&
                    e.getEntity().getOpenInventory().getType() == InventoryType.CRAFTING && e.getEntity().getGameMode() != GameMode.CREATIVE)
                removeItems(e.getEntity().getOpenInventory());
        }, 1L);
    }

    @EventHandler
    public void playerRespawn(PlayerRespawnEvent e) {
        if (e.getPlayer().getOpenInventory().getTopInventory() instanceof CraftingInventory &&
                e.getPlayer().getOpenInventory().getType() == InventoryType.CRAFTING && e.getPlayer().getGameMode() != GameMode.CREATIVE)
            removeItems(e.getPlayer().getOpenInventory());

        Bukkit.getScheduler().runTaskLater(CraftSlotCommands.plugin, () -> {
            if (e.getPlayer().getOpenInventory().getTopInventory() instanceof CraftingInventory &&
                    e.getPlayer().getOpenInventory().getType() == InventoryType.CRAFTING && e.getPlayer().getGameMode() != GameMode.CREATIVE && !(e.getPlayer().isDead()))
                addItems(e.getPlayer().getOpenInventory());
        }, 1L);
    }

    @EventHandler
    public void playerGameModeChange(PlayerGameModeChangeEvent e) {
        if (e.getPlayer().getOpenInventory().getTopInventory() instanceof CraftingInventory &&
                e.getPlayer().getOpenInventory().getType() == InventoryType.CRAFTING)
            removeItems(e.getPlayer().getOpenInventory());

        Bukkit.getScheduler().runTaskLater(CraftSlotCommands.plugin, () -> {
            if (e.getPlayer().getOpenInventory().getTopInventory() instanceof CraftingInventory &&
                    e.getPlayer().getOpenInventory().getType() == InventoryType.CRAFTING)
                removeItems(e.getPlayer().getOpenInventory());
        }, 1L);
    }

    private void addItems(InventoryView inventory) {
        if(inventory.getItem(1) == null || Objects.requireNonNull(inventory.getItem(1)).getType() == Material.AIR)
            inventory.setItem(1, i1);
        if(inventory.getItem(2) == null || Objects.requireNonNull(inventory.getItem(2)).getType() == Material.AIR)
            inventory.setItem(2, i2);
        if(inventory.getItem(3) == null || Objects.requireNonNull(inventory.getItem(3)).getType() == Material.AIR)
            inventory.setItem(3, i3);
        if(inventory.getItem(4) == null || Objects.requireNonNull(inventory.getItem(4)).getType() == Material.AIR)
            inventory.setItem(4, i4);
        if(inventory.getItem(1) != null && !(Objects.requireNonNull(inventory.getItem(1)).getType() == Material.AIR) &&
                inventory.getItem(2) != null && !(Objects.requireNonNull(inventory.getItem(2)).getType() == Material.AIR) &&
                inventory.getItem(3) != null && !(Objects.requireNonNull(inventory.getItem(3)).getType() == Material.AIR) &&
                inventory.getItem(4) != null && !(Objects.requireNonNull(inventory.getItem(4)).getType() == Material.AIR))
            inventory.setItem(0, i0);
    }
    static void removeItems(InventoryView inventory) {
        if(inventory.getItem(0) != null && Objects.requireNonNull(inventory.getItem(0)).equals(i0))
            inventory.setItem(0, null);
        if(inventory.getItem(1) != null && Objects.requireNonNull(inventory.getItem(1)).equals(i1))
            inventory.setItem(1, null);
        if(inventory.getItem(2) != null && Objects.requireNonNull(inventory.getItem(2)).equals(i2))
            inventory.setItem(2, null);
        if(inventory.getItem(3) != null && Objects.requireNonNull(inventory.getItem(3)).equals(i3))
            inventory.setItem(3, null);
        if(inventory.getItem(4) != null && Objects.requireNonNull(inventory.getItem(4)).equals(i4))
            inventory.setItem(4, null);
    }
    public void reload(FileConfiguration config) {
        i0 = ItemBuilder.build(Objects.requireNonNull(config.getConfigurationSection("slot-item.0")));
        i1 = ItemBuilder.build(Objects.requireNonNull(config.getConfigurationSection("slot-item.1")));
        i2 = ItemBuilder.build(Objects.requireNonNull(config.getConfigurationSection("slot-item.2")));
        i3 = ItemBuilder.build(Objects.requireNonNull(config.getConfigurationSection("slot-item.3")));
        i4 = ItemBuilder.build(Objects.requireNonNull(config.getConfigurationSection("slot-item.4")));
    }
}