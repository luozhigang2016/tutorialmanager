package com.lab2.tutorialmanager.infra.persistence.impl;

import com.lab2.tutorialmanager.domain.manager.model.Tutorial;
import com.lab2.tutorialmanager.domain.manager.port.ManagerDomainPersistenceRepoIF;
import com.lab2.tutorialmanager.infra.persistence.po.DataConverter;
import com.lab2.tutorialmanager.infra.persistence.po.TutorialPO;
import com.lab2.tutorialmanager.infra.persistence.repo.TutorialPoRepo;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Repository;

/**
 * 项目： tutorialmanager
 * <p>
 * Implementation for Domain "Manager" persistence repository interface
 *
 * @author : Bo Wang
 * @date : 8/31/2024
 */
@Slf4j
@RequiredArgsConstructor
@Repository
public class ManagerDomainPersistenceRepo implements ManagerDomainPersistenceRepoIF {

  private final TutorialPoRepo repo;
  static final private DataConverter dataConverter = Mappers.getMapper(DataConverter.class);

  @Override
  public List<Tutorial> findTutorials(String title) {
    if (title == null) {
      title = "";
    }
    return repo.findByTitleContainingIgnoreCase(title).stream().map(dataConverter::poToEntity)
        .toList();
  }

  @Override
  public List<Tutorial> findTutorialsPublished() {
    return repo.findByPublished(true).stream().map(dataConverter::poToEntity)
        .toList();
  }

  @Override
  public Tutorial findTutorialById(long id) {
    Optional<TutorialPO> o = repo.findById(id);
    if (o.isEmpty()) {
      return null;
    }
    TutorialPO po = o.get();
    Tutorial e = dataConverter.poToEntity(po);
    return e;
//    return repo.findById(id).map(dataConverter::poToEntity).orElse(null);
  }

  @Override
  public Long createTutorial(Tutorial obj) {
    TutorialPO po = dataConverter.entityToPO(obj);
    Date now = new Date();
    if (po.getCreateTime() == null) {
      po.setCreateTime(now);
    }
    if (po.getUpdateTime() == null) {
      po.setUpdateTime(now);
    }
    po = repo.save(po);
    return po.getId();
  }

  @Override
  public void updateTutorial(Long id, Tutorial obj) {
    Optional<TutorialPO> o = repo.findById(id);
    if (o.isEmpty()) {
      throw new IllegalArgumentException("TutorialPO(id=" + id + ") does not exist!");
    }
    TutorialPO po = dataConverter.entityToPO(obj);
    po.setId(id);
    repo.save(po);
  }

  @Override
  public void removeTutorial(Long id) {
    Optional<TutorialPO> o = repo.findById(id);
    if (o.isEmpty()) {
      throw new IllegalArgumentException("TutorialPO(id=" + id + ") does not exist!");
    }
    repo.delete(o.get());
  }
}
