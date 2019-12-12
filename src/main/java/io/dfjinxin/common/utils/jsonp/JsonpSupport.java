//package io.dfjinxin.common.utils.jsonp;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.common.base.Preconditions;
//import com.google.common.base.Strings;
//import io.dfjinxin.common.utils.json.JsonSupport;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
// * Created by pc on 2017/7/3.
// */
//public class JsonpSupport {
//
//    public static String objectToJsonp(String jsonp_callback,Object object){
//        Preconditions.checkNotNull(object);
//        jsonp_callback = Strings.isNullOrEmpty(jsonp_callback)?"callback":jsonp_callback;
////        Gson gson = new Gson();
//        ObjectMapper mapper = new ObjectMapper();
//        String jsonValue = null;
//        try {
//            if(object instanceof String){
//                jsonValue = (String) object;
//            }else
//                jsonValue = mapper.writeValueAsString(object);
//        } catch (JsonProcessingException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        return new StringBuilder().append(jsonp_callback).append("(").append(jsonValue).append(")").toString();
//    }
//
//    public static String jsonpCallbackFunctionName(HttpServletRequest request){
//        String web_call_back = request.getParameter("web_call_back");
//        if(Strings.isNullOrEmpty(web_call_back)){
//            web_call_back = (String) request.getAttribute("web_call_back");
//        }
//        return web_call_back;
//    }
//
//    public static String makeJsonpResponse(JsonResult.RESULT result, String resultMsg, String failReason, Object resultData, HttpServletRequest request){
//        return objectToJsonp(jsonpCallbackFunctionName(request), JsonSupport.makeJsonpResult(result, resultMsg, failReason, resultData));
//    }
//
//}
