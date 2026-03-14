package io.github.mxiwbr.capturebioms.utils;

import io.github.mxiwbr.capturebioms.CaptureBioms;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import io.papermc.paper.datacomponent.item.UseCooldown;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.apache.commons.text.WordUtils;
import org.bukkit.persistence.PersistentDataType;
import java.util.List;

public class ItemFactory {

    /**
     * Creates a biom potion
     * @param biome the biome (as Biome object) for which the potion should be created
     * @param tier the tier (size set in config) of the potion
     * @return ItemStack of the created potion
     */
    public static ItemStack createBiomePotion(Biome biome, int tier) {

        // Create potion ItemStack as lingering potion
        ItemStack potion = new ItemStack(Material.LINGERING_POTION);
        // create meta for the item
        PotionMeta meta = (PotionMeta) potion.getItemMeta();

        // get the key of the biome
        String biomeKey = biome.getKey().getKey();
        // set the potion color depending on the biome
        Color color = BiomeUtils.getBiomeColor(biomeKey);
        // cancel if now
        if (color == null ) { return null; }
        meta.setColor(color);

        // get size matching to tier from config
        int size = switch (tier) {
            case 2 -> CaptureBioms.CONFIG.getBiomePotionSize()[1];
            case 3 -> CaptureBioms.CONFIG.getBiomePotionSize()[2];
            case 4 -> CaptureBioms.CONFIG.getBiomePotionSize()[3];
            default -> CaptureBioms.CONFIG.getBiomePotionSize()[0];
        };

        // set item name
        // parse org.bukkit.Color to net.kyori.adventure.text.format.NamedTextColor: NamedTextColor.nearestTo(TextColor.color(color.asRGB()))
        // make not italic: decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)
        meta.customName(Component.text("Biome potion", NamedTextColor.nearestTo(TextColor.color(color.asRGB()))).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        // set item lore
        // make lore not italic: .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)
        meta.lore(List.of(
                Component.text("Captured biome: ", NamedTextColor.GRAY)
                        .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                        .append(Component.text(WordUtils.capitalizeFully(biome.getKey().getKey().replace("_", " ")), NamedTextColor.WHITE)
                                        .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
                Component.text("Size: ", NamedTextColor.GRAY)
                        .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                        .append(Component.text(size + "x" + size + " blocks", NamedTextColor.WHITE))
                                        .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)
        ));
        // PersistentDataContainer key to identify the biome potion when thrown
        meta.getPersistentDataContainer().set(new NamespacedKey(CaptureBioms.INSTANCE, "capturebioms.biomepotion"), PersistentDataType.INTEGER, 1);
        // PersistentDataContainer key to get the potion tier when thrown
        meta.getPersistentDataContainer().set(new NamespacedKey(CaptureBioms.INSTANCE, "capturebioms.biomepotion_tier"), PersistentDataType.INTEGER, tier);

        // Hide the "no effects" tooltip
        potion.setItemMeta(meta);
        potion.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay().addHiddenComponents(DataComponentTypes.POTION_CONTENTS));

        // Only apply the potion cooldown if it is enabled (default) in the config
        if (CaptureBioms.CONFIG.isEnablePotionCooldown()) {

            // set potion cooldown of 30 seconds for all biome potions
            Key cooldownGroupKey = Key.key("capturebioms", "potion_cooldown");
            UseCooldown useCooldown = UseCooldown.useCooldown(CaptureBioms.CONFIG.getPotionCooldown()).cooldownGroup(cooldownGroupKey).build();
            potion.setData(DataComponentTypes.USE_COOLDOWN, useCooldown);
        }

        return potion;
    }

}
