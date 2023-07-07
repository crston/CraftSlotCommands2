package com.gmail.bobason01;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

public class ItemBuilder {
        public static ItemStack build(ConfigurationSection config) {
            boolean errored = false;
            ItemStack item;
            List<String> lore = new ArrayList<>();

            Material.valueOf(config.getString("material"));
            item = new ItemStack(Material.valueOf(config.getString("material")));

            ItemMeta meta = item.getItemMeta();
            if (config.contains("name"))
                Objects.requireNonNull(meta).setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("name"))));
            if (config.contains("model"))
                Objects.requireNonNull(meta).setCustomModelData(config.getInt("model"));
            if (config.getBoolean("hide-flags", false))
                Objects.requireNonNull(meta).addItemFlags(ItemFlag.values());
            if (config.getBoolean("unbreakable", false))
                Objects.requireNonNull(meta).setUnbreakable(true);
            if (config.contains("damage")) {
                if (meta instanceof Damageable)
                    ((Damageable) meta).setDamage(config.getInt("damage"));
                else {
                    errored = true;
                    lore.add("&cCouldn't add damage value.");
                    lore.add("&cDoes this item have durability?");
                }
            }
            if (config.contains("skull-owner-uuid") || config.contains("skull-texture-value")) {
                if (meta instanceof SkullMeta) {
                    if (config.contains("skull-owner-uuid"))
                        ((SkullMeta) meta).setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(Objects.requireNonNull(config.getString("skull-owner-uuid")))));
                    else if (config.contains("skull-texture-value")) {
                        try {
                            Field profileField = meta.getClass().getDeclaredField("profile");
                            profileField.setAccessible(true);
                            GameProfile gp = new GameProfile(UUID.randomUUID(), null);
                            gp.getProperties().put("textures", new Property("textures", config.getString("skull-texture-value")));
                            profileField.set(meta, gp);
                        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
                            errored = true;
                            lore.add("&cCouldn't add head texture.");
                            lore.add("&cIs your Spigot version supported?");
                            e.printStackTrace();
                        }
                    }
                } else {
                    errored = true;
                    lore.add("&cCouldn't add head texture.");
                    lore.add("&cIs the Material '&ePLAYER_HEAD'&c?");
                }
            }

            if (errored) {
                item = new ItemStack(Material.BARRIER);
                meta = item.getItemMeta();
                lore.add("");
                lore.add("&cConfig: '&e" + config.getName() + "&c'");
                lore.add("&c(&e" + config.getCurrentPath() + "&c)");

                for (ListIterator<String> i = lore.listIterator(); i.hasNext(); ) {
                    String line = i.next();
                    i.set(ChatColor.translateAlternateColorCodes('&', line));
                }

                Objects.requireNonNull(meta).setDisplayName(ChatColor.DARK_RED + "ERROR!");
                meta.setLore(lore);
                item.setItemMeta(meta);
            } else if (config.contains("lore")) {
                for (String line : config.getStringList("lore"))
                    lore.add(ChatColor.translateAlternateColorCodes('&', line));
                Objects.requireNonNull(meta).setLore(lore);
            }

            item.setItemMeta(meta);
            return item;
        }
    }