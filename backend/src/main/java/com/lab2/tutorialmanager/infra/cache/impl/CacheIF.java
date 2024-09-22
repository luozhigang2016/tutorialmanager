package com.lab2.tutorialmanager.infra.cache.impl;

/**
 * Cache interface
 * @author : Bo Wang
 * @date : 8/31/2024
 */
public interface CacheIF {
  String putString(String key, String value, int expiredSec);
  String getString(String key);

  boolean delete(String key);
}
