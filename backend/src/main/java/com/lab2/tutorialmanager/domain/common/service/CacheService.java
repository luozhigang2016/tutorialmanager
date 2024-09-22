package com.lab2.tutorialmanager.domain.common.service;

import com.lab2.tutorialmanager.domain.common.port.CommonDomainCacheRepoIF;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 项目： tutorialmanager
 * <p>
 * Common Domain-Cache Service
 *
 * @author : Bo Wang
 * @date : 8/31/2024
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CacheService {

  private final CommonDomainCacheRepoIF repo;

  public <T> T get(Object id, Class<T> classOfT) {
    return repo.get(id, classOfT);
  }

  public <T> void put(Object id, T obj, int expireSec) {
    repo.put(id, obj, expireSec);
  }

  public <T> boolean delete(Object id, Class<T> classOfT) {
    return repo.delete(id, classOfT);
  }
}
