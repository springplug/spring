package com.springplug.common.util.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * \* User: zhangpanxiang
 * \* Date: 2020/5/15
 * \* Time: 10:18
 * \
 */
public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static{
        JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        timeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        objectMapper.registerModule(timeModule);
    }

    //时间序列化时变为时间戳
    static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
        @Override
        public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeNumber(localDateTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli());
        }
    }

    //时间戳反序列化时间
    static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            Long timestamp = jsonParser.getLongValue();
            return LocalDateTime.ofEpochSecond(timestamp / 1000, 0, ZoneOffset.ofHours(8));
        }
    }

    /**
     * 此类不允许实例化
     */
    private JsonUtil() {
    }

    /**
     * json字符串转list
     *
     * @param json 需要转换的json串
     * @return 转换后的list<Object>
     */
    public static List<Object> toList(String json) {
        return toList(json, Object.class);

    }

    /**
     * json字符串转list<Map>
     *
     * @param json 需要转换的json串
     * @return 转换后的list<Object>
     */
    public static List<Map<String,Object>> toListMap(String json) {
        return toListMap(json,String.class,Object.class);
    }

    /**
     * json字符串转list<Map>
     *
     * @param json 需要转换的json串
     * @return 转换后的list<Object>
     */
    public static <T> List<Map<String,T>> toListMap(String json, Class<T> t) {
        return toListMap(json,String.class,t);

    }

    /**
     * json字符串转list<Map>
     *
     * @param json 需要转换的json串
     * @return 转换后的list<Object>
     */
    public static <K, V> List<Map<K,V>> toListMap(String json, Class<K> k, Class<V> v) {
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class,objectMapper.getTypeFactory().constructMapLikeType(HashMap.class,k,v)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * json字符串转list
     *
     * @param <T>  泛型
     * @param json 需要转换的json串
     * @param t    需要转换成的list泛型
     * @return 转换后的list<T>
     */
    public static <T> List<T> toList(String json, Class<T> t) {
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, t));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * list转json字符串
     *
     * @param list 需要转换的list
     * @return 转换后的json字符串
     */
    public static String toJson(Collection<?> list) {
        try {
            return objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json转换map
     *
     * @param json 需要转换的json字符串
     * @return 转换后的Map<String,Object>
     */
    public static Map<String, Object> toMap(String json) {
        return toMap(json, String.class, Object.class);
    }

    /**
     * json转换map
     *
     * @param <T>  泛型
     * @param json 需要转换的json字符串
     * @param arg  需要转换成的value类型
     * @return 转换后的Map<String,T>
     */
    public static <T> Map<String, T> toMap(String json, Class<T> arg) {
        return toMap(json, String.class, arg);
    }

    /**
     * json转换map
     *
     * @param <K>泛型
     * @param <V>泛型
     * @param json  需要转换的json字符串
     * @param k     需要转换成的key类型
     * @param v     需要转换成的value类型
     * @return 转换后的Map<K,V>
     */
    public static <K, V> Map<K, V> toMap(String json, Class<K> k, Class<V> v) {
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructMapType(HashMap.class,k,v));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * map转json字符串
     *
     * @param <K> 泛型
     * @param <V> 泛型
     * @param map 需要转换的map
     * @return 转换后的json字符串
     */
    public static <K, V> String toJson(Map<K, V> map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * map转json字符串
     *
     * @return 转换后的json字符串
     */
    public static <T> String toJson(T t) {
        try {
            return objectMapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json字符串转bean
     *
     * @param <T>  泛型
     * @param json 需要转换的json字符串
     * @param t    需要转换成的类型
     * @return 转换后的bean
     */
    public static <T> T toBean(String json, Class<T> t) {
        try {
            return objectMapper.readValue(json, t);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * map转bean
     *
     * @param <T>泛型
     * @return 转换后的bean
     */
    public static <T> Map<String,Object> toMap(T t) {
        try {
            return objectMapper.readValue(toJson(t),Map.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * map转bean
     *
     * @param <T>泛型
     * @param map   需要转换的map
     * @param t     需要转换成的类型
     * @return 转换后的bean
     */
    public static <T> T toBean(Map<?, ?> map, Class<T> t) {
        try {
            return objectMapper.readValue(toJson(map), t);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}