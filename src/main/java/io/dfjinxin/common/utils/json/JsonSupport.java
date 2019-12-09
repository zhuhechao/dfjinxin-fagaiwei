package io.dfjinxin.common.utils.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.dfjinxin.common.utils.jsonp.JsonResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pc on 2017/7/5.
 */
public class JsonSupport {
    public static <T> String objectToJson(T object)
    {
        ObjectMapper mapper = new ObjectMapper();
        String jsonValue = null;
        try {
            jsonValue = mapper.writeValueAsString(object);
            jsonValue = formatString(jsonValue);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonValue;
    }

    public static <T> String objectToJsonWithoutFormatter(T object)
    {
        ObjectMapper mapper = new ObjectMapper();
        String jsonValue = null;
        try {
            jsonValue = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonValue;
    }

    public static HashMap jsonToMap(String jsonVal){
        ObjectMapper mapper = new ObjectMapper();
        try {
            HashMap mapVal = mapper.readValue(jsonVal, HashMap.class);
            return mapVal;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object jsonToObect(String jsonVal, Class clazz){

        List<PageField> tList = new ArrayList<>();
        tList.getClass();

        ObjectMapper mapper = new ObjectMapper();
        try {
            TypeFactory t = TypeFactory.defaultInstance();
            Object mapVal = mapper.readValue(jsonVal,clazz);

            return mapVal;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object jsonToList4Generic(String jsonVal, Class rootClazz,Class genericClazz){
        ObjectMapper mapper = new ObjectMapper();
        Object mapVal = null;
        try {
            TypeFactory t = TypeFactory.defaultInstance();
            mapVal = mapper.readValue(jsonVal, t.constructCollectionType(rootClazz, genericClazz));
            return mapVal;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapVal;
    }

    public static Object jsonToMap4Generic(String jsonStr, Class<?> rootClassType, Class<?>[] jsonObjType) {
        ObjectMapper mapper = new ObjectMapper();
        Object mapVal = null;
        try {
            TypeFactory t = TypeFactory.defaultInstance();
            mapVal = mapper.readValue(jsonStr, t.constructMapLikeType(rootClassType, jsonObjType[0],jsonObjType[1]));
            return mapVal;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapVal;
    }

    public static JsonResult makeJsonpResult(JsonResult.RESULT result, String resultMsg, String failReason, Object resultData){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setResult(result);
        jsonResult.setResult_msg(resultMsg);
        jsonResult.setFaild_reason(failReason);
        jsonResult.setResultData(resultData);
        return jsonResult;
    }

    public static String makeJsonResultStr(JsonResult.RESULT result, String resultMsg, String failReason, Object resultData){
        JsonResult jsonResult = makeJsonpResult(result, resultMsg, failReason, resultData);
        return jsonResult.toString();
    }

    public static String formatString(String jsonStr){
        jsonStr = jsonStr.replace("\\\"","\"");
        jsonStr = jsonStr.replace("\"[","[");
        jsonStr = jsonStr.replace("]\"","]");
        jsonStr = jsonStr.replace("\"{","{");
        jsonStr = jsonStr.replace("}\"","}");
        return jsonStr;
    }

//    public static Object jsonToObect(String jsonVal, Type type){
//
//        ObjectMapper mapper = new ObjectMapper();
//
//        mapper.getTypeFactory().constructCollectionLikeType(ArrayList.class,type);
//
//        try {
//            Object mapVal = mapper.readValue(jsonVal, clazz);
//            return mapVal;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

}
