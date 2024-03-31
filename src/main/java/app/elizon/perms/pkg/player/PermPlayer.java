package app.elizon.perms.pkg.player;

import app.elizon.perms.pkg.Initializer;
import app.elizon.perms.pkg.exception.IllegalMultiStateException;
import app.elizon.perms.pkg.group.PermGroup;
import app.elizon.perms.pkg.util.MultiState;
import co.plocki.mysql.MySQLInsert;
import co.plocki.mysql.MySQLPush;
import co.plocki.mysql.MySQLRequest;
import co.plocki.mysql.MySQLResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermPlayer {

    private String uuid;

    public PermPlayer(String uuid) {
        this.uuid = uuid;

        MySQLRequest request = new MySQLRequest();
        request.prepare(Initializer.getPlayerTable().getTableName());
        request.addRequirement("uuid", uuid);
        MySQLResponse response = request.execute();

        if (response.isEmpty()) {
            // Player does not exist, insert into database
            MySQLInsert insert = new MySQLInsert();
            insert.prepare(Initializer.getPlayerTable(), uuid, "{\"permissions\": {}}", "{\"groups\": []}");
            insert.execute();
        }
    }

    public void setMultiStatePermission(String permission, @Nullable MultiState state) {
        if (state == MultiState.DATA) {
            new IllegalMultiStateException("Trying to set DATA Permission in simple state").printStackTrace();
            return;
        } else if (state == null) {
            // Remove permission
            MySQLRequest request = new MySQLRequest();
            request.prepare("permissionsJson", Initializer.getPlayerTable().getTableName());
            request.addRequirement("uuid", uuid);
            JSONObject obj = new JSONObject((String) request.execute().get("permissionsJson"));
            JSONObject object = obj.getJSONObject("permissions");
            object.remove(permission.toLowerCase());
            obj.put("permissions", object);
            MySQLPush push = new MySQLPush();
            push.prepare(Initializer.getPlayerTable().getTableName(), "permissionsJson", obj.toString());
            push.addRequirement("uuid", uuid);
            push.execute();
            return;
        }

        MySQLRequest request = new MySQLRequest();
        request.prepare("permissionsJson", Initializer.getPlayerTable().getTableName());
        request.addRequirement("uuid", uuid);
        JSONObject obj = new JSONObject((String) request.execute().get("permissionsJson"));
        JSONObject object = obj.getJSONObject("permissions");

        JSONObject permObj = new JSONObject();
        permObj.put("state", state.name());

        object.put(permission.toLowerCase(), permObj);

        obj.put("permissions", object);

        MySQLPush push = new MySQLPush();
        push.prepare(Initializer.getPlayerTable().getTableName(), "permissionsJson", obj.toString());
        push.addRequirement("uuid", uuid);
        push.execute();
    }

    public void setMultiStatePermission(String permission, @NotNull MultiState state, JSONObject object) {
        MySQLRequest request = new MySQLRequest();
        request.prepare("permissionsJson", Initializer.getPlayerTable().getTableName());
        request.addRequirement("uuid", uuid);
        JSONObject obj = new JSONObject((String) request.execute().get("permissionsJson"));
        JSONObject permsObject = obj.getJSONObject("permissions");

        JSONObject permObj = new JSONObject();
        permObj.put("state", state.name());
        permObj.put("data", object);

        permsObject.put(permission.toLowerCase(), permObj);

        obj.put("permissions", permsObject);

        MySQLPush push = new MySQLPush();
        push.prepare(Initializer.getPlayerTable().getTableName(), "permissionsJson", obj.toString());
        push.addRequirement("uuid", uuid);
        push.execute();
    }


    public void addGroup(String name) {
        MySQLRequest request = new MySQLRequest();
        request.prepare("groupsJson", Initializer.getPlayerTable().getTableName());
        request.addRequirement("uuid", uuid);
        JSONObject obj = new JSONObject((String) request.execute().get("groupsJson"));
        JSONArray array = obj.getJSONArray("groups");
        array.put(name.toLowerCase());
        obj.put("groups", array);
        MySQLPush push = new MySQLPush();
        push.prepare(Initializer.getPlayerTable().getTableName(), "groupsJson", obj.toString());
        push.addRequirement("uuid", uuid);
        push.execute();
    }


    public void removeGroup(String name) {
        MySQLRequest request = new MySQLRequest();
        request.prepare("groupsJson", Initializer.getPlayerTable().getTableName());
        request.addRequirement("uuid", uuid);
        JSONObject obj = new JSONObject((String) request.execute().get("groupsJson"));
        JSONArray array = obj.getJSONArray("groups");

        List<String> updatedGroups = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            String groupName = array.getString(i);
            if (!groupName.equals(name.toLowerCase())) {
                updatedGroups.add(groupName);
            }
        }

        obj.put("groups", new JSONArray(updatedGroups));
        MySQLPush push = new MySQLPush();
        push.prepare(Initializer.getPlayerTable().getTableName(), "groupsJson", obj.toString());
        push.addRequirement("uuid", uuid);
        push.execute();
    }


    public void setGroup(String name) {
        MySQLRequest request = new MySQLRequest();
        request.prepare("groupsJson", Initializer.getPlayerTable().getTableName());
        request.addRequirement("uuid", uuid);
        JSONObject obj = new JSONObject((String) request.execute().get("groupsJson"));
        JSONArray array = new JSONArray();
        array.put(name.toLowerCase());
        obj.put("groups", array);
        MySQLPush push = new MySQLPush();
        push.prepare(Initializer.getPlayerTable().getTableName(), "groupsJson", obj.toString());
        push.addRequirement("uuid", uuid);
        push.execute();
    }

    public List<String> getGroups() {
        MySQLRequest request = new MySQLRequest();
        request.prepare("groupsJson", Initializer.getPlayerTable().getTableName());
        request.addRequirement("uuid", uuid);
        JSONObject obj = new JSONObject((String) request.execute().get("groupsJson"));
        JSONArray array = obj.getJSONArray("groups");

        List<String> groups = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            groups.add(array.getString(i));
        }

        return groups;
    }

    public Map<String, Boolean> getAllSimpleSetPermissions() {
        MySQLRequest request = new MySQLRequest();
        request.prepare("permissionsJson", Initializer.getPlayerTable().getTableName());
        request.addRequirement("uuid", uuid);
        JSONObject obj = new JSONObject((String) request.execute().get("permissionsJson"));
        JSONObject permsObject = obj.getJSONObject("permissions");

        Map<String, Boolean> permissions = new HashMap<>();
        for (String permission : permsObject.keySet()) {
            JSONObject permObj = permsObject.getJSONObject(permission);
            MultiState state = MultiState.valueOf(permObj.getString("state"));
            if (state == MultiState.DATA) {
                JSONObject data = permObj.getJSONObject("data");
                if (data.has("simple")) {
                    permissions.put(permission, Boolean.parseBoolean(data.getString("simple")));
                }
            } else {
                permissions.put(permission, Boolean.parseBoolean(state.name()));
            }
        }

        return permissions;
    }

    public Map<String, JSONObject> getAllDataSetPermissions() {
        MySQLRequest request = new MySQLRequest();
        request.prepare("permissionsJson", Initializer.getPlayerTable().getTableName());
        request.addRequirement("uuid", uuid);
        JSONObject obj = new JSONObject((String) request.execute().get("permissionsJson"));
        JSONObject permsObject = obj.getJSONObject("permissions");

        Map<String, JSONObject> permissions = new HashMap<>();
        for (String permission : permsObject.keySet()) {
            JSONObject permObj = permsObject.getJSONObject(permission);
            MultiState state = MultiState.valueOf(permObj.getString("state"));
            if (state == MultiState.DATA) {
                permissions.put(permission, permObj.getJSONObject("data"));
            }
        }

        return permissions;
    }


    public boolean simpleHasPermission(String permission) {
        // Check if the permission string itself is *

        permission = permission.toLowerCase();

        if(getAllSimpleSetPermissions().containsKey("*")) {
            if(getAllSimpleSetPermissions().get("*")) {
                return true;
            }
        }

        // Check if the player is in any group and if that group has the permission
        for (String group : getGroups()) {
            PermGroup permGroup = new PermGroup(group);
            if (permGroup.simpleHasPermission(permission)) {
                return true;
            }
        }

        if(permission.contains(".")) {
            // Split the permission string at each dot (.)
            String[] sections = permission.split("\\.");

            // Iterate over the sections to build wildcard patterns
            StringBuilder wildcardBuilder = new StringBuilder();
            for (String section : sections) {
                wildcardBuilder.append(section).append(".");
                String wildcardPermission = wildcardBuilder + "*";

                // Check if the wildcard permission matches any permission in the database
                for (String perm : getAllSimpleSetPermissions().keySet()) {
                    if (perm.startsWith(wildcardPermission)) {
                        return getAllSimpleSetPermissions().get(perm);
                    }
                }
            }
        }

        // Check if the permission is partially in the database
        for (String perm : getAllSimpleSetPermissions().keySet()) {
            // If the permission is found in the database
            if (perm.equals(permission)) {
                return getAllSimpleSetPermissions().get(permission);
            }
        }

        return false;
    }


    public @Nullable JSONObject getPermissionData(String permission) {
        MySQLRequest request = new MySQLRequest();
        request.prepare("permissionsJson", Initializer.getPlayerTable().getTableName());
        request.addRequirement("uuid", uuid);
        JSONObject obj = new JSONObject((String) request.execute().get("permissionsJson"));
        JSONObject permsObject = obj.getJSONObject("permissions");

        if (permsObject.has(permission)) {
            MultiState state = MultiState.valueOf(permsObject.getJSONObject(permission).getString("state"));

            if (state == MultiState.DATA) {
                return permsObject.getJSONObject(permission).getJSONObject("data");
            }
        }
        return null;
    }

    public boolean permissionHasData(String permission) {
        MySQLRequest request = new MySQLRequest();
        request.prepare("permissionsJson", Initializer.getPlayerTable().getTableName());
        request.addRequirement("uuid", uuid);
        JSONObject obj = new JSONObject((String) request.execute().get("permissionsJson"));
        JSONObject permsObject = obj.getJSONObject("permissions");

        if (permsObject.has(permission)) {
            MultiState state = MultiState.valueOf(permsObject.getJSONObject(permission).getString("state"));

            return state == MultiState.DATA;
        }
        return false;
    }

}
