package com.lab2.tutorialmanager.app.dto;

import java.util.Date;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;
/**
 * 项目： tutorialmanager
 * <p>
 * TODO: 说明 ...
 *
 * @author : Bo Wang
 * @date : 8/30/2024
 */
@Data
@Schema(title = "Tutorial", description = "Tutorial DTO")
public class TutorialDTO {
  @Schema(title = "Identity")
  private Long id;
  @Schema(title = "Title")
  private String title;
  @Schema(title = "ContentDescription")
  private String description;
  @Schema(title = "Published")
  private boolean published;
  @Schema(title = "CreateTime")
  private Date createTime;
  @Schema(title = "UpdateTime")
  private Date updateTime;
  @Schema(title = "PublishTime")
  private Date publishTime;
}
