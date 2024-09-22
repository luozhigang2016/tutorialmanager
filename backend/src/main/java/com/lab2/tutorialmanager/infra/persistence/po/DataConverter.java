package com.lab2.tutorialmanager.infra.persistence.po;

import com.lab2.tutorialmanager.domain.manager.model.Tutorial;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * 项目： tutorialmanager
 * <p>
 * Data Converter
 *
 * @author : Bo Wang
 * @date : 8/31/2024
 */
@Mapper
public interface DataConverter {
  TutorialPO entityToPO(Tutorial entity);
  Tutorial poToEntity(TutorialPO po);
}
