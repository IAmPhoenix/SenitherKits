package net.Senither.Kits.ulits;

import net.Senither.Kits.Kits;

public class Permissions
{

    @SuppressWarnings("unused")
    private final Kits _plugin;
    private String prefix = "kits.";

    public Permissions(Kits plugin)
    {
        _plugin = plugin;
    }
    
     public final String MANAGE_BUILD = prefix + "build";
     public final String MANAGE_ADDCREDITS = prefix + "admin.addcredits";
     public final String MANAGE_MODIFY = prefix + "admin.modify";
     public final String MANAGE_MODIFY_KILLS = prefix + "admin.modify.kills";
     public final String MANAGE_MODIFY_DEATHS = prefix + "admin.modify.deaths";
     public final String MANAGE_MODIFY_KILLSTREAK = prefix + "admin.modify.killstreak";
     public final String MANAGE_MODIFY_DUELSCORE = prefix + "admin.modify.duelscore";
     public final String MANAGE_MAP = prefix + "admin.map";
}
