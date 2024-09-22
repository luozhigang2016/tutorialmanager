package com.lab2.tutorialmanager.infra.cache.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lab2.tutorialmanager.domain.common.port.CommonDomainCacheRepoIF;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * 项目： tutorialmanager
 * <p>
 * Common Domain-Cache repo implementation
 *
 * @author : Bo Wang
 * @date : 8/31/2024
 */
@Slf4j
@RequiredArgsConstructor
@Repository
public class CommonDomainCacheRepo implements CommonDomainCacheRepoIF {

  public <T> T get(Object id, Class<T> classOfT) {
    String key = generateKey(id, classOfT);
    String json = cache.getString(key);
    if (json == null) {
      return null;
    }
    if (log.isDebugEnabled()) {
      log.debug("Found json value from cache with id '{}({})':\n{}", id, key, json);
    }
    try {
      ObjectMapper mapper = new ObjectMapper();
      Object obj = mapper.readerFor(classOfT).readValue(json);
      if (!obj.getClass().equals(classOfT)) {
        throw new IllegalArgumentException(
            String.format("Cannot convert to class '%s' with JSON:\n%s", classOfT, json));
      }
      if (log.isDebugEnabled()) {
        log.debug("Found value from cache with id '{}({})':\n{}", id, key, obj);
      }
      return (T) obj;
    } catch (Throwable e) {
      throw new IllegalArgumentException(
          String.format("Cannot convert to class '%s' with JSON:\n%s", classOfT, json), e);
    }
  }

  public <T> void put(Object id, T obj, int expireSec) {
    String key = generateKey(id, obj.getClass());
    ObjectMapper mapper = new ObjectMapper();
    try {
      String json = mapper.writeValueAsString(obj);
      cache.putString(key, json, expireSec);
      if (log.isDebugEnabled()) {
        log.debug("Put value to cache with id '{}({})':\n{}\nJSON:\n{}", id, key, obj, json);
      }
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(
          String.format("Cannot convert to json from object '%s'", obj), e);
    }
  }

  public <T> boolean delete(Object id, Class<T> classOfT) {
    String key = generateKey(id, classOfT);
    boolean ok = cache.delete(key);
    if (log.isDebugEnabled()) {
      log.debug("Delete value from cache with id '{}({})':{}", id, key, ok);
    }
    return ok;
  }

  private String generateKey(Object id, Class clazz) {
    return id.toString() + "@" + clazz.toString();
  }

  final private CacheIF cache;


  public static final String CFG_CACHE_PROVIDER = "lab2.tutorial-manager.infra-cache.provider";

}
