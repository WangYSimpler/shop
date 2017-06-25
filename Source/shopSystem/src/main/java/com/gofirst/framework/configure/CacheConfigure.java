package com.gofirst.framework.configure;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@CacheConfig(cacheManager="cacheManager")
public class CacheConfigure {

}
