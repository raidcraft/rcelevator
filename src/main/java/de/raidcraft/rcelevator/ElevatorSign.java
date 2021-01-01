package de.raidcraft.rcelevator;

import de.raidcraft.rcelevator.exceptions.NoTargetException;
import de.raidcraft.rcelevator.exceptions.UnknownFloorException;
import de.raidcraft.rcelevator.exceptions.WrongSignFormatException;
import de.raidcraft.rcelevator.utils.SignUtils;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.material.Directional;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Philip
 * Date: 17.09.12 - 22:30
 * Description:
 */
public class ElevatorSign {

    private static final String FLOOR_TEXT = "Etage: ";
    private static final String RANGE_SEPARATOR = "-";
    private static final String TARGET_TEXT = "Ziel: ";
    private static final ChatColor TARGET_COLOR = ChatColor.DARK_RED;
    private static final String PREFIX_UPSTAIRS = "OG";
    private static final String GROUND_FLOOR = "EG";
    private static final String PREFIX_DOWNSTAIRS = "UG";
    private static final int DEFAULT_RADIUS = 2;
    private static final int MAX_RADIUS = 5;
    
    @Getter
    private Sign sign;
    private int thisFloor;
    private int lowestFloor;
    private int highestFloor;
    private int targetFloor;
    private boolean valid;
    private String invalidMsg;
    private int passengerRadius;
    
    public ElevatorSign(Sign sign) {
        this(sign.getLines());
        this.sign = sign;
        if(isValid()) {
            updateSign();
        }
    }
    
    public ElevatorSign(String[] lines) {
        try {
            this.thisFloor = parseFloorString(lines[0].replace(FLOOR_TEXT, ""));
            
            String[] rangeFloors = lines[2].split(RANGE_SEPARATOR);
            this.lowestFloor = parseFloorString(rangeFloors[0]);
            //parse radius
            if(rangeFloors.length == 3) {
                try {
                    rangeFloors[1] = rangeFloors[1].trim();
                    this.passengerRadius = Integer.parseInt(rangeFloors[1]);
                }
                catch(Exception e) {
                    throw new WrongSignFormatException("Die Radius-Angabe hat das falsche Format!");
                }
                if(this.passengerRadius > MAX_RADIUS) {
                    throw new WrongSignFormatException("Der Teleport-Radius ist zu groß (max. " + MAX_RADIUS + ")!");
                }
                this.highestFloor = parseFloorString(rangeFloors[2]);
            }
            else {
                this.passengerRadius = DEFAULT_RADIUS;
                this.highestFloor = parseFloorString(rangeFloors[1]);
            }
            
            this.targetFloor = parseFloorString(lines[3].replace(TARGET_TEXT, ""));
            valid = true;
        }
        catch(Throwable e) {
            invalidMsg = e.getMessage();
            valid = false;
        }
    }
    
    private int parseFloorString(String floorString) throws UnknownFloorException {
        floorString = ChatColor.stripColor(floorString);
        floorString = floorString.trim();

        if(floorString.equals(GROUND_FLOOR)) {
            return 0;
        }
        try {
            if(floorString.startsWith(PREFIX_UPSTAIRS)) {
                return Integer.parseInt(floorString.replace(PREFIX_UPSTAIRS, ""));
            }
            if(floorString.startsWith(PREFIX_DOWNSTAIRS)) {
                return -Integer.parseInt(floorString.replace(PREFIX_DOWNSTAIRS, ""));
            }
            throw new UnknownFloorException("Die Etagenbezeichnung ist falsch: " + floorString);
        }
        catch(Exception e) {
            throw new UnknownFloorException("Die Etagenbezeichnung ist falsch: " + floorString);
        }
    }
    
    private String getFloorString(int floor) {
        if(floor == 0) {
            return GROUND_FLOOR;
        }
        if(floor > 0) {
            return PREFIX_UPSTAIRS + floor;
        }
        else {
            return PREFIX_DOWNSTAIRS + Math.abs(floor);
        }
    }
    
    public void increaseTarget() {
        targetFloor++;
        if(targetFloor == thisFloor) {
            targetFloor++;
        }
        if(targetFloor > highestFloor) {
            targetFloor = lowestFloor;
        }
        if(targetFloor == thisFloor) {
            targetFloor++;
        }
        updateSign();
    }
    
    public void decreaseTarget() {
        targetFloor--;
        if(targetFloor == thisFloor) {
            targetFloor--;
        }
        if(targetFloor < lowestFloor) {
            targetFloor = highestFloor;
        }
        updateSign();
    }
    
    private void updateSign() {
        sign.setLine(0, FLOOR_TEXT + getFloorString(thisFloor));
        sign.setLine(1, RCElevator.SIGN_TAG);
        if(passengerRadius != DEFAULT_RADIUS) {
            sign.setLine(2, getFloorString(lowestFloor) + " " + RANGE_SEPARATOR + passengerRadius + RANGE_SEPARATOR + " " + getFloorString(highestFloor));
        }
        else {
            sign.setLine(2, getFloorString(lowestFloor) + " "  + RANGE_SEPARATOR + " " + getFloorString(highestFloor));
        }
        sign.setLine(3, TARGET_TEXT + TARGET_COLOR + getFloorString(targetFloor));
        sign.update();
    }
    
