package com.mobile.healthmate.app.lib.json;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;

/**
 * JSON序列化支持
 *
 * @author zdxing
 */

/**
 * JSON序列化支持
 *
 * @author zdxing
 */
public class JsonHelper {

    /**
     * 将java对象转换为JSONObject
     *
     * @return 如果转换失败，将返回null
     */
    @Nullable
    public static JSONObject toJSONObject(@NonNull Object object) {
        JSONObject jsonObject = new JSONObject();
        Class<?> currentClass = object.getClass();
        while (currentClass != Object.class) {
            Field[] fields = currentClass.getDeclaredFields();
            for (Field field : fields) {
                if (!field.isAccessible())
                    field.setAccessible(true);

                if (field.isAnnotationPresent(JsonTransparent.class)) {
                    // 忽略掉JsonTransparent注解的部分
                    continue;
                }

                int modifiers = field.getModifiers();
                if (Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers)) {
                    // 忽略掉static 和 final 修饰的变量
                    continue;
                }

                String fieldName;
                if (field.isAnnotationPresent(JsonField.class)) {
                    fieldName = field.getAnnotation(JsonField.class).value();
                } else {
                    fieldName = field.getName();
                }

                try {
                    Object value = getJsonObject(field.get(object));
                    if (value == null) {
                        continue;
                    } else {
                        jsonObject.put(fieldName, value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        return jsonObject;
    }

    /**
     * 将Array数据转换为JSONArray
     *
     * @param datas 要转换的数据
     */
    @NonNull
    public static JSONArray toJSONArray(@NonNull List<?> datas) {
        JSONArray jsonArray = new JSONArray();
        for (Object object : datas) {
            jsonArray.put(getJsonObject(object));
        }
        return jsonArray;
    }

    ;

    /**
     * 将java对象转换为json支持的类型
     *
     * @param target java类型
     * @return json类型
     */
    private static Object getJsonObject(Object target) {
        Object result;
        try {
            if (target == null) {
                return null;
            }
            Class<?> fieldType = target.getClass();
            if (fieldType.isArray()) {
                int length = Array.getLength(target);
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < length; i++) {
                    Object arrayValue = Array.get(target, i);
                    Object value = getJsonObject(arrayValue);
                    if (value != null)
                        jsonArray.put(value);
                }
                result = jsonArray;
            } else if (Collection.class.isAssignableFrom(fieldType)) {
                // 集合类型转换
                Collection<?> list = (Collection<?>) target;
                JSONArray jsonArray = new JSONArray();
                for (Object object : list) {
                    Object value = getJsonObject(object);
                    if (value != null)
                        jsonArray.put(value);
                }
                result = jsonArray;
            } else if (Map.class.isAssignableFrom(fieldType)) {
                throw new RuntimeException("不支持 Map 类型的数据转换");
            } else if (fieldType == Integer.class || fieldType == Character.class || fieldType == Double.class
                    || fieldType == Float.class || fieldType == Byte.class || fieldType == Long.class
                    || fieldType == Short.class || fieldType == String.class || fieldType == Boolean.class) {
                // 基本数据类型转换
                result = target;
            } else {
                result = toJSONObject(target);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    /**
     * 将JSONArray转换为
     *
     * @param jsonArray  JSON数组
     * @param entityType 对应的JAVA类型
     * @param <V>
     * @return 转换后的java对象
     */
    @NonNull
    public static <V> List<V> toList(@NonNull JSONArray jsonArray, @NonNull Class<V> entityType) {
        List<V> list = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                V v;

                String json = jsonArray.getString(i);
                if (json.startsWith("{")) {
                    JSONObject jsonObject = new JSONObject(json);
                    v = toObject(jsonObject, entityType);
                } else {
                    v = (V) getJavaObject(json, entityType, null);
                }

                if (v != null) {
                    list.add(v);
                } else {
                    Log.d("json", "json序列化失败：" + json);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    /**
     * 将json对象的值，复制到object对象中
     */
    public static void initObject(@NonNull JSONObject jsonObject, @NonNull Object object) {
        if (jsonObject == null) {
            return;
        }
        try {
            Class<?> currentClass = object.getClass();
            while (currentClass != Object.class) {
                Field[] fields = currentClass.getDeclaredFields();
                for (Field field : fields) {
                    try {
                        if (field.isAnnotationPresent(JsonTransparent.class)) {
                            // 忽略掉JsonTransparent注解的部分
                            continue;
                        }

                        int modifiers = field.getModifiers();
                        if (Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers)) {
                            // 忽略掉static 和 final 修饰的变量
                            continue;
                        }

                        if (!field.isAccessible())
                            field.setAccessible(true);

                        String fieldName;
                        if (field.isAnnotationPresent(JsonField.class)) {
                            fieldName = field.getAnnotation(JsonField.class).value();
                        } else {
                            fieldName = field.getName();
                        }

                        String stringObject = jsonObject.optString(fieldName, null);
                        if (stringObject == null) {
                            // 跳过json中不存在的字段
                            continue;
                        }

                        try {
                            Object value = getJavaObject(stringObject, field.getType(), field.getGenericType());
                            field.set(object, value);
                        } catch (Exception e) {
                            String formatMsg = "无法进行json解析 java->(对象:%s, 属性:%s, 类型:%s) json->(key = %s, value = \"%s\")";
                            String msg = String.format(formatMsg, currentClass.getSimpleName(), field.getName(), field.getType().getSimpleName(), fieldName, stringObject);
                            Log.e("JsonHelper", msg);
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                currentClass = currentClass.getSuperclass();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 将jsonObject转换为java对象
     *
     * @return 转换失败，返回null
     */
    @Nullable
    public static <V> V toObject(@NonNull JSONObject jsonObject, @NonNull Class<V> entityType) {
        V v;
        try {
            v = entityType.newInstance();
            initObject(jsonObject, v);
        } catch (Exception e1) {
            e1.printStackTrace();
            v = null;
        }
        return v;
    }

    @SuppressWarnings("unchecked")
    public static Object getJavaObject(String jsonString, Class<?> targetType, Type parameterizedType) throws JSONException,
            InstantiationException, IllegalAccessException {
        Object result = null;
        if (targetType == Integer.class || targetType == int.class) {
            result = Integer.parseInt(jsonString);
        } else if (targetType == Character.class || targetType == char.class) {
            result = Character.valueOf((char) Integer.parseInt(jsonString));
        } else if (targetType == Double.class || targetType == double.class) {
            result = Double.parseDouble(jsonString);
        } else if (targetType == Float.class || targetType == float.class) {
            result = Float.parseFloat(jsonString);
        } else if (targetType == Byte.class || targetType == byte.class) {
            result = Byte.parseByte(jsonString);
        } else if (targetType == Long.class || targetType == long.class) {
            result = Long.parseLong(jsonString);
        } else if (targetType == Short.class || targetType == short.class) {
            result = Short.parseShort(jsonString);
        } else if (targetType == Boolean.class || targetType == boolean.class) {
            result = Boolean.parseBoolean(jsonString);
        } else if (targetType == String.class) {
            result = jsonString;
        } else if (targetType.isArray()) {
            // 数组类型
            JSONArray jsonArray = new JSONArray(jsonString);
            int length = jsonArray.length();
            result = Array.newInstance(targetType.getComponentType(), length);
            for (int i = 0; i < length; i++) {
                String json = jsonArray.getString(i);
                Array.set(result, i, getJavaObject(json, targetType.getComponentType(), null));
            }
        } else if (Collection.class.isAssignableFrom(targetType)) {
            JSONArray jsonArray = new JSONArray(jsonString);
            int length = jsonArray.length();
            Collection<Object> list;
            if (targetType.equals(List.class)) {
                list = new ArrayList<Object>();
            } else if (targetType.equals(Set.class)) {
                list = new HashSet<Object>();
            } else if (targetType.equals(Queue.class) || targetType.equals(Deque.class)) {
                list = new LinkedList<Object>();
            } else if (targetType.equals(ArrayList.class) || targetType.equals(LinkedList.class)
                    || targetType.equals(HashSet.class) || targetType.equals(TreeSet.class)
                    || targetType.equals(Vector.class) || targetType.equals(Stack.class)) {
                list = (Collection<Object>) targetType.newInstance();
            } else {
                throw new RuntimeException("不支持的java集合类型！");
            }
            for (int i = 0; i < length; i++) {
                String json = jsonArray.getString(i);
                list.add(getJavaObject(json, getCollectionClass((ParameterizedType) parameterizedType), null));
            }
            result = list;
        } else if (Map.class.isAssignableFrom(targetType)) {
            throw new RuntimeException("不支持 Map 类型的数据转换");
        } else {
            result = toObject(new JSONObject(jsonString), targetType);
        }
        return result;
    }

    /** 获取泛型类型 */
    private static Class<?> getCollectionClass(ParameterizedType parameterizedType) {
        Class<?> entityClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
        return entityClass;
    }
}