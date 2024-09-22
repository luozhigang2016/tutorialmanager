package com.lab2.tutorialmanager.infra.persistence.po;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.Data;
import lombok.Setter;

/**
 * 项目： tutorialmanager
 * <p>
 * PO
 *
 * @author : Bo Wang
 * @date : 8/31/2024
 */
@Data
@Entity
@Table(name = "tutorials")
public class TutorialPO {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @Column(name = "title")
  private String title;
  @Column(name = "description")
  private String description;
  @Column(name = "published")
  private boolean published;
  @Column(name = "ts_create")
  private Date createTime;
  @Column(name = "ts_update")
  private Date updateTime;
  @Column(name = "ts_publish")
  private Date publishTime;
}