    private Location getTarget() throws NoTargetException {
        Location signLocation = sign.getLocation();
        for(int i = signLocation.getBlockY(); checkSearchCount(i); i = incDecSearchCount(i)) {
            
            Block block = signLocation.getWorld().getBlockAt(signLocation.getBlockX(), i, signLocation.getBlockZ());
            if(block == null || !(block.getState() instanceof Sign)) {
                continue;
            }
            ElevatorSign elevatorSign = new ElevatorSign((Sign)block.getState());
            if(elevatorSign.isValid() && elevatorSign.getThisFloor() == targetFloor) {

                MaterialData materialData = block.getState().getData();
                BlockFace facing = SignUtils.getFacing(materialData);

                Block targetBlock = null;
                if(facing == BlockFace.NORTH) {
                    targetBlock = block.getRelative(-1, 0, 0);
                }
                if(facing == BlockFace.EAST) {
                    targetBlock = block.getRelative(0, 0, -1);
                }
                if(facing == BlockFace.SOUTH) {
                    targetBlock = block.getRelative(1, 0, 0);
                }
                if(facing == BlockFace.WEST) {
                    targetBlock = block.getRelative(0, 0, 1);
                }
                
                if(targetBlock == null) {
                    throw new NoTargetException("Das gewählte Stockwerk bietet nicht genug Platz!");
                }

                for(i = 0; i < 5; i++) {
                    Block below = targetBlock.getRelative(0, -1, 0);
                    if(below.getType() == Material.AIR) {
                        targetBlock = below;
                    }
                    else {
                        break;
                    }
                }
                return targetBlock.getLocation();
            }
        }
        throw new NoTargetException("Das gewählte Stockwerk wurde nicht gefunden!");
    }
    
    private int incDecSearchCount(int i) {
        if(targetFloor > thisFloor) {
            return i+1;
        }
        else {
            return i-1;
        }
    }

    private boolean checkSearchCount(int i) {
        if(targetFloor > thisFloor) {
            return (i < sign.getLocation().getWorld().getMaxHeight());
        }
        else {
            return (i > 0);
        }
    }

    private List<Player> getPassenger() {
        List<Player> passenger = new ArrayList<>();
        for(Player player : sign.getLocation().getWorld().getPlayers()) {
            if(Math.abs(Math.abs(player.getLocation().getY()) - Math.abs(sign.getLocation().getY())) > 2) continue;
            if(SignUtils.isWallSign(sign.getType())) {

                MaterialData materialData = sign.getBlock().getState().getData();
                BlockFace facing = SignUtils.getFacing(materialData);

                if(facing == BlockFace.NORTH) {
                    if(player.getLocation().getX()-1 > sign.getLocation().getX()) continue;
                }
                if(facing == BlockFace.EAST) {
                    if(player.getLocation().getZ()-1 > sign.getLocation().getZ()) continue;
                }
                if(facing == BlockFace.SOUTH) {
                    if(player.getLocation().getX()+1 < sign.getLocation().getX()) continue;
                }
                if(facing == BlockFace.WEST) {
                    if(player.getLocation().getZ()+1 < sign.getLocation().getZ()) continue;
                }


            }
            int distX = (int)Math.round(Math.abs((player.getLocation().getX() - (sign.getLocation().getX() + 0.5) )));
            int distZ = (int)Math.round(Math.abs((player.getLocation().getZ() - (sign.getLocation().getZ() + 0.5) )));

            if(distX <= passengerRadius && distZ <= passengerRadius) {
                passenger.add(player);
            }
        }
        return passenger;
    }

    public boolean teleportPlayersToTarget() {
        List<Player> passenger = getPassenger();
        if(passenger.size() == 0) {
            return false;
        }
        try {
            Location target = getTarget();
            for(Player player : passenger) {
                Location newLocation = player.getLocation();
                newLocation.setY(target.getY());
                player.teleport(newLocation);
                player.sendMessage(ChatColor.GOLD + "[Aufzug] "
                        + ChatColor.GREEN + "Willkommen im " + getFloorTypeString(targetFloor) + "!");
            }
        }
        catch(NoTargetException e) {
            for(Player player : passenger) {
                player.sendMessage(ChatColor.RED + "[Aufzug] " + e.getMessage());
                return false;
            }
        }
        return true;
    }
    
    private String getFloorTypeString(int floor) {
        if(floor == 0) {
            return "Erdgeschoss";
        }
        if(floor > 0) {
            return Math.abs(floor) + ". Obergeschoss";
        }
        else {
            return Math.abs(floor) + ". Untergeschoss";
        }
    }

    public int getThisFloor() {
        return thisFloor;
    }

    public boolean isValid() {
        return valid;
    }

    public String getInvalidMsg() {
        return invalidMsg;
    }

    public static boolean isElevatorSign(Sign sign) {

        return SignUtils.isLineEqual(sign.getLine(1), RCElevator.SIGN_TAG);
    }

    public static boolean isElevatorSignTag(String string) {

        return SignUtils.isLineEqual(string, RCElevator.SIGN_TAG);
    }
}
