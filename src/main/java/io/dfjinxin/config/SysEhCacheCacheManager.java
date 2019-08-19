//package io.dfjinxin.config;
//
//import net.sf.ehcache.Ehcache;
//import org.springframework.cache.Cache;
//import org.springframework.cache.ehcache.EhCacheCache;
//import org.springframework.cache.ehcache.EhCacheCacheManager;
//import org.springframework.stereotype.Component;
//
//@Component
//public class SysEhCacheCacheManager extends EhCacheCacheManager {
//
//    @Override
//    protected Cache getMissingCache(String name) {
//        Cache cache = super.getMissingCache(name);
//        if (cache == null) {
//            Ehcache ehcache = super.getCacheManager().addCacheIfAbsent(name);
//            cache = new EhCacheCache(ehcache);
//        }
//        return cache;
//    }
//}
