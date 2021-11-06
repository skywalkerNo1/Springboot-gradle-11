package demo.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * json工具类
 */
public class JacksonUtils {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * json字符串转Map<String, Object> 类型
     *
     * @param json 入参
     * @return 返回值
     */
    public static Map<String, Object> jsonToMap(String json) {
        try {
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * json字符串转List<Map<String, Object>>类型
     *
     * @param json 入参
     * @return 返回值
     */
    public static List<Map<String, Object>> jsonToMapList(String json) {
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, Map.class);
            return objectMapper.readValue(json, javaType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * json字符串转 javaBean
     *
     * @param json json入参
     * @param clazz Bean类型
     * @param <T> 返回类型
     * @return 返回值
     */
    public static <T> T jsonToBean(String json, Class<T> clazz) {
        try {
            objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * object 转成javaBean
     *
     * @param object 入参
     * @param clazz 类型
     * @param <T> 泛型
     * @return 返回值
     */
    public static <T> T objToBean(Object object, Class<T> clazz) {
        return objectMapper.convertValue(object, clazz);
    }


    /**
     * Map对象转json字符串
     *
     * @param map 入参参数
     * @return 返回值
     */
    public static String mapToJson(Map<String, Object> map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * List<Map<String, Object>> 转json字符串
     *
     * @param mapList 入参
     * @return 返回值
     */
    public static String mapListToJson(List<Map<String, Object>> mapList) {
        try {
            return objectMapper.writeValueAsString(mapList);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * java对象转成json字符串
     *
     * @param object 对象入参
     * @return 返回值
     */
    public static String objToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void main(String[] args) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        Map<String, Object> json = new HashMap<>();
        json.put("aa","aa");
        json.put("bb","aa");
        json.put("cc","aa");
        mapList.add(json);
        String map = mapListToJson(mapList);
        System.out.println(map);
    }

}
