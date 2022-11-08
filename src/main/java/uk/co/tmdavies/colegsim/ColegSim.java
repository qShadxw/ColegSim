package uk.co.tmdavies.colegsim;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.tmdavies.colegsim.commands.MainCommand;
import uk.co.tmdavies.colegsim.listeners.PlayerListener;
import uk.co.tmdavies.colegsim.objects.ColegPlayer;
import uk.co.tmdavies.colegsim.utils.ShadowConfig;
import uk.co.tmdavies.colegsim.utils.ShadowLogger;

import java.util.HashMap;

public final class ColegSim extends JavaPlugin {

    public static ShadowLogger logger;
    public static ShadowConfig mainConfig;
    public static HashMap<Player, ColegPlayer> playerStorage;

    @Override
    public void onLoad() {

        logger = new ShadowLogger();
        mainConfig = new ShadowConfig(ColegSim.class, "config.yml", false, true);
        playerStorage = new HashMap<>();

    }

    @Override
    public void onEnable() {

        new MainCommand(this);
        new PlayerListener(this);

        logger.startUp();

        if (Bukkit.getOnlinePlayers().isEmpty()) return;

        Bukkit.getOnlinePlayers().forEach(player -> playerStorage.put(player, new ColegPlayer(player)));

    }

    @Override
    public void onDisable() {

        playerStorage.values().forEach(colegPlayer -> colegPlayer.saveData());

    }
}
