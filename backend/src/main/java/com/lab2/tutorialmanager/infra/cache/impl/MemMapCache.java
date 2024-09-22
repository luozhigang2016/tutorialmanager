package com.lab2.tutorialmanager.infra.cache.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author : Bo Wang
 * @date : 8/31/2024
 */
@Slf4j
@Component
@ConditionalOnProperty(name = CommonDomainCacheRepo.CFG_CACHE_PROVIDER, havingValue = "mem", matchIfMissing = true)
public class MemMapCache implements CacheIF {

  public MemMapCache() {
    log.info("MemMapCache was created!");
  }

  @Override
  public String putString(String key, String value, int expiredSec) {
    try {
      CacheItem item = new CacheItem(key, value, expiredSec, System.currentTimeMillis());
      String json = null;
      json = mapper.writeValueAsString(item);
      if (log.isDebugEnabled()) {
        log.debug(">>> Write Cache key='{}'\n{}", key, json);
      }
      String old = memCache.put(key, json);
      if (old != null) {
        log.warn("Overwrite cache:\n{}", old);
        CacheItem i = mapper.readerFor(CacheItem.class).readValue(json);
        return i.getValue();
      }
      return null;
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("putString error:" + e.getMessage(), e);
    }
  }

  @Override
  public String getString(String key) {
    try {
      String json = memCache.get(key);
      if (json == null) {
        log.info(">>> Read Cache key='{}', Missing!", key);
        return null;
      }
      CacheItem item = mapper.readerFor(CacheItem.class).readValue(json);
      long itemExpire = (item.getExpiredSec() * 1000L + item.getPutTimestamp());
      if (itemExpire < System.currentTimeMillis()) {
        log.info(">>> Read Cache key='{}', expired at '{}'!\n{}", key, new Date(itemExpire), json);
        memCache.remove(key);
        return null;
      }
      if (log.isDebugEnabled()) {
        log.debug(">>> Read Cache key='{}', value='{}'\n{}", key, item.getValue(), json);
      }
      return item.getValue();
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("getString error:" + e.getMessage(), e);
    }
  }

  @Override
  public boolean delete(String key) {
    String json = memCache.get(key);
    if (json == null) {
      log.info(">>> Read Cache key='{}', Missing!", key);
      return false;
    }
    String oldValue = memCache.remove(key);
    return oldValue != null;
  }

  final private Map<String, String> memCache = new HashMap<>();
  final private ObjectMapper mapper = new ObjectMapper();

  @Getter
  static public class CacheItem {

    private String value;
    private String key;
    private int expiredSec;
    private long putTimestamp;

    private CacheItem() {
    }

    public CacheItem(String key, String value, int expiredSec, long ts) {
      this.key = key;
      this.value = value;
      this.expiredSec = expiredSec;
      this.putTimestamp = ts;
    }
  }
}
