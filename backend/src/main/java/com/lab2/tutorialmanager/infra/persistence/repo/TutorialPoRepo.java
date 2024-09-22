package com.lab2.tutorialmanager.infra.persistence.repo;

import com.lab2.tutorialmanager.infra.persistence.po.TutorialPO;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 项目： tutorialmanager
 * <p>
 * TutorialPO JPA Repository
 *
 * @author : Bo Wang
 * @date : 8/31/2024
 */
public interface TutorialPoRepo extends JpaRepository<TutorialPO, Long> {
  List<TutorialPO> findByTitleContainingIgnoreCase(String title);
  List<TutorialPO> findByPublished(boolean published);
}
