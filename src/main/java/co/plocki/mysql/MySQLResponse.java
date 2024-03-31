package co.plocki.mysql;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MySQLResponse {

    private final List<HashMap<String, String>> values;
    private final HashMap<String, String> value;
    private final boolean noll;

    @SafeVarargs
    public MySQLResponse(HashMap<String, String>... values) {
        this.values = Arrays.asList(values);
        this.value = values[0];
        noll = false;
    }

    public MySQLResponse(List<HashMap<String, String>> values) {
        this.values = values;
        this.value = values.get(0);
        noll = false;
    }

    public MySQLResponse() {
        this.values = null;
        this.value = null;
        noll = true;
    }

    public String getString(String column) {
        return value.get(column);
    }

    public double getDouble(String column) {
        return Double.parseDouble(value.get(column));
    }

    public int getInt(String column) {
        return Integer.parseInt(value.get(column));
    }

    public long getLong(String column) {
        return Long.parseLong(value.get(column));
    }

    public float getFloat(String column) {
        return Float.parseFloat(value.get(column));
    }

    public boolean getBoolean(String column) {
        return Boolean.parseBoolean(value.get(column));
    }

    public boolean isEmpty() {
        return value == null || values == null || values.isEmpty() && value.isEmpty() || noll;
    }

    public Object get(String column) {
        return value.get(column);
    }

    public HashMap<String, String> rawFirst() {
        return value;
    }

    public List<HashMap<String, String>> rawAll() {
        return values;
    }

    @Override
    public String toString() {
        return values.toString();
    }
}