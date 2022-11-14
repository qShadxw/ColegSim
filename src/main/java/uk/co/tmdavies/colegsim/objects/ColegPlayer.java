package uk.co.tmdavies.colegsim.objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import uk.co.tmdavies.colegsim.ColegSim;
import uk.co.tmdavies.colegsim.utils.ShadowUtils;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

public class ColegPlayer {

    private final Player player;
    private int playerKills;
    private int playerLives;
    private boolean isDead;
    private long loginTime;
    private long tomorrowTime;

    private String target;
    private boolean targetKilled;

    public ColegPlayer(Player player) {

        this.player = player;
        this.playerKills = 0;
        this.playerLives = 1;
        this.isDead = false;
        this.loginTime = new Date().getTime();
        this.tomorrowTime = ShadowUtils.getTimeTomorrow();
        
        if (ColegSim.mainConfig.getString(this.player.getName() + ".UUID") != null) {

            this.playerKills = ColegSim.mainConfig.getInt(this.player.getName() + ".Kills");
            this.playerLives = ColegSim.mainConfig.getInt(this.player.getName() + ".Lives");
            this.isDead = ColegSim.mainConfig.getBoolean(this.player.getName() + ".IsDead");
            this.tomorrowTime = ColegSim.mainConfig.getLong(this.player.getName() + ".DailyReset");
            this.target = ColegSim.mainConfig.getString(this.player.getName() + ".Bounty");
            this.targetKilled = ColegSim.mainConfig.getBoolean(this.player.getName() + ".BountyKilled");

        }

        if (this.target == null) this.selectRandomTarget();

        this.doDailyReset();

    }

    public Player getPlayer() {

        return this.player;

    }

    public int getPlayerKills() {

        return this.playerKills;

    }

    public int getPlayerLives() {

        return this.playerLives;

    }

    public boolean getTargetKilled() {

        return this.targetKilled;

    }

    public boolean isDead() {

        return this.isDead;

    }

    public String getTarget() {

        return this.target;

    }

    public long getLoginTime() {

        return this.loginTime;

    }
    
    public long getTomorrowTime() {

        return this.tomorrowTime;

    }

    public void addPlayerKills(int amount) {

        this.playerKills+=amount;

    }

    public void addPlayerLives(int amount) {

        this.playerLives+=amount;

    }

    public void removePlayerKills(int amount) {

        this.playerKills-=amount;

    }

    public void removePlayerLives(int amount) {

        this.playerLives-=amount;

    }

    public void setPlayerKills(int amount) {

        this.playerKills = amount;

    }

    public void setPlayerLives(int amount) {

        this.playerLives = amount;

    }

    public void setDead(boolean isDead) {

        this.isDead = isDead;

    }

    public void setTarget(String target) {

        this.target = target;

    }

    public void setTargetKilled(boolean killed) {

        this.targetKilled = killed;

    }

    public void selectRandomTarget() {

        List<String> players = ColegSim.playerConfig.getStringList("Players");
        Random random = new Random();

        if (players.isEmpty()) return;

        players.forEach(string -> this.player.sendMessage(ShadowUtils.Chat(string)));

        this.target = players.get(random.nextInt(players.size()));

        this.player.sendMessage(ShadowUtils.Chat("&fYour bounty today is: " + this.getTarget()));

    }

    public void doDailyReset() {

        if (this.loginTime < this.tomorrowTime) return;

        this.selectRandomTarget();
        this.targetKilled = false;

        this.player.sendMessage(ShadowUtils.Chat("&fYour bounty today is: " + this.getTarget()));

        this.tomorrowTime = ShadowUtils.getTimeTomorrow();

    }

    public void killedTarget() {

        this.player.sendMessage(ShadowUtils.Chat("&aYou have killed your bounty, Congratulations!"));
        this.player.sendMessage(ShadowUtils.Chat(" %d| &a+1 Life"));

        this.targetKilled = true;
        this.playerLives += 1;

    }

    public void playerDeath() {

        this.playerLives -= 1;

        if (this.playerLives > 0) return;

        long howMany = ShadowUtils.getTimeTillTomorrow();

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tempban " + this.player.getName() + " " + (howMany/60000) + "m DeathBan");

        this.player.kickPlayer("You have died! Nerd!");

    }

    public void saveData() {

        ColegSim.mainConfig.add(this.player.getName() + ".UUID", this.player.getUniqueId().toString());
        ColegSim.mainConfig.add(this.player.getName() + ".Kills", this.playerKills);
        ColegSim.mainConfig.add(this.player.getName() + ".Lives", this.playerLives);
        ColegSim.mainConfig.add(this.player.getName() + ".IsDead", this.isDead);
        ColegSim.mainConfig.add(this.player.getName() + ".LastLogin", this.loginTime);
        ColegSim.mainConfig.add(this.player.getName() + ".DailyReset", this.tomorrowTime);
        ColegSim.mainConfig.add(this.player.getName() + ".Bounty", this.target);
        ColegSim.mainConfig.add(this.player.getName() + ".BountyKilled", this.targetKilled);

        ColegSim.mainConfig.save();

    }

}
