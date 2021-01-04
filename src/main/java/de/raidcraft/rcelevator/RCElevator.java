package de.raidcraft.rcelevator;

import de.raidcraft.rcelevator.listener.ElevatorListener;
import kr.entree.spigradle.annotations.PluginMain;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

/**
 * @author Philip Urban
 *
 * ELEVATOR SIGN FORMAT
 * --------------------
 *
 * 0    |   Etage O1    |
 * 1    |   [Aufzug]    |
 * 2    |   U2 - O3     |
 * 3    |   Ziel: EG    |
 *
 * text defined in ElevatorSign class!
 */
@PluginMain
public class RCElevator extends JavaPlugin {

    public final static String SIGN_TAG = ChatColor.GOLD + "[Aufzug]";

    @Getter
    @Accessors(fluent = true)
    private static RCElevator instance;

    @Getter
    private static boolean testing = false;

    public RCElevator() {
        instance = this;
    }

    public RCElevator(
            JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
        instance = this;
        testing = true;
    }

    @Override
    public void onEnable() {

        if (!testing) {
            setupListener();
        }
    }

    private void setupListener() {

        ElevatorListener elevatorListener = new ElevatorListener();
        Bukkit.getPluginManager().registerEvents(elevatorListener, this);
    }

    public static String getFormattedTag() {
        return ChatColor.GOLD + SIGN_TAG;
    }
}
