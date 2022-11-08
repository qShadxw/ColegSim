package uk.co.tmdavies.colegsim.objects;

import org.bukkit.entity.Player;
import uk.co.tmdavies.colegsim.ColegSim;

public class ColegPlayer {

    private final Player player;
    private int playerKills;
    private int playerLives;
    private boolean isDead;

    public ColegPlayer(Player player) {

        this.player = player;
        this.playerKills = 0;
        this.playerLives = 1;
        this.isDead = false;

        if (ColegSim.mainConfig.getString(this.player.getName() + ".UUID") == null) return;

        this.playerKills = ColegSim.mainConfig.getInt(this.player.getName() + ".Kills");
        this.playerLives = ColegSim.mainConfig.getInt(this.player.getName() + ".Lives");
        this.isDead = ColegSim.mainConfig.getBoolean(this.player.getName() + ".IsDead");

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

    public boolean isDead() {

        return this.isDead;

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

    public void saveData() {

        ColegSim.mainConfig.add(this.player.getName() + ".UUID", this.player.getUniqueId().toString());
        ColegSim.mainConfig.add(this.player.getName() + ".Kills", this.playerKills);
        ColegSim.mainConfig.add(this.player.getName() + ".Lives", this.playerLives);
        ColegSim.mainConfig.add(this.player.getName() + ".IsDead", this.isDead);

        ColegSim.mainConfig.save();


    }

}
