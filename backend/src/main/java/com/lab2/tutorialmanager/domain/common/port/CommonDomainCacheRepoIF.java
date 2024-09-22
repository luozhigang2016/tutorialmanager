package com.lab2.tutorialmanager.domain.common.port;

/**
 * 项目： cwssh
 * <p>
 * Common Domain-Cache Repo Interface
 * Implemented in infra-cache
 *
 *
 * @author : Bo Wang
 * @date : 8/31/2024
 */
public interface CommonDomainCacheRepoIF {
  <T> T get(Object key, Class<T> classOfT);
  <T> void put(Object key, T obj, int expireSec);
  <T> boolean delete(Object key, Class<T> classOfT);
}
