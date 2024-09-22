package com.lab2.tutorialmanager.app.dto;

import com.lab2.tutorialmanager.domain.manager.model.Tutorial;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 项目： tutorialmanager
 * <p>
 * DTOAssembler
 *
 * @author : Bo Wang
 * @date : 8/31/2024
 */
@Mapper
public interface DTOAssembler {
  TutorialDTO entityToDto(Tutorial entity);
  Tutorial dtoToEntity(TutorialDTO entity);
}
