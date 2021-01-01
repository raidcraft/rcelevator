package de.raidcraft.rcelevator.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.material.MaterialData;

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

    public static boolean isSign(Material material) {

        return isWallSign(material) || isPostSign(material);
    }

    public static Sign getSignByBlock(Block block)
    {
        if(!SignUtils.isSign(block.getType())) {
            return null;
        }

        return (Sign)block.getState();
    }

    /**
     * Get the attached face of the sign
     * Stolen from: https://github.com/NLthijs48/AreaShop
     * @param materialData the material data of the sign
     * @return The attached BlockFace
     */
    public static BlockFace getAttachedFace(MaterialData materialData) {
        if (isWallSign(materialData.getItemType())) {
            byte data = materialData.getData();
            switch(data) {
                case 2:
                    return BlockFace.SOUTH;
                case 3:
                    return BlockFace.NORTH;
                case 4:
                    return BlockFace.EAST;
                case 5:
                    return BlockFace.WEST;
                default:
                    return null;
            }
        } else {
            return BlockFace.DOWN;
        }
    }

    /**
     * Get the facing direction of the sign.
     * Stolen from: https://github.com/NLthijs48/AreaShop
     * @param materialData the material data of the sign
     * @return The facing BlockFace of the sign
     */
    public static BlockFace getFacing(MaterialData materialData) {
        byte data = materialData.getData();
        if (!isWallSign(materialData.getItemType())) {
            switch(data) {
                case 0:
                    return BlockFace.SOUTH;
                case 1:
                    return BlockFace.SOUTH_SOUTH_WEST;
                case 2:
                    return BlockFace.SOUTH_WEST;
                case 3:
                    return BlockFace.WEST_SOUTH_WEST;
                case 4:
                    return BlockFace.WEST;
                case 5:
                    return BlockFace.WEST_NORTH_WEST;
                case 6:
                    return BlockFace.NORTH_WEST;
                case 7:
                    return BlockFace.NORTH_NORTH_WEST;
                case 8:
                    return BlockFace.NORTH;
                case 9:
                    return BlockFace.NORTH_NORTH_EAST;
                case 10:
                    return BlockFace.NORTH_EAST;
                case 11:
                    return BlockFace.EAST_NORTH_EAST;
                case 12:
                    return BlockFace.EAST;
                case 13:
                    return BlockFace.EAST_SOUTH_EAST;
                case 14:
                    return BlockFace.SOUTH_EAST;
                case 15:
                    return BlockFace.SOUTH_SOUTH_EAST;
                default:
                    return null;
            }
        } else {
            return getAttachedFace(materialData).getOppositeFace();
        }
    }

    /**
     * Set the facing direction of the sign.
     * Stolen from: https://github.com/NLthijs48/AreaShop
     * @param materialData the material data of the sign
     * @param face The block face to set to
     */
    public static void setFacingDirection(MaterialData materialData, BlockFace face) {
        byte data;
        if (isWallSign(materialData.getItemType())) {
            switch(face) {
                case NORTH:
                    data = 2;
                    break;
                case SOUTH:
                    data = 3;
                    break;
                case WEST:
                    data = 4;
                    break;
                case EAST:
                default:
                    data = 5;
            }
        } else {
            switch(face) {
                case NORTH:
                    data = 8;
                    break;
                case SOUTH:
                    data = 0;
                    break;
                case WEST:
                    data = 4;
                    break;
                case EAST:
                    data = 12;
                    break;
                case SOUTH_SOUTH_WEST:
                    data = 1;
                    break;
                case SOUTH_WEST:
                    data = 2;
                    break;
                case WEST_SOUTH_WEST:
                    data = 3;
                    break;
                case WEST_NORTH_WEST:
                    data = 5;
                    break;
                case NORTH_WEST:
                    data = 6;
                    break;
                case NORTH_NORTH_WEST:
                    data = 7;
                    break;
                case NORTH_NORTH_EAST:
                    data = 9;
                    break;
                case NORTH_EAST:
                    data = 10;
                    break;
                case EAST_NORTH_EAST:
                    data = 11;
                    break;
                case EAST_SOUTH_EAST:
                    data = 13;
                    break;
                case SOUTH_SOUTH_EAST:
                    data = 15;
                    break;
                case SOUTH_EAST:
                default:
                    data = 14;
            }
        }

        materialData.setData(data);
    }
}
