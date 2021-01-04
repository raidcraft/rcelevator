package de.raidcraft.rcelevator;

import be.seeseemelk.mockbukkit.block.data.BlockDataMock;
import de.raidcraft.rcelevator.utils.SignUtils;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

public class FacingBlockData extends BlockDataMock {

    BlockFace blockFace;

    public FacingBlockData(Material type, BlockFace blockFace) {
        super(type);
        this.blockFace = blockFace;
    }

    @Override
    public @NotNull String getAsString() {

        String decodedFacing;

        if(SignUtils.isWallSign(getMaterial())) {
            decodedFacing = "facing=" + blockFace.name().toLowerCase();
        } else {
            decodedFacing = "rotation=";
            int rotation;
            if(blockFace == BlockFace.SOUTH)                    rotation = 0;
            else if(blockFace == BlockFace.SOUTH_SOUTH_WEST)     rotation = 1;
            else if(blockFace == BlockFace.SOUTH_WEST)           rotation = 2;
            else if(blockFace == BlockFace.WEST_SOUTH_WEST)      rotation = 3;
            else if(blockFace == BlockFace.WEST)                 rotation = 4;
            else if(blockFace == BlockFace.WEST_NORTH_WEST)      rotation = 5;
            else if(blockFace == BlockFace.NORTH_WEST)           rotation = 6;
            else if(blockFace == BlockFace.NORTH_NORTH_WEST)     rotation = 7;
            else if(blockFace == BlockFace.NORTH)                rotation = 8;
            else if(blockFace == BlockFace.NORTH_NORTH_EAST)     rotation = 9;
            else if(blockFace == BlockFace.NORTH_EAST)           rotation = 10;
            else if(blockFace == BlockFace.EAST_NORTH_EAST)      rotation = 11;
            else if(blockFace == BlockFace.EAST)                 rotation = 12;
            else if(blockFace == BlockFace.EAST_SOUTH_EAST)      rotation = 13;
            else if(blockFace == BlockFace.SOUTH_EAST)           rotation = 14;
            else if(blockFace == BlockFace.SOUTH_SOUTH_EAST)     rotation = 15;
            else rotation = 0;

            decodedFacing += rotation;
        }

        return "minecraft:" + getMaterial().name().toLowerCase() + "[" + decodedFacing + ",waterlogged=false]";
    }
}
