//package io.dfjinxin.common.advice;
//
//import io.dfjinxin.common.annotation.IgnoreResponseAdvice;
//import io.dfjinxin.common.utils.R;
//import org.springframework.core.MethodParameter;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.lang.Nullable;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
//
///**
// * Created by Qinyi.
// */
//@RestControllerAdvice
//public class CommonResponseDataAdvice implements ResponseBodyAdvice<Object> {
//
//    @Override
//    @SuppressWarnings("all")
//    public boolean supports(MethodParameter methodParameter,
//                            Class<? extends HttpMessageConverter<?>> aClass) {
//
//        if (methodParameter.getDeclaringClass().isAnnotationPresent(
//                IgnoreResponseAdvice.class
//        )) {
//            return false;
//        }
//
//        if (methodParameter.getMethod().isAnnotationPresent(
//                IgnoreResponseAdvice.class
//        )) {
//            return false;
//        }
//
//        return true;
//    }
//
//    @Nullable
//    @Override
//    @SuppressWarnings("all")
//    public Object beforeBodyWrite(@Nullable Object o,
//                                  MethodParameter methodParameter,
//                                  MediaType mediaType,
//                                  Class<? extends HttpMessageConverter<?>> aClass,
//                                  ServerHttpRequest serverHttpRequest,
//                                  ServerHttpResponse serverHttpResponse) {
//
//        R response = R.ok();
//        if (null == o) {
//            return response;
//        } else if (o instanceof R) {
//            response = (R) o;
//        } else {
//            response.put("data", o);
//        }
//
//        return response;
//    }
//}