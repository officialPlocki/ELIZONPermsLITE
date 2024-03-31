package co.plocki.json;

import org.json.JSONObject;

import java.io.IOException;

public class ButifyJSON {
    public ButifyJSON() {
    }

    public static void main(String[] args) throws IOException {
        String json = (new JSONFile("notbuty.json", new JSONValue[0])).get("json").toString();
        JSONFile file = new JSONFile("butiful.json", new JSONValue[0]);
        file.put("json", new JSONObject(json));
        file.save();
    }
}
