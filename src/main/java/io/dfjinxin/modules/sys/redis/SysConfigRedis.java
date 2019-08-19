///**
// * 2019 东方金信
// *
// *
// *
// *
// */
//
//package io.dfjinxin.modules.sys.redis;
//
//
//import io.dfjinxin.common.utils.RedisKeys;
//import io.dfjinxin.common.utils.RedisUtils;
//import io.dfjinxin.modules.sys.entity.SysConfigEntity;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * 系统配置Redis
// *
// * @author Mark sunlightcs@gmail.com
// */
//@Component
//public class SysConfigRedis {
//    @Autowired
//    private RedisUtils redisUtils;
//
//    public void saveOrUpdate(SysConfigEntity config) {
//        if(config == null){
//            return ;
//        }
//        String key = RedisKeys.getSysConfigKey(config.getParamKey());
//        redisUtils.set(key, config);
//    }
//
//    public void delete(String configKey) {
//        String key = RedisKeys.getSysConfigKey(configKey);
//        redisUtils.delete(key);
//    }
//
//    public SysConfigEntity get(String configKey){
//        String key = RedisKeys.getSysConfigKey(configKey);
//        return redisUtils.get(key, SysConfigEntity.class);
//    }
//}
