package net.Senither.Kits.api.events;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChangeMapEvent extends Event
{

    private static final HandlerList handlers = new HandlerList();
    private final Location location;
    private final String name;

    public ChangeMapEvent(Location location, String name)
    {
        this.location = location;
        this.name = name;
    }

    public Location getArenaLocation()
    {
        return location;
    }

    public String getArenaName()
    {
        return name;
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
