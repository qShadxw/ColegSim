package uk.co.tmdavies.colegsim.listeners;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import uk.co.tmdavies.colegsim.ColegSim;
import uk.co.tmdavies.colegsim.objects.ColegPlayer;
import uk.co.tmdavies.colegsim.utils.ShadowUtils;

import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class PlayerListener implements Listener {

    public PlayerListener(ColegSim plugin) {

        Bukkit.getPluginManager().registerEvents(this, plugin);

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (event.getPlayer().isBanned()) return;

        List<String> playerNames = new ArrayList<>();

        if (!ColegSim.playerConfig.getStringList("Players").isEmpty())
        
            ColegSim.playerConfig.getStringList("Players").forEach(string -> playerNames.add(string));

        if (!playerNames.contains(event.getPlayer().getName())) {

            playerNames.add(event.getPlayer().getName());
            
            ColegSim.playerConfig.add("Players", playerNames);

            ColegSim.playerConfig.save();

        }

        ColegSim.playerStorage.put(event.getPlayer(), new ColegPlayer(event.getPlayer()));

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {

        if (!ColegSim.playerStorage.containsKey(event.getPlayer())) return;

        ColegSim.playerStorage.get(event.getPlayer()).saveData();
        ColegSim.playerStorage.remove(event.getPlayer());

    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {

        if (!(event.getEntity().getKiller() instanceof Player)) return;

        ColegPlayer colegPlayer = ColegSim.playerStorage.get(event.getEntity());
        ColegPlayer colegPlayerKiller = ColegSim.playerStorage.get(event.getEntity().getKiller());

        Bukkit.broadcastMessage(ColegSim.mainConfig.getString("Death.Announcement")
            .replace("%player%", colegPlayer.getPlayer().getName())
            .replace("%target%", colegPlayerKiller.getPlayer().getName()));

        if (event.getEntity().getKiller().getName().equals(event.getEntity().getName())) {
            // Player killed self
            // If Target is Self
            if (colegPlayer.getTarget() == colegPlayerKiller.getPlayer().getName()) {

                if (!colegPlayer.getTargetKilled()) colegPlayer.killedTarget();
                colegPlayer.playerDeath();

                return;

            }

        }

        colegPlayer.playerDeath();
        
        if (colegPlayerKiller.getTarget().equals(colegPlayer.getPlayer().getName())) colegPlayerKiller.killedTarget();

    }

}
