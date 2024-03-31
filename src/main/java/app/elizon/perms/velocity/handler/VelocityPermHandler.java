package app.elizon.perms.velocity.handler;

import app.elizon.perms.pkg.player.PermPlayer;
import app.elizon.perms.velocity.VelocityLoader;
import com.velocitypowered.api.permission.PermissionFunction;
import com.velocitypowered.api.permission.PermissionProvider;
import com.velocitypowered.api.permission.PermissionSubject;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.Player;

public class VelocityPermHandler implements PermissionProvider {

    private PermissionSubject subject;

    public VelocityPermHandler(PermissionSubject subject) {
        this.subject = subject;
    }

    @Override
    public PermissionFunction createFunction(PermissionSubject permissionSubject) {
        return new PermissionFunctionable(permissionSubject, this.createFunction(permissionSubject));
    }

    private final class PermissionFunctionable implements PermissionFunction {

        private PermissionSubject subject;
        private PermissionFunction function;

        PermissionFunctionable(PermissionSubject subject, PermissionFunction function) {
            this.function = function;
            this.subject = subject;
        }

        @Override
        public Tristate getPermissionValue(String perm) {
            PermPlayer player = new PermPlayer(((Player) VelocityPermHandler.this.subject).getUniqueId().toString());
            return Tristate.fromBoolean(player.simpleHasPermission(perm));
        }
    }

}
