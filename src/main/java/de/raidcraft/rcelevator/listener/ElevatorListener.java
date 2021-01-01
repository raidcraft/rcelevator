package de.raidcraft.rcelevator.listener;

import de.raidcraft.rcelevator.ElevatorSign;
import de.raidcraft.rcelevator.RCElevator;
import de.raidcraft.rcelevator.utils.SignUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Stack;

/**
 * @author Philip Urban
 */
public class ElevatorListener implements Listener {

    private Stack<Location> lockedTriggerLocations = new Stack<>();

    @EventHandler(ignoreCancelled = true)
    public void onSignChange(SignChangeEvent event) {
        if(!SignUtils.isLineEqual(event.getLine(1), RCElevator.SIGN_TAG)){
            return;
        }

        if(!event.getPlayer().hasPermission("elevatorpro.build")) {
            event.getPlayer().sendMessage(ChatColor.RED + "You don't have permission!");
            event.setCancelled(true);
            return;
        }

        ElevatorSign elevatorSign = new ElevatorSign(event.getLines());

        if(!elevatorSign.isValid()) {
            event.getPlayer().sendMessage(ChatColor.RED + "Das Schild ist falsch formatiert!");
            event.getPlayer().sendMessage(ChatColor.RED + "Fehler: " + elevatorSign.getInvalidMsg());
            event.setCancelled(true);
            return;
        }

        event.setLine(1, RCElevator.getFormattedTag());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getClickedBlock() == null || !(event.getClickedBlock().getState() instanceof Sign)) {
            return;
        }

        Sign sign = (Sign)event.getClickedBlock().getState();

        if(!SignUtils.isLineEqual(sign.getLine(1), RCElevator.SIGN_TAG)) {
            return;
        }

        ElevatorSign elevatorSign = new ElevatorSign(sign);

        if(!elevatorSign.isValid()) {
            event.getPlayer().sendMessage(ChatColor.RED + "Das Schild ist falsch formatiert!");
            event.getPlayer().sendMessage(ChatColor.RED + "Fehler: " + elevatorSign.getInvalidMsg());
            return;
        }
        if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
            elevatorSign.increaseTarget();
        }
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            elevatorSign.decreaseTarget();
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPowered(BlockRedstoneEvent event) {
        int current = event.getNewCurrent();

        Sign sign = null;

        // check if trigger is already processed
        if(lockedTriggerLocations.contains(event.getBlock().getLocation()) && current > 0) {
            return;
        }

        // remove trigger if current returns to zero
        if(current == 0) {
            lockedTriggerLocations.remove(event.getBlock().getLocation());
            return;
        }

        // add trigger to ignore oncoming
        lockedTriggerLocations.push(event.getBlock().getLocation());

        // check nearby blocks
        do {
            // y+1
            sign = getSign(event.getBlock().getRelative(0, 1, 0));
            if(sign != null) break;
            // y-1
            sign = getSign(event.getBlock().getRelative(0, -1, 0));
            if(sign != null) break;
            // x+1
            sign = getSign(event.getBlock().getRelative(1, 0, 0));
            if(sign != null) break;
            // x-1
            sign = getSign(event.getBlock().getRelative(-1, 0, 0));
            if(sign != null) break;
            // z+1
            sign = getSign(event.getBlock().getRelative(0, 0, 1));
            if(sign != null) break;
            // z-1
            sign = getSign(event.getBlock().getRelative(0, 0, -1));
            break;
        } while(false);

        if(sign == null) return;

//        RaidCraft.LOGGER.info("[RCElevator] Sign Redstone event at: " +
//                "x=" + sign.getLocation().getBlockX() + "|" +
//                "y=" + sign.getLocation().getBlockY() + "|" +
//                "z=" + sign.getLocation().getBlockZ());

        if(!SignUtils.isLineEqual(sign.getLine(1), RCElevator.SIGN_TAG)) {
            return;
        }

        ElevatorSign elevatorSign = new ElevatorSign(sign);

        if(!elevatorSign.isValid()) {
            RCElevator.instance().getLogger().info("RCElevator: Invalid sign at: " +
                    "x=" + sign.getLocation().getBlockX() + "|" +
                    "y=" + sign.getLocation().getBlockY() + "|" +
                    "z=" + sign.getLocation().getBlockZ());
            return;
        }

        if(!elevatorSign.teleportPlayersToTarget())
        {
            RCElevator.instance().getLogger().info("RCElevator was unable to teleport passengers! Maybe triggered by none player? (Sign at: " +
                    "x=" + sign.getLocation().getBlockX() + "|" +
                    "y=" + sign.getLocation().getBlockY() + "|" +
                    "z=" + sign.getLocation().getBlockZ());
        }
    }

    private Sign getSign(Block block)
    {
        if(!SignUtils.isWallSign(block.getType())
                && !SignUtils.isPostSign(block.getType())) {
            return null;
        }

        return (Sign)block.getState();
    }
}
