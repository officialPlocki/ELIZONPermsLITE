package app.elizon.perms.bungeecord.handler;

import app.elizon.perms.pkg.player.PermPlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PermissionCheckEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class BungeePermHandler implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPermCheck(PermissionCheckEvent event) {

        PermPlayer player = new PermPlayer(ProxyServer.getInstance().getPlayer(event.getSender().getName()).getUniqueId().toString());
        event.setHasPermission(player.simpleHasPermission(event.getPermission()));

    }

}
