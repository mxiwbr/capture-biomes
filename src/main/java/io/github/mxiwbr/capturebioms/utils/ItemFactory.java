package io.github.mxiwbr.capturebioms.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.apache.commons.text.WordUtils;

import java.util.List;

public class ItemFactory {

    /**
     * Creates a biom potion
     * @param biome the biome (as Biome object) for which the potion should be created
     * @return ItemStack of the created potion
     */
    public static ItemStack createBiomePotion(Biome biome) {

        // Create potion ItemStack as splash potion
        ItemStack potion = new ItemStack(Material.SPLASH_POTION);
        // create meta for the item
        PotionMeta meta = (PotionMeta) potion.getItemMeta();

        // set item name
        meta.customName(Component.text("Biome potion", NamedTextColor.DARK_AQUA));
        // set item lore
        meta.lore(List.of(
                Component.text("Captured biome: ", NamedTextColor.GRAY),
                Component.text(WordUtils.capitalizeFully(biome.getKey().getKey().replace("_", " ")), NamedTextColor.WHITE),
                Component.text("Size:", NamedTextColor.GRAY),
                Component.text("1x1 chunk", NamedTextColor.WHITE)
        ));

        // get the key of the biome
        String biomeKey = biome.getKey().getKey();
        // set the potion color depending on the biome
        Color color = BiomeUtils.getBiomeColor(biomeKey);
        // cancel if now
        if (color == null ) { return null; }
        meta.setColor(color);

        potion.setItemMeta(meta);
        return potion;
    }

}
