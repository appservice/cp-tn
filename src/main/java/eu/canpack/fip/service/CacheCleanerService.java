package eu.canpack.fip.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * CP S.A.
 * Created by lukasz.mochel on 11.03.2018.
 */
@Service
public class CacheCleanerService {
    private static final Logger log= LoggerFactory.getLogger(CacheCleanerService.class);

private final CacheManager cacheManager;

    public CacheCleanerService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void clearCache(){
        cacheManager.getCacheNames()
            .forEach(cn -> {cacheManager.getCache(cn).clear();
            log.debug("Cleared cache: {}",cn);});
    }

}
