package app.elizon.perms.forge;

import app.elizon.perms.forge.handler.ForgePermHandler;
import app.elizon.perms.pkg.Initializer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.permission.PermissionAPI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("ELIZONPerms")
public class ForgeLoader {

    private static final Logger LOGGER = LogManager.getLogger();

    public ForgeLoader() {
        new Initializer().init();
        PermissionAPI.setPermissionHandler(new ForgePermHandler());
    }

}
