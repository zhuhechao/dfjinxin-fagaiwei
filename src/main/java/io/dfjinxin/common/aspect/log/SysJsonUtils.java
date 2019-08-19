package io.dfjinxin.common.aspect.log;

import com.google.gson.Gson;

public class SysJsonUtils {
    private static Gson gson = new Gson();

    public static String objectsToJson(Object[] os){
        try {
            return gson.toJson(os);
        }catch (Exception e){

        }
        return "";
    }

    public static String objectToJson(Object o){
        try {
            return gson.toJson(o);
        }catch (Exception e){

        }
        return "";
    }

//    public static List<WashCmpuParasDto> jsonToWashCmpuParas(String jsonArray){
//        try{
//            Type type = new TypeToken<List<WashCmpuParasDto>>() {}.getType();
//            return gson.fromJson(jsonArray, type);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return null;
//    }

}
