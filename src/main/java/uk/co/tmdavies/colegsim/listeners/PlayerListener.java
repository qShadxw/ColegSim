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

public class PlayerListener implements Listener {

    public PlayerListener(ColegSim plugin) {

        Bukkit.getPluginManager().registerEvents(this, plugin);

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        if (event.getPlayer().isBanned()) return;

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

        // if (!(event.getEntity().getKiller() instanceof Player)) return;

        Player deadPlayer = event.getEntity();
        ColegPlayer colegPlayer = ColegSim.playerStorage.get(deadPlayer);

        colegPlayer.removePlayerLives(1);

        if (colegPlayer.getPlayerLives() != 0) return;

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long howMany = (calendar.getTimeInMillis()-System.currentTimeMillis());

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tempban " + deadPlayer.getName() + " " + (howMany/60000) + "m DeathBan");

        deadPlayer.kickPlayer("Lmao Nerd.");

    }

}
