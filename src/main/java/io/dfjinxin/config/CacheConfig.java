package io.dfjinxin.config;



import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.CacheManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created by GaoPh on 2019/11/25.
 */
@Configuration
public class CacheConfig {
   @Bean(name="cacheManager")
   public CacheManager cacheManager(){
     CaffeineCacheManager cacheManager= new CaffeineCacheManager();
     Caffeine caffeine=  Caffeine.newBuilder()
               .initialCapacity(1000)
               .maximumSize(80000)
               .expireAfterWrite(90, TimeUnit.SECONDS);
     cacheManager.setCaffeine(caffeine);
//     cacheManager.setCacheLoader(cacheLoader);
     cacheManager.setCacheNames(getNames());
     cacheManager.setAllowNullValues(false);
     return  cacheManager;
   }

   private static List<String> getNames(){
       List<String> names = new ArrayList<>();
       names.add("cacheToken");
       return names;
   }

}

