package app.elizon.perms.bungeecord.commands;

import app.elizon.perms.pkg.group.PermGroup;
import app.elizon.perms.pkg.player.PermPlayer;
import app.elizon.perms.pkg.util.MultiState;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ElizonPermsBungeeCommand extends Command implements TabExecutor {

    public ElizonPermsBungeeCommand() {
        super("elizonpermsbungee", "elizonperms.command.execute", "epb", "elizonpermsb", "elizonpb");
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {

        CommandSender player = sender;

        if(!sender.hasPermission("elizonperms.command.execute")) {
            sender.sendMessage("§f[§9EP§f] §aThis server is using the free ElizonPerms Lite permissions system by ELIZONMedia. Thanks for using!");
            return;
        }

        if (args.length < 3) {
            player.sendMessage("§f[§9EP§f] §9Usage for groups: /epb group <name> <permission|info|create|delete|rename|clone> <add|remove|set|info> <string> <true|false>");
            player.sendMessage("§f[§9EP§f] §9Usage for users: /epb user <name> <group|permission|info> <add|remove|set|info> <string> <true|false|data>");
            player.sendMessage("§f[§9EP§f] §cDATA MultiState can be set by player, but must be modified via Plugin API.");
            return;
        }

        String targetType = args[0];
        String targetName = args[1];
        String actionType = args[2];

        if (targetType.equalsIgnoreCase("group")) {
            handleGroupAction(player, targetName, actionType, args);
        } else if (targetType.equalsIgnoreCase("user")) {
            handleUserAction(player, targetName, actionType, args);
        } else {
            player.sendMessage("§f[§9EP§f] §cInvalid target type. Use 'group' or 'user'.");
        }
    }

    private void handleGroupAction(CommandSender player, String targetName, String actionType, String[] args) {
        PermGroup group = new PermGroup(targetName);

        if (!player.hasPermission("elizonperms.group." + actionType.toLowerCase())) {
            player.sendMessage("§f[§9EP§f] §cYou don't have permission to perform this action on groups.");
            return;
        }

        if (actionType.equalsIgnoreCase("permission")) {


            if(args.length < 4) {
                player.sendMessage("§f[§9EP§f] §cMissing argument.");
                return;
            }

            String action = args[3];

            switch (action.toLowerCase()) {
                case "add":
                    // Permission check for add action
                    if (!player.hasPermission("elizonperms.group.permission.add")) {
                        player.sendMessage("§f[§9EP§f] §cYou don't have permission to add permissions to groups.");
                        return;
                    }

                    if(args.length < 5) {
                        player.sendMessage("§f[§9EP§f] §cMissing argument.");
                        return;
                    }
                    group.setMultiStatePermission(args[4].toLowerCase(), MultiState.TRUE);
                    player.sendMessage("§f[§9EP§f] §aAdding permission " + args[4] + " to group " + targetName);
                    break;
                case "remove":
                    // Permission check for remove action
                    if (!player.hasPermission("elizonperms.group.permission.remove")) {
                        player.sendMessage("§f[§9EP§f] §cYou don't have permission to remove permissions from groups.");
                        return;
                    }

                    if(args.length < 5) {
                        player.sendMessage("§f[§9EP§f] §cMissing argument.");
                        return;
                    }
                    group.setMultiStatePermission(args[4].toLowerCase(), null);
                    player.sendMessage("§f[§9EP§f] §cRemoving permission " + args[4] + " from group " + targetName);
                    break;
                case "set":
                    // Permission check for set action
                    if (!player.hasPermission("elizonperms.group.permission.set")) {
                        player.sendMessage("§f[§9EP§f] §cYou don't have permission to set permissions for groups.");
                        return;
                    }
                    //value
                    if(args.length < 6) {
                        player.sendMessage("§f[§9EP§f] §cMissing argument.");
                        return;
                    }
                    MultiState state = MultiState.valueOf(args[5].toUpperCase());
                    group.setMultiStatePermission(args[4].toLowerCase(), state);
                    player.sendMessage("§f[§9EP§f] §aSetting permission " + args[4] + " (" + args[5] + ") for group " + targetName);
                    break;
                case "info":
                    // Permission check for info action
                    if (!player.hasPermission("elizonperms.group.permission.info")) {
                        player.sendMessage("§f[§9EP§f] §cYou don't have permission to view permission info for groups.");
                        return;
                    }
                    player.sendMessage("§f[§9EP§f] §bGetting info about permission in group " + targetName + "...");
                    Map<String, JSONObject> dataVals = group.getAllDataSetPermissions();
                    Map<String, Boolean> stateVals = group.getAllSimpleSetPermissions();

                    StringBuilder builder = new StringBuilder("§f[§9EP§f] §bGroup Info §f(RAW)");
                    if (!stateVals.isEmpty()) {
                        stateVals.forEach((permission, value) -> builder.append("\n").append(permission).append(" (").append(value).append(")"));
                        builder.append("\n");
                    }
                    if (!dataVals.isEmpty()) {
                        dataVals.forEach((permission, data) -> builder.append("\n").append(permission).append(" (").append(data.toString()).append(")"));
                        builder.append("\n");
                    }

                    player.sendMessage(builder.toString());
                    break;
                default:
                    player.sendMessage(
                            "§f[§9EP§f] §bCommand Help §f(/epb group " + targetName + " permission ... <string> <true|false|data>) \n" +
                                    "§f[§9EP§f] §9> §aadd\n" +
                                    "§f[§9EP§f] §9> §cremove\n" +
                                    "§f[§9EP§f] §9> §aset\n" +
                                    "§f[§9EP§f] §9> §ainfo"
                    );
                    break;
            }
        } else if (actionType.equalsIgnoreCase("create")) {
            // Permission check for create action
            if (!player.hasPermission("elizonperms.group.create")) {
                player.sendMessage("§f[§9EP§f] §cYou don't have permission to create groups.");
                return;
            }
            player.sendMessage("§f[§9EP§f] §aGroup " + targetName + " ( " + group.build() + " ) created.");
        } else if (actionType.equalsIgnoreCase("delete")) {
            // Permission check for delete action
            if (!player.hasPermission("elizonperms.group.delete")) {
                player.sendMessage("§f[§9EP§f] §cYou don't have permission to delete groups.");
                return;
            }
            group.deleteGroup();
            player.sendMessage("§f[§9EP§f] §cGroup " + targetName + " deleted successfully.");
        } else if (actionType.equalsIgnoreCase("rename")) {
            // Permission check for rename action
            if (!player.hasPermission("elizonperms.group.rename")) {
                player.sendMessage("§f[§9EP§f] §cYou don't have permission to rename groups.");
                return;
            }
            String newName = args[5];
            group.renameGroup(newName);
            player.sendMessage("§f[§9EP§f] §bGroup " + targetName + " renamed to " + newName + ".");
        } else if (actionType.equalsIgnoreCase("info")) {
            // Permission check for info action
            if (!player.hasPermission("elizonperms.group.info")) {
                player.sendMessage("§f[§9EP§f] §cYou don't have permission to view general info about groups.");
                return;
            }
            int groupSize = group.getPlayersInGroup().size();
            player.sendMessage("§f[§9EP§f] §bGetting general info about group " + targetName + " (Size: " + groupSize + ")");
        } else if (actionType.equalsIgnoreCase("clone")) {
            // Permission check for clone action
            if (!player.hasPermission("elizonperms.group.clone")) {
                player.sendMessage("§f[§9EP§f] §cYou don't have permission to clone groups.");
                return;
            }
            group.cloneGroup(args[5], false);
            player.sendMessage("§f[§9EP§f] §aCloning group " + targetName + " to group " + args[5]);
        } else {
            // Help message
            player.sendMessage(
                    "§f[§9EP§f] §bCommand Help §f(/epb group " + targetName + " <action>)\n" +
                            "§f[§9EP§f] §9> §apermission\n" +
                            "§f[§9EP§f] §9> §ainfo\n" +
                            "§f[§9EP§f] §9> §aclone"
            );
        }
    }


    private void handleUserAction(CommandSender player, String targetName, String actionType, String[] args) {
        PermPlayer permPlayer = new PermPlayer(ProxyServer.getInstance().getPlayer(targetName).getUniqueId().toString());

        if (!player.hasPermission("elizonperms.user." + actionType.toLowerCase())) {
            player.sendMessage("§f[§9EP§f] §cYou don't have permission to perform this action on users.");
            return;
        }

        if (actionType.equalsIgnoreCase("group")) {

            if(args.length < 4) {
                player.sendMessage("§f[§9EP§f] §cMissing argument.");
                return;
            }

            String action = args[3];
            switch (action.toLowerCase()) {
                case "add":
                    // Permission check for add action
                    if (!player.hasPermission("elizonperms.user.group.add")) {
                        player.sendMessage("§f[§9EP§f] §cYou don't have permission to add users to groups.");
                        return;
                    }
                    if(args.length < 5) {
                        player.sendMessage("§f[§9EP§f] §cMissing argument.");
                        return;
                    }
                    permPlayer.addGroup(args[4].toLowerCase());
                    player.sendMessage("§f[§9EP§f] §aAdding user " + targetName + " to group " + args[4]);
                    break;
                case "remove":
                    // Permission check for remove action
                    if (!player.hasPermission("elizonperms.user.group.remove")) {
                        player.sendMessage("§f[§9EP§f] §cYou don't have permission to remove users from groups.");
                        return;
                    }
                    if(args.length < 5) {
                        player.sendMessage("§f[§9EP§f] §cMissing argument.");
                        return;
                    }
                    permPlayer.removeGroup(args[4].toLowerCase());
                    player.sendMessage("§f[§9EP§f] §cRemoving user " + targetName + " from group " + args[4]);
                    break;
                case "set":
                    // Permission check for set action
                    if (!player.hasPermission("elizonperms.user.group.set")) {
                        player.sendMessage("§f[§9EP§f] §cYou don't have permission to set user groups.");
                        return;
                    }
                    if(args.length < 5) {
                        player.sendMessage("§f[§9EP§f] §cMissing argument.");
                        return;
                    }
                    permPlayer.setGroup(args[4].toLowerCase());
                    player.sendMessage("§f[§9EP§f] §aSetting group for user " + targetName + " to " + args[4]);
                    break;
                case "info":
                    // Permission check for info action
                    if (!player.hasPermission("elizonperms.user.group.info")) {
                        player.sendMessage("§f[§9EP§f] §cYou don't have permission to view group info for users.");
                        return;
                    }
                    List<String> groups = permPlayer.getGroups();
                    player.sendMessage("§f[§9EP§f] §bGroups of player: " + groups);
                    break;
                default:
                    player.sendMessage(
                            "§f[§9EP§f] §bCommand Help §f(/epb user " + targetName + " group ... <group>)\n" +
                                    "§f[§9EP§f] §9> §aadd\n" +
                                    "§f[§9EP§f] §9> §cremove\n" +
                                    "§f[§9EP§f] §9> §aset\n" +
                                    "§f[§9EP§f] §9> §ainfo"
                    );
                    break;
            }
        } else if (actionType.equalsIgnoreCase("permission")) {

            if(args.length < 4) {
                player.sendMessage("§f[§9EP§f] §cMissing argument.");
                return;
            }

            String action = args[3];

            //permission

            if(args.length < 5) {
                player.sendMessage("§f[§9EP§f] §cMissing argument.");
                return;
            }
            switch (action.toLowerCase()) {
                case "add":
                    // Permission check for add action
                    if (!player.hasPermission("elizonperms.user.permission.add")) {
                        player.sendMessage("§f[§9EP§f] §cYou don't have permission to add permissions to users.");
                        return;
                    }
                    permPlayer.setMultiStatePermission(args[4].toLowerCase(), MultiState.TRUE);
                    player.sendMessage("§f[§9EP§f] §aAdding permission " + args[4] + " to user " + targetName);
                    break;
                case "remove":
                    // Permission check for remove action
                    if (!player.hasPermission("elizonperms.user.permission.remove")) {
                        player.sendMessage("§f[§9EP§f] §cYou don't have permission to remove permissions from users.");
                        return;
                    }
                    permPlayer.setMultiStatePermission(args[4].toLowerCase(), null);
                    player.sendMessage("§f[§9EP§f] §cRemoving permission " + args[4] + " from user " + targetName);
                    break;
                case "set":
                    // Permission check for set action
                    if (!player.hasPermission("elizonperms.user.permission.set")) {
                        player.sendMessage("§f[§9EP§f] §cYou don't have permission to set permissions for users.");
                        return;
                    }
                    //value
                    if(args.length < 6) {
                        player.sendMessage("§f[§9EP§f] §cMissing argument.");
                        return;
                    }
                    permPlayer.setMultiStatePermission(args[4].toLowerCase(), MultiState.valueOf(args[5].toUpperCase()));
                    player.sendMessage("§f[§9EP§f] §aSetting permission " + args[4] + " (" + args[5] + ") for user " + targetName);
                    break;
                case "info":
                    // Permission check for info action
                    if (!player.hasPermission("elizonperms.user.permission.info")) {
                        player.sendMessage("§f[§9EP§f] §cYou don't have permission to view permission info for users.");
                        return;
                    }
                    Map<String, JSONObject> dataVals = permPlayer.getAllDataSetPermissions();
                    Map<String, Boolean> stateVals = permPlayer.getAllSimpleSetPermissions();

                    StringBuilder builder = new StringBuilder("§f[§9EP§f] §bPermission Info for user ").append(targetName).append("§f: \n");

                    // Append simple set permissions
                    builder.append("§aSimple Set Permissions§f: \n");
                    for (Map.Entry<String, Boolean> entry : stateVals.entrySet()) {
                        builder.append("§9- ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                    }

                    // Append data set permissions
                    builder.append("§aData Set Permissions§f: \n");
                    for (Map.Entry<String, JSONObject> entry : dataVals.entrySet()) {
                        builder.append("§9- ").append(entry.getKey()).append(": ").append(entry.getValue().toString()).append("\n");
                    }

                    player.sendMessage(builder.toString());
                    break;
                default:
                    player.sendMessage(
                            "§f[§9EP§f] §bCommand Help §f(/epb user " + targetName + " permission ... <string> <true|false|data>)\n" +
                                    "§f[§9EP§f] §9> §aadd\n" +
                                    "§f[§9EP§f] §9> §cremove\n" +
                                    "§f[§9EP§f] §9> §aset\n" +
                                    "§f[§9EP§f] §9> §ainfo"
                    );
                    break;
            }
        } else if (actionType.equalsIgnoreCase("info")) {
            // Permission check for info action
            if (!player.hasPermission("elizonperms.user.info")) {
                player.sendMessage("§f[§9EP§f] §cYou don't have permission to view general info about users.");
                return;
            }
            List<String> groups = permPlayer.getGroups();
            player.sendMessage("§f[§9EP§f] §bGetting general info about user " + targetName);
            player.sendMessage("§f[§9EP§f] §bGroups: " + groups);
        } else {
            player.sendMessage(
                    "§f[§9EP§f] §bCommand Help §f(/epb user " + targetName + " <action>)\n" +
                            "§f[§9EP§f] §9> §apermission\n" +
                            "§f[§9EP§f] §9> §ainfo"
            );
        }
    }

    @Nullable
    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {

        if (!sender.hasPermission("elizonperms.autocomplete")) {
            return Collections.emptyList();
        }

        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            // Complete target type (group/user)
            completions.add("group");
            completions.add("user");
        } else if (args.length == 2) {
            // Complete target name (players/groups)
            if ("group".equalsIgnoreCase(args[0])) {
                // Provide list of group names
                completions.addAll(getGroupNames());
            } else if ("user".equalsIgnoreCase(args[0])) {
                // Provide list of online player names
                completions.addAll(getOnlinePlayerNames());
                completions.add("uuid:");
            }
        } else if (args.length == 3) {
            // Complete action type (permission/info/create/delete/rename/clone)
            if ("group".equalsIgnoreCase(args[0])) {
                completions.add("permission");
                completions.add("info");
                completions.add("create");
                completions.add("delete");
                completions.add("rename");
                completions.add("clone");
            } else if ("user".equalsIgnoreCase(args[0])) {
                completions.add("group");
                completions.add("permission");
                completions.add("info");
            }
        } else if (args.length >= 4) {
            // Complete subcommands based on previous arguments
            if ("group".equalsIgnoreCase(args[0])) {
                completions.addAll(getGroupSubcommands(args));
            } else if ("user".equalsIgnoreCase(args[0])) {
                completions.addAll(getUserSubcommands(args));
            }
        }

        return completions;
    }

    private List<String> getGroupNames() {
        // Provide your list of group names here
        return new PermGroup(null).getAllGroups();
    }

    private List<String> getOnlinePlayerNames() {
        // Provide your list of online player names here

        List<String> names = new ArrayList<>();

        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            names.add(player.getName());
        }

        return names;
    }

    private List<String> getGroupSubcommands(String[] args) {
        List<String> completions = new ArrayList<>();
        String actionType = args[2];
        switch (actionType.toLowerCase()) {
            case "permission":
                completions.add("add");
                completions.add("remove");
                completions.add("info");
                completions.add("set");
                break;
            case "info":
                completions.add("permission");
                completions.add("create");
                completions.add("delete");
                completions.add("rename");
                completions.add("clone");
                break;
            case "create":
            case "delete":
                break; // Command ends, no further completions needed
            case "rename":
            case "clone":
                // No completions for the new group name
                break;
        }
        return completions;
    }

    private List<String> getUserSubcommands(String[] args) {
        List<String> completions = new ArrayList<>();
        String actionType = args[2];
        switch (actionType.toLowerCase()) {
            case "group":
                completions.add("add");
                completions.add("set");
                completions.add("remove");
                break;
            case "permission":
                completions.add("add");
                completions.add("remove");
                completions.add("set");
                completions.add("info");
                break;
            case "info":
                completions.add("permission");
                completions.add("info");
                break;
        }
        return completions;
    }
}
