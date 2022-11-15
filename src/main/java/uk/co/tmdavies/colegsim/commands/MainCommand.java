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

                switch (args[0].toLowerCase()) {

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
                        sender.sendMessage(ShadowUtils.Chat(" %d| %sLastLogin: %p" + colegPlayer.getLoginTime()));
                        sender.sendMessage(ShadowUtils.Chat(" %d| %sDailyReset: %p" + colegPlayer.getTomorrowTime()));
                        sender.sendMessage(ShadowUtils.Chat(" %d| %sTarget: %p" + colegPlayer.getTarget()));
                        sender.sendMessage(ShadowUtils.Chat(" %d| %sTargetKilled: %p" + colegPlayer.getTargetKilled()));

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

                case 4:

                    if (args[0].equalsIgnoreCase("set")) {

                        if (args.length != 4) {

                            sender.sendMessage(ShadowUtils.Chat("Invalid Arguments. Usage: /colegsim set <name> <stat> <value>"));
    
                            return true;
    
                        }
    
                        Player target = Bukkit.getPlayer(args[1]);
    
                        if (target == null || !target.isOnline()) {
    
                            sender.sendMessage(ShadowUtils.Chat("&cInvalid Target"));
    
                            return true;
    
                        }
    
                        ColegPlayer colegTargetPlayer = ColegSim.playerStorage.get(target); 
    
                        switch (args[2].toLowerCase()) {
    
                            case "kills" -> {
    
                                colegTargetPlayer.setPlayerKills(Integer.parseInt(args[3]));
    
                                sender.sendMessage(ShadowUtils.Chat("&aSuccessfully set " + target.getName() + "'s 'kills' to " + args[3] + "."));
    
                                return true;
    
                            }
    
                            case "lives" -> {
                                
                                colegTargetPlayer.setPlayerLives(Integer.parseInt(args[3]));
    
                                sender.sendMessage(ShadowUtils.Chat("&aSuccessfully set " + target.getName() + "'s 'lives' to " + args[3] + "."));
    
                                if (colegTargetPlayer.getPlayerLives() == 0) colegTargetPlayer.playerDeath();

                                return true;
    
                            }
    
                            case "isdead" -> {
                                
                                colegTargetPlayer.setDead(Boolean.getBoolean(args[3]));
    
                                sender.sendMessage(ShadowUtils.Chat("&aSuccessfully set " + target.getName() + "'s 'isdead' to " + args[3] + "."));

                                colegTargetPlayer.setPlayerLives(0);
                                colegTargetPlayer.playerDeath();
    
                                return true;
    
                            }
    
                            case "target" -> {
                                
                                colegTargetPlayer.setTarget(args[3]);
    
                                sender.sendMessage(ShadowUtils.Chat("&aSuccessfully set " + target.getName() + "'s 'target' to " + args[3] + "."));
    
                                return true;
    
                            }
    
                            case "targetkilled" -> {
                                
                                colegTargetPlayer.setTargetKilled(Boolean.getBoolean(args[3]));
    
                                sender.sendMessage(ShadowUtils.Chat("&aSuccessfully set " + target.getName() + "'s 'targetkilled' to " + args[3] + "."));
    
                                return true;
    
                            }
    
                            default -> sender.sendMessage(ShadowUtils.Chat("&cInvalid Arguments."));
    
                        }

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

        List<String> argumentsList = new ArrayList<>();

        switch (args.length) {

            case 1:

                argumentsList.add("dump");
                argumentsList.add("reset");
                argumentsList.add("set");

                return argumentsList;

            case 2:

                switch(args[0].toLowerCase()) {

                    case "dump", "reset", "set" -> Bukkit.getOnlinePlayers().forEach(player -> argumentsList.add(player.getName()));

                }

                return argumentsList;

            case 3:

                switch(args[0].toLowerCase()) {

                    case "set" -> {

                        argumentsList.add("kills");
                        argumentsList.add("lives");
                        argumentsList.add("isdead");
                        argumentsList.add("target");
                        argumentsList.add("targetkilled");

                    }

                }

            case 4:

                switch(args[2].toLowerCase()) {

                    case "kills", "lives" -> {

                        for (int i = 0; i < 100; i++) argumentsList.add(String.valueOf(i));

                    }

                    case "isdead", "targetkilled" -> {

                        argumentsList.add("true");
                        argumentsList.add("false");

                    }

                    case "target" -> {

                        Bukkit.getOnlinePlayers().forEach(players -> argumentsList.add(players.getName()));

                    }

                }

        }

        return argumentsList;

    }
}
