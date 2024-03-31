package app.elizon.perms.forge.handler;

import app.elizon.perms.pkg.player.PermPlayer;
import com.mojang.authlib.GameProfile;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.IPermissionHandler;
import net.minecraftforge.server.permission.context.IContext;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ForgePermHandler implements IPermissionHandler {

    private final Map<String, DefaultPermissionLevel> nodes = new HashMap<>();
    private final Map<String, String> descriptions = new HashMap<>();

    @Override
    public void registerNode(@NotNull String node, @NotNull DefaultPermissionLevel level, @NotNull String desc) {
        nodes.put(node, level);
        descriptions.put(node, desc);
    }

    @Override
    public Collection<String> getRegisteredNodes() {
        return nodes.keySet().stream().toList();
    }

    @Override
    public boolean hasPermission(@NotNull GameProfile profile, @NotNull String node, IContext context) {
        PermPlayer player = new PermPlayer(profile.getId().toString());
        return player.simpleHasPermission(node);
    }

    @Override
    public String getNodeDescription(@NotNull String node) {
        return descriptions.get(node);
    }
}
