package com.lab2.tutorialmanager.domain.manager.service;

import com.lab2.tutorialmanager.domain.common.service.CacheService;
import com.lab2.tutorialmanager.domain.manager.model.Tutorial;
import com.lab2.tutorialmanager.domain.manager.port.ManagerDomainPersistenceRepoIF;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

/**
 * 项目： tutorialmanager
 * <p>
 * 领域服务-Manager Service
 *
 * @author : Bo Wang
 * @date : 8/30/2024
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ManagerService {

  private final ManagerDomainPersistenceRepoIF repoPersistence;
  private final CacheService cacheService;


  @Data
  @Configuration
  @ConfigurationProperties(prefix = "lab2.tutorial-manager.domain-manager")
  static public class Config {

    int cacheExpirationSec = 60;
    List<String> wordBlackList = new ArrayList<>();//Word cannot exist in title and description
  }

  final private Config config;

  public List<Tutorial> findTutorials(String title) {
    return repoPersistence.findTutorials(title);
  }

  public List<Tutorial> findAllTutorials() {
    return repoPersistence.findTutorials(null);
  }

  public List<Tutorial> findTutorialsPublished() {
    return repoPersistence.findTutorialsPublished();
  }

  public Tutorial findTutorialById(long id) {
    Tutorial e = cacheService.get(id, Tutorial.class);
    if (e != null) {
      if (log.isDebugEnabled()) {
        log.debug("Found entity from cache with id '{}': {}", id, e);
      }
      return e;
    }
    e = repoPersistence.findTutorialById(id);
    if (e == null) {
      return null;
    }
    if (log.isDebugEnabled()) {
      log.debug("Found entity from DB with id '{}': {}", id, e);
    }
    cacheService.put(id, e, config.getCacheExpirationSec());
    if (log.isDebugEnabled()) {
      log.debug("Put entity to cache with id '{}': {}", id, e);
    }
    return e;
  }

  public Tutorial createTutorial(String title, String description) {
    if (checkBlackWord(title)) {
      throw new IllegalArgumentException("Found black list word(s) in title: " + title);
    }
    if (checkBlackWord(description)) {
      throw new IllegalArgumentException("Found black list word(s) in description: " + description);
    }
    Tutorial e = Tutorial.builder().title(title).description(description).build();
    long id = repoPersistence.createTutorial(e);
    e = repoPersistence.findTutorialById(id);
    cacheService.put(id, e, config.getCacheExpirationSec());
    if (log.isDebugEnabled()) {
      log.debug("Create entity and put to cache with id '{}': {}", id, e);
    }
    return e;
  }

  public Tutorial updateTutorial(Long id, String title, String description) {
    if (checkBlackWord(title)) {
      throw new IllegalArgumentException("Found black list word(s) in title: " + title);
    }
    if (checkBlackWord(description)) {
      throw new IllegalArgumentException("Found black list word(s) in description: " + description);
    }
    Tutorial e = findTutorialById(id);
    if (e == null) {
      throw new IllegalArgumentException(
          String.format("Cannot find tutorial by id '%d' while updating!", id));
    }
    if (title != null) {
      e.setTitle(title);
    }
    if (description != null) {
      e.setDescription(description);
    }
    e.setUpdateTime(new Date());
    cacheService.delete(id, Tutorial.class);
    repoPersistence.updateTutorial(id, e);
    return e;
  }

  private boolean checkBlackWord(String content) {
    if (content == null) {
      return false;
    }
    List<String> contentWords = new ArrayList<>(Arrays.asList(content.split("\\W+")));
    contentWords.retainAll(config.getWordBlackList());
    boolean blocked = !contentWords.isEmpty();
    if (blocked) {
      if (log.isDebugEnabled()) {
        log.debug("Found black list word '{}' in content:\n{}", contentWords, content);
      }
    }
    return blocked;
  }

  public Tutorial publishTutorial(Long id) {
    Tutorial e = findTutorialById(id);
    if (e == null) {
      throw new IllegalArgumentException(
          String.format("Cannot find tutorial by id '%d' while updating!", id));
    }
    e.publish();
    cacheService.delete(id, Tutorial.class);
    repoPersistence.updateTutorial(id, e);
    return e;
  }

  public Tutorial unpublishTutorial(Long id) {
    Tutorial e = findTutorialById(id);
    if (e == null) {
      throw new IllegalArgumentException(
          String.format("Cannot find tutorial by id '%d' while updating!", id));
    }
    e.unpublish();
    cacheService.delete(id, Tutorial.class);
    repoPersistence.updateTutorial(id, e);
    return e;
  }

  public Tutorial removeTutorial(Long id) {
    Tutorial e = findTutorialById(id);
    if (e == null) {
      throw new IllegalArgumentException(
          String.format("Cannot find tutorial by id '%d' while updating!", id));
    }
    cacheService.delete(id, Tutorial.class);
    repoPersistence.removeTutorial(id);
    return e;
  }


}
