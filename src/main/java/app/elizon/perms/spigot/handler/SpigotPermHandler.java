package app.elizon.perms.spigot.handler;

import app.elizon.perms.pkg.player.PermPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SpigotPermHandler extends PermissibleBase {

    private UUID uuid;

    public SpigotPermHandler(@Nullable Player player) {
        super(player);

        assert player != null;
        this.uuid = player.getUniqueId();
    }

    public SpigotPermHandler(@Nullable OfflinePlayer offlinePlayer) {
        super(offlinePlayer);

        assert offlinePlayer != null;
        this.uuid = offlinePlayer.getUniqueId();
    }

    @Override
    public boolean hasPermission(@NotNull String inName) {
        PermPlayer player = new PermPlayer(uuid.toString());
        return player.simpleHasPermission(inName);
    }
}
