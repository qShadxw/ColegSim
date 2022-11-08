package uk.co.tmdavies.colegsim.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import uk.co.tmdavies.colegsim.ColegSim;
import uk.co.tmdavies.colegsim.objects.ColegPlayer;
import uk.co.tmdavies.colegsim.utils.ShadowUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainCommand implements CommandExecutor, TabCompleter {

    public MainCommand(ColegSim plugin) {

        Objects.requireNonNull(plugin.getCommand("colegsim")).setExecutor(this);
        Objects.requireNonNull(plugin.getCommand("colegsim")).setTabCompleter(this);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {

        if (!sender.hasPermission("colegsim.admin")) {

            sender.sendMessage(ShadowUtils.Chat("&cYou do not have the permission to execute this command."));

            return true;

        }

        switch (args.length) {

            case 2:

                Player targetPlayer;
                ColegPlayer colegPlayer;

                switch (args[0]) {

                    case "dump" -> {

                        targetPlayer = Bukkit.getPlayer(args[1]);

                        if (targetPlayer == null || !targetPlayer.isOnline()) {

                            sender.sendMessage(ShadowUtils.Chat("Invalid Target."));

                            break;

                        }

                        colegPlayer = ColegSim.playerStorage.get(targetPlayer);

                        sender.sendMessage(ShadowUtils.Chat("%s" + targetPlayer.getName() + "'s Stats:"));
                        sender.sendMessage(ShadowUtils.Chat(" %d| %sKills: %p" + colegPlayer.getPlayerKills()));
                        sender.sendMessage(ShadowUtils.Chat(" %d| %sLives: %p" + colegPlayer.getPlayerLives()));
                        sender.sendMessage(ShadowUtils.Chat(" %d| %sIsDead: %p" + colegPlayer.isDead()));

                    }

                    case "reset" -> {

                        targetPlayer = Bukkit.getPlayer(args[1]);

                        if (targetPlayer == null || !targetPlayer.isOnline()) {

                            sender.sendMessage(ShadowUtils.Chat("Invalid Target."));

                            break;

                        }

                        colegPlayer = ColegSim.playerStorage.get(targetPlayer);

                        colegPlayer.setPlayerKills(0);
                        colegPlayer.setPlayerLives(1);
                        colegPlayer.setDead(false);

                    }

                    default -> sender.sendMessage(ShadowUtils.Chat("&cInvalid Arguments."));

                }

                break;

            default:

                sender.sendMessage(ShadowUtils.Chat("&cInvalid Arguments."));

                break;

        }

        return true;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String string, String[] args) {

        List<String> noArguments = new ArrayList<>();
        List<String> oneArguments = new ArrayList<>();

        switch (args.length) {

            case 1:

                noArguments.add("dump");
                noArguments.add("reset");

                return noArguments;

            case 2:

                if (args[0].equalsIgnoreCase("dump") || args[0].equalsIgnoreCase("reset")) {

                    Bukkit.getOnlinePlayers().forEach(player -> oneArguments.add(player.getName()));

                }

                return oneArguments;

            default:

                return new ArrayList<>();

        }

    }
}
