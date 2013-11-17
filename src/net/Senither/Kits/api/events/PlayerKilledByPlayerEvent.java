package net.Senither.Kits.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerKilledByPlayerEvent extends Event
{

    private static final HandlerList handlers = new HandlerList();
    private final Player killer;
    private final Player target;
    private final int killerKillstreak;
    private final int targetKillstreak;
    private final int killerCredits;
    private final int targetCredits;

    public PlayerKilledByPlayerEvent(Player killer, int killerKillstreak, int killerCredits, Player target, int targetKillstreak, int targetCredits)
    {
        this.killer = killer;
        this.killerKillstreak = killerKillstreak;
        this.killerCredits = killerCredits;

        this.target = target;
        this.targetKillstreak = targetKillstreak;
        this.targetCredits = targetCredits;
    }

    public Player getPlayer()
    {
        return killer;
    }

    public Player getTarget()
    {
        return target;
    }

    public int getPlayerKillstreak()
    {
        return killerKillstreak;
    }

    public int getTargetKillstreak()
    {
        return targetKillstreak;
    }

    public int getKillerCredits()
    {
        return killerCredits;
    }

    public int getTargetCredits()
    {
        return targetCredits;
    }

    public HandlerList getHandlers()
    {
        return handlers;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }
}
