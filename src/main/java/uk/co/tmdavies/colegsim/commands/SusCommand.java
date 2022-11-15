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

public class SusCommand implements CommandExecutor {

    public SusCommand(ColegSim plugin) {

        Objects.requireNonNull(plugin.getCommand("susbar")).setExecutor(this);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {

        if (!(sender instanceof Player)) {

            sender.sendMessage(ShadowUtils.Chat("&cOnly players may execute this command."));

            return true;

        }

        if (!sender.hasPermission("colegsim.susbar")) {

            sender.sendMessage(ShadowUtils.Chat("&cYou do not have permission to execute this command."));

            return true;

        }

        Player player = (Player) sender;
        
        if (args.length != 1) {
            
            player.sendMessage(ShadowUtils.Colour("&8[&4SusBar&8] &cInvalid Arguments. Usage: /susbar <value>"));

            return true;

        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bossbar set minecraft:0 value " + args[0]);

        return true;

    }

}