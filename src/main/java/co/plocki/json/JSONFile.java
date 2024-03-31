package co.plocki.json;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JSONFile {
    private File file;
    private final String mapperVersion = "1.0";
    private JSONObject object;
    private boolean isNew;

    public JSONFile(String filePath, JSONValue... objects) {
        String var10003 = String.valueOf(Paths.get("").toAbsolutePath());
        this.file = new File(var10003 + File.separator + filePath);
        if (!this.file.exists()) {
            this.createNewFile(objects);
        } else {
            this.isNew = false;
            this.loadFile();
        }

    }

    private void createNewFile(JSONValue... objects) {
        try {
            if (!this.file.getParentFile().exists()) {
                this.file.getParentFile().mkdirs();
            }

            this.file.createNewFile();
            this.object = new JSONObject();
            this.object.put("mapperVersion", "1.0");
            JSONValue[] var7 = objects;
            int var3 = objects.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                JSONValue jsonValue = var7[var4];
                this.object.put(jsonValue.objectName(), jsonValue.object());
            }

            this.saveToFile();
            this.isNew = true;
        } catch (IOException var6) {
            IOException e = var6;
            e.printStackTrace();
        }

    }

    private void loadFile() {
        try {
            this.object = new JSONObject(new String(Files.readAllBytes(this.file.toPath())));
        } catch (IOException var2) {
            IOException e = var2;
            e.printStackTrace();
        }

    }

    private void saveToFile() throws IOException {
        PrintWriter writer = new PrintWriter(this.file);

        try {
            writer.println((new BeautifulJson()).beautiful(this.object.toString()));
        } catch (Throwable var5) {
            try {
                writer.close();
            } catch (Throwable var4) {
                var5.addSuppressed(var4);
            }

            throw var5;
        }

        writer.close();
    }

    public boolean isNew() {
        return this.isNew;
    }

    public JSONObject getFileObject() {
        return this.object;
    }

    public String getMapperVersion() {
        return "1.0";
    }

    public File getFile() {
        return this.file;
    }

    public void save() throws IOException {
        this.file.delete();
        this.saveToFile();
    }

    public void setNull(String key) {
        this.object.put(key, JSONObject.NULL);
    }

    public void save(String filePath) throws IOException {
        this.file.delete();
        this.file = new File(filePath);
        this.saveToFile();
    }

    public void save(File customFile) throws IOException {
        this.file.delete();
        this.file = customFile;
        JSONObject obj = new JSONObject(this.object.toString());
        this.createNewFile();
        this.object = obj;
        this.saveToFile();
    }

    public void remove(String key) {
        this.object.remove(key);
    }

    public void put(String key, Object value) {
        this.object.put(key, value);
    }

    public void putDefaultObject(String key, Object value) {
        if (!this.object.has(key)) {
            this.object.put(key, value);
        }

    }

    public JSONObject get(String key) {
        return this.object.getJSONObject(key);
    }

    public String getString(String key) {
        return this.object.getString(key);
    }

    public int getInt(String key) {
        return this.object.getInt(key);
    }

    public boolean getBoolean(String key) {
        return this.object.getBoolean(key);
    }

    public double getDouble(String key) {
        return this.object.getDouble(key);
    }

    public long getLong(String key) {
        return this.object.getLong(key);
    }

    public float getFloat(String key) {
        return (float)this.object.getDouble(key);
    }

    public short getShort(String key) {
        return (short)this.object.getInt(key);
    }

    public byte getByte(String key) {
        return (byte)this.object.getInt(key);
    }

    public char getChar(String key) {
        return this.object.getString(key).charAt(0);
    }

    public boolean has(String key) {
        return this.object.has(key);
    }

    public boolean isNull(String key) {
        return this.object.isNull(key);
    }

    public boolean isString(String key) {
        return this.object.get(key) instanceof String;
    }

    public boolean isInt(String key) {
        return this.object.get(key) instanceof Integer;
    }

    public boolean isBoolean(String key) {
        return this.object.get(key) instanceof Boolean;
    }

    public boolean isDouble(String key) {
        return this.object.get(key) instanceof Double;
    }

    public boolean isLong(String key) {
        return this.object.get(key) instanceof Long;
    }

    public boolean isFloat(String key) {
        return this.object.get(key) instanceof Float;
    }

    public boolean isShort(String key) {
        return this.object.get(key) instanceof Short;
    }

    public boolean isByte(String key) {
        return this.object.get(key) instanceof Byte;
    }

    public boolean isChar(String key) {
        return this.object.get(key) instanceof Character;
    }

    public boolean isObject(String key) {
        return this.object.get(key) instanceof JSONObject;
    }

    public boolean isArray(String key) {
        return this.object.get(key) instanceof JSONArray;
    }

    public boolean isNumber(String key) {
        return this.object.get(key) instanceof Number;
    }

    public boolean isPrimitive(String key) {
        return this.object.get(key) instanceof String || this.object.get(key) instanceof Integer || this.object.get(key) instanceof Boolean || this.object.get(key) instanceof Double || this.object.get(key) instanceof Long || this.object.get(key) instanceof Float || this.object.get(key) instanceof Short || this.object.get(key) instanceof Byte || this.object.get(key) instanceof Character;
    }

    public boolean isPrimitiveArray(String key) {
        return this.object.get(key) instanceof String[] || this.object.get(key) instanceof Integer[] || this.object.get(key) instanceof Boolean[] || this.object.get(key) instanceof Double[] || this.object.get(key) instanceof Long[] || this.object.get(key) instanceof Float[] || this.object.get(key) instanceof Short[] || this.object.get(key) instanceof Byte[] || this.object.get(key) instanceof Character[];
    }

    public boolean isStringArray(String key) {
        return this.object.get(key) instanceof String[];
    }

    public boolean isIntArray(String key) {
        return this.object.get(key) instanceof Integer[];
    }

    public boolean isBooleanArray(String key) {
        return this.object.get(key) instanceof Boolean[];
    }

    public boolean isDoubleArray(String key) {
        return this.object.get(key) instanceof Double[];
    }

    public boolean isLongArray(String key) {
        return this.object.get(key) instanceof Long[];
    }

    public boolean isFloatArray(String key) {
        return this.object.get(key) instanceof Float[];
    }

    public boolean isShortArray(String key) {
        return this.object.get(key) instanceof Short[];
    }

    public boolean isByteArray(String key) {
        return this.object.get(key) instanceof Byte[];
    }

    public boolean isCharArray(String key) {
        return this.object.get(key) instanceof Character[];
    }

    public boolean isObjectArray(String key) {
        return this.object.get(key) instanceof JSONObject[];
    }

    public boolean isNumberArray(String key) {
        return this.object.get(key) instanceof Number[];
    }

    public String[] getStringArray(String key) {
        JSONArray array = this.object.getJSONArray(key);
        String[] strings = new String[array.length()];

        for(int i = 0; i < array.length(); ++i) {
            strings[i] = array.getString(i);
        }

        return strings;
    }

    public int[] getIntArray(String key) {
        JSONArray array = this.object.getJSONArray(key);
        int[] ints = new int[array.length()];

        for(int i = 0; i < array.length(); ++i) {
            ints[i] = array.getInt(i);
        }

        return ints;
    }

    public boolean[] getBooleanArray(String key) {
        JSONArray array = this.object.getJSONArray(key);
        boolean[] booleans = new boolean[array.length()];

        for(int i = 0; i < array.length(); ++i) {
            booleans[i] = array.getBoolean(i);
        }

        return booleans;
    }

    public double[] getDoubleArray(String key) {
        JSONArray array = this.object.getJSONArray(key);
        double[] doubles = new double[array.length()];

        for(int i = 0; i < array.length(); ++i) {
            doubles[i] = array.getDouble(i);
        }

        return doubles;
    }

    public long[] getLongArray(String key) {
        JSONArray array = this.object.getJSONArray(key);
        long[] longs = new long[array.length()];

        for(int i = 0; i < array.length(); ++i) {
            longs[i] = array.getLong(i);
        }

        return longs;
    }

    public float[] getFloatArray(String key) {
        JSONArray array = this.object.getJSONArray(key);
        float[] floats = new float[array.length()];

        for(int i = 0; i < array.length(); ++i) {
            floats[i] = (float)array.getDouble(i);
        }

        return floats;
    }

    public short[] getShortArray(String key) {
        JSONArray array = this.object.getJSONArray(key);
        short[] shorts = new short[array.length()];

        for(int i = 0; i < array.length(); ++i) {
            shorts[i] = (short)array.getInt(i);
        }

        return shorts;
    }

    public byte[] getByteArray(String key) {
        JSONArray array = this.object.getJSONArray(key);
        byte[] bytes = new byte[array.length()];

        for(int i = 0; i < array.length(); ++i) {
            bytes[i] = (byte)array.getInt(i);
        }

        return bytes;
    }

    public char[] getCharArray(String key) {
        JSONArray array = this.object.getJSONArray(key);
        char[] chars = new char[array.length()];

        for(int i = 0; i < array.length(); ++i) {
            chars[i] = (char)array.getInt(i);
        }

        return chars;
    }

    public JSONObject[] getObjectArray(String key) {
        JSONArray array = this.object.getJSONArray(key);
        JSONObject[] objects = new JSONObject[array.length()];

        for(int i = 0; i < array.length(); ++i) {
            objects[i] = new JSONObject(array.getJSONObject(i));
        }

        return objects;
    }

    public Number[] getNumberArray(String key) {
        JSONArray array = this.object.getJSONArray(key);
        Number[] numbers = new Number[array.length()];

        for(int i = 0; i < array.length(); ++i) {
            numbers[i] = (Number)array.get(i);
        }

        return numbers;
    }

    public JSONArray getArray(String key) {
        return this.object.getJSONArray(key);
    }
}
