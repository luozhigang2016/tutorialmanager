package com.lab2.tutorialmanager.domain.manager.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 项目： tutorialmanager
 * <p>
 * TODO: 说明 ...
 *
 * @author : Bo Wang
 * @date : 8/30/2024
 */
@Getter
@ToString
@Builder
@AllArgsConstructor
public class Tutorial {

  private Tutorial() {
  }

  private Long id;
  @Setter
  private String title;
  @Setter
  private String description;
  private boolean published;
  private Date createTime;
  @Setter
  private Date updateTime;
  private Date publishTime;

  public void publish() {
    //TODO Execute domain logic to do some business check before publish
    published = true;
    Date now = new Date();
    publishTime = now;
    updateTime = now;
    //TODO: Post publish logic
  }

  public void unpublish() {
    //TODO Execute domain logic to do some business check before unpublish
    published = false;
    publishTime = null;
    updateTime = new Date();
    //TODO: Post unpublish logic
  }

}
