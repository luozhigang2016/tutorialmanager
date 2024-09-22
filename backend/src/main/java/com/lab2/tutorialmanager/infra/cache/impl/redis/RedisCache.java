package com.lab2.tutorialmanager.infra.cache.impl.redis;

import com.lab2.tutorialmanager.infra.cache.impl.CacheIF;
import com.lab2.tutorialmanager.infra.cache.impl.CommonDomainCacheRepo;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 项目： tutorialmanager
 * <p>
 * TODO: 说明 ...
 *
 * @author : Bo Wang
 * @date : 8/31/2024
 */
@Slf4j
@Component
@ConditionalOnProperty(name = CommonDomainCacheRepo.CFG_CACHE_PROVIDER, havingValue = "redis")
public class RedisCache implements CacheIF {

  private final RedisTemplate<String, String> redisTemplate;

  public RedisCache(RedisTemplate<String, String> redisTemplate) {
    this.redisTemplate = redisTemplate;
    log.info("RedisCache was created!");
  }

  @Override
  public String putString(String key, String value, int expiredSec) {
    redisTemplate.opsForValue().set(key, value, expiredSec, TimeUnit.SECONDS);
    return value;
  }

  @Override
  public String getString(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  @Override
  public boolean delete(String key) {
    String value = redisTemplate.opsForValue().getAndDelete(key);
    return value != null;
  }
}
