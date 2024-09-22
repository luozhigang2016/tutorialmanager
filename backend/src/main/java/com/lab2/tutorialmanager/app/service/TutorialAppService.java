package com.lab2.tutorialmanager.app.service;

import com.lab2.tutorialmanager.app.dto.DTOAssembler;
import com.lab2.tutorialmanager.app.dto.TutorialDTO;
import com.lab2.tutorialmanager.domain.manager.model.Tutorial;
import com.lab2.tutorialmanager.domain.manager.service.ManagerService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

/**
 * 项目： tutorialmanager
 * <p>
 * 应用层-服务
 *
 * @author : Bo Wang
 * @date : 8/31/2024
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TutorialAppService {
  final private ManagerService managerService;
  static private DTOAssembler dtoAssembler = Mappers.getMapper( DTOAssembler.class );
  public List<TutorialDTO> findTutorials(String title) {
    List<Tutorial> list=managerService.findTutorials(title);
    return list.stream().map(dtoAssembler::entityToDto).toList();
  }

  public List<TutorialDTO> findAllTutorials() {
    List<Tutorial> list=managerService.findAllTutorials();
    return list.stream().map(dtoAssembler::entityToDto).toList();
  }

  public List<TutorialDTO> findTutorialsPublished() {
    List<Tutorial> list=managerService.findTutorialsPublished();
    return list.stream().map(dtoAssembler::entityToDto).toList();
  }

  public TutorialDTO findTutorialById(long id) {
    Tutorial entity=managerService.findTutorialById(id);
    return dtoAssembler.entityToDto(entity);
  }

  public TutorialDTO createTutorial(String title, String description) {
    Tutorial entity=managerService.createTutorial(title,description);
    return dtoAssembler.entityToDto(entity);
  }

  public TutorialDTO updateTutorial(Long id, String title, String description,
      boolean published) {
    Tutorial entity=managerService.updateTutorial(id,title,description);
    if(published && !entity.isPublished()) {
      entity=managerService.publishTutorial(id);
      log.warn("AppService>>> Published tutorial '{}', send event to downstream system!!!",id);
    }
    if(!published && entity.isPublished()) {
      entity=managerService.unpublishTutorial(id);
      log.warn("AppService>>> Unpublished tutorial '{}', send event to downstream system!!!",id);
    }
    return dtoAssembler.entityToDto(entity);
  }
  public TutorialDTO publishTutorial(Long id) {
    Tutorial entity=managerService.publishTutorial(id);
    return dtoAssembler.entityToDto(entity);
  }

  public TutorialDTO removeTutorial(Long id) {
    Tutorial entity=managerService.removeTutorial(id);
    return dtoAssembler.entityToDto(entity);
  }
}
