package com.zombie.utils.json;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Gson相关工具
 */

public class GsonUtils {
    private static JsonParser parser = new JsonParser();
    // private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    //重写deserialize方法,避免int,long类型被转成double
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(
                    new TypeToken<TreeMap<String, Object>>() {
                    }.getType(),
                    new JsonDeserializer<TreeMap<String, Object>>() {
                        @Override
                        public TreeMap<String, Object> deserialize(
                                JsonElement json, Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {

                            TreeMap<String, Object> treeMap = new TreeMap<>();
                            JsonObject jsonObject = json.getAsJsonObject();
                            Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
                            for (Map.Entry<String, JsonElement> entry : entrySet) {
                                treeMap.put(entry.getKey(), entry.getValue());
                            }
                            return treeMap;
                        }
                    }).setPrettyPrinting().create();


    /**
     * 将json字符串格式化
     *
     * @param jsonString
     *
     * @return
     */
    public static String formatJsonString(String jsonString) {
        JsonObject json = getJsonObject(jsonString);
        return gson.toJson(json);
    }

    private static JsonObject getJsonObject(String jsonString) {
        return parser.parse(jsonString).getAsJsonObject();
    }

    /**
     * 将对象转换成json
     *
     * @param object
     *
     * @return
     */
    public static String parseJson(Object object) {
        if (object.getClass().equals(String.class)) {
            return formatJsonString(object.toString());
        } else {
            return gson.toJson(object);
        }
    }

    /**
     * 将object对象转换成map
     *
     * @param object
     *
     * @return
     */
    public static Map<String, Object> jsonObjectToMap(Object object) {
        TreeMap<String, Object> resultMap = gson.fromJson(parseJson(object), new TypeToken<TreeMap<String, Object>>() {
        }.getType());
        return resultMap;
    }


}
