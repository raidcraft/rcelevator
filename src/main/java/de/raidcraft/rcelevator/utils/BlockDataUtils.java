package de.raidcraft.rcelevator.utils;

import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlockDataUtils {

    public static BlockFace getFacing(BlockData blockData) {

        String blockDataString = blockData.getAsString();

        if(blockDataString.contains("facing=")) {
            Pattern pattern = Pattern.compile("facing=([a-zA-Z]+)");
            Matcher matcher = pattern.matcher(blockDataString);
            if (matcher.find()) {
                String facingName = matcher.group(1);
                return BlockFace.valueOf(facingName.toUpperCase());
            }
        } else if(blockDataString.contains("rotation=")) {
            Pattern pattern = Pattern.compile("rotation=([\\d]+)");
            Matcher matcher = pattern.matcher(blockDataString);
            if (matcher.find()) {
                int rotation = Integer.parseInt(matcher.group(1));
                switch(rotation) {
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
                        return BlockFace.DOWN;
                }
            }
        }

        // Facing not found
        return BlockFace.DOWN;
    }
}
