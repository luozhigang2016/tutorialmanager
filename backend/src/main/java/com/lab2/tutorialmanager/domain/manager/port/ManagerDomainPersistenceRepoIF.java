package com.lab2.tutorialmanager.domain.manager.port;

import com.lab2.tutorialmanager.domain.manager.model.Tutorial;
import java.util.List;

/**
 * 项目： tutorialmanager
 * <p>
 * TODO: 说明 ...
 *
 * @author : Bo Wang
 * @date : 8/30/2024
 */
 public interface ManagerDomainPersistenceRepoIF {
   List<Tutorial> findTutorials(String title);
   List<Tutorial> findTutorialsPublished();
   Tutorial findTutorialById(long id);
   Long createTutorial(Tutorial obj);
   void updateTutorial(Long id,Tutorial obj);
   void removeTutorial(Long id);
}
