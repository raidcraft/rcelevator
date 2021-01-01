package de.raidcraft.rcelevator.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUtils {

    /**
     * Checks if the given line on the sign is equals to
     * the given withText colored in the given color.
     *
     * @param strSign colored sign withText
     * @param strText withText to compare
     *
     * @return true if withText is equal
     */
    public static boolean isLineEqual(String strSign, String strText) {

        strSign = strip(strSign);
        strText = strip(strText);
        return strText.equalsIgnoreCase(strSign);
    }

    public static String strip(String strText) {

        strText = ChatColor.stripColor(strText).trim();
        Pattern pattern = Pattern.compile("\\[(.*?)\\]");
        Matcher matcher = pattern.matcher(strText);
        if (matcher.matches()) {
            strText = matcher.group(1);
        }
        return strText;
    }

    public static boolean isWallSign(Material signMaterial) {

        Material[] wallSignMaterial = {
                Material.WARPED_WALL_SIGN,
                Material.ACACIA_WALL_SIGN,
                Material.BIRCH_WALL_SIGN,
                Material.CRIMSON_WALL_SIGN,
                Material.OAK_WALL_SIGN,
                Material.JUNGLE_WALL_SIGN,
                Material.SPRUCE_WALL_SIGN,
                Material.DARK_OAK_WALL_SIGN
        };

        return Arrays.asList(wallSignMaterial).contains(signMaterial);
    }

    public static boolean isPostSign(Material signMaterial) {

        Material[] postSignMaterial = {
                Material.WARPED_SIGN,
                Material.ACACIA_SIGN,
                Material.BIRCH_SIGN,
                Material.CRIMSON_SIGN,
                Material.OAK_SIGN,
                Material.JUNGLE_SIGN,
                Material.SPRUCE_SIGN,
                Material.DARK_OAK_SIGN
        };

        return Arrays.asList(postSignMaterial).contains(signMaterial);
    }
}
