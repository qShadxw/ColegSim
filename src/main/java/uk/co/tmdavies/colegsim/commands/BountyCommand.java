package uk.co.tmdavies.colegsim.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.co.tmdavies.colegsim.ColegSim;
import uk.co.tmdavies.colegsim.objects.ColegPlayer;
import uk.co.tmdavies.colegsim.utils.ShadowUtils;

import java.util.Objects;

public class BountyCommand implements CommandExecutor {

    public BountyCommand(ColegSim plugin) {

        Objects.requireNonNull(plugin.getCommand("bounty")).setExecutor(this);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {

        if (!(sender instanceof Player)) {

            sender.sendMessage(ShadowUtils.Chat("&cOnly players may execute this command."));

            return true;

        }

        if (!sender.hasPermission("colegsim.bounty")) {

            sender.sendMessage(ShadowUtils.Chat("&cYou do not have permission to execute this command."));

            return true;

        }

        Player player = (Player) sender;
        ColegPlayer colegPlayer = ColegSim.playerStorage.get(player);

        player.sendMessage(ShadowUtils.Colour("&8[&cBounty&8] &fYour bounty is: &c" + colegPlayer.getTarget()));

        return true;

    }

}