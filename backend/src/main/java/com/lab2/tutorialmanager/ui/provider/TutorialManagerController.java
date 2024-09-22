package com.lab2.tutorialmanager.ui.provider;

import com.lab2.tutorialmanager.app.dto.TutorialDTO;
import com.lab2.tutorialmanager.app.service.TutorialAppService;
import com.lab2.tutorialmanager.ui.provider.dto.ResultDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目： tutorialmanager
 * <p>
 * 用户接口层-API Provider
 *
 * @author : Bo Wang
 * @date : 8/31/2024
 */
@Slf4j
@CrossOrigin(origins = "*",
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class TutorialManagerController {

  private final TutorialAppService appService;

  @GetMapping("/tutorials/{id}")
  public ResultDTO<TutorialDTO> getTutorialById(@PathVariable("id") long id) {
    long beginTimeMs = System.currentTimeMillis();
    TutorialDTO o = appService.findTutorialById(id);
    if (o == null) {
      return ResultDTO.fail(HttpStatus.NO_CONTENT, "Cannot find tutorials by id:" + id,
          beginTimeMs);
    }
    return ResultDTO.ok(o, beginTimeMs);
  }

  @GetMapping("/tutorials")
  public ResultDTO<List<TutorialDTO>> getAllTutorials(
      @RequestParam(required = false) String title) {
    long beginTimeMs = System.currentTimeMillis();
    List<TutorialDTO> tutorials;
    try {
      if (title == null) {
        tutorials = appService.findAllTutorials();
      } else {
        tutorials = appService.findTutorials(title);
      }

      if (tutorials == null || tutorials.isEmpty()) {
        return ResultDTO.fail(HttpStatus.NO_CONTENT, "Cannot find tutorials by title:" + title,
            beginTimeMs);
      }

      return ResultDTO.ok(tutorials, beginTimeMs);
    } catch (Throwable e) {
      return ResultDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e, beginTimeMs);
    }
  }


  @PostMapping("/tutorials")
  public ResultDTO<TutorialDTO> createTutorial(@RequestBody TutorialDTO input) {
    long beginTimeMs = System.currentTimeMillis();
    try {
      TutorialDTO output = appService.createTutorial(input.getTitle(), input.getDescription());
      return ResultDTO.ok(HttpStatus.CREATED, output, beginTimeMs);
    } catch (Throwable e) {
      return ResultDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e, beginTimeMs);
    }
  }

  @PutMapping("/tutorials/{id}")
  public ResultDTO<TutorialDTO> updateTutorial(@PathVariable("id") long id,
      @RequestBody TutorialDTO input) {
    long beginTimeMs = System.currentTimeMillis();
    try {
      TutorialDTO output = appService.updateTutorial(id, input.getTitle(), input.getDescription(),
          input.isPublished());
      if (output == null) {
        return ResultDTO.fail(HttpStatus.NO_CONTENT, "Cannot find tutorials by id:" + id,
            beginTimeMs);
      }
      return ResultDTO.ok(output, beginTimeMs);
    } catch (Throwable e) {
      return ResultDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e, beginTimeMs);
    }

  }

  @DeleteMapping("/tutorials/{id}")
  public ResultDTO<TutorialDTO> deleteTutorial(@PathVariable("id") long id) {
    long beginTimeMs = System.currentTimeMillis();
    try {
      TutorialDTO output = appService.removeTutorial(id);
      return ResultDTO.ok(HttpStatus.NO_CONTENT, output, beginTimeMs);
    } catch (Throwable e) {
      return ResultDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e, beginTimeMs);
    }
  }

  @GetMapping("/tutorials/published")
  public ResultDTO<List<TutorialDTO>> findByPublished() {
    long beginTimeMs = System.currentTimeMillis();
    try {
      List<TutorialDTO> tutorials = appService.findTutorialsPublished();
      if (tutorials == null || tutorials.isEmpty()) {
        return ResultDTO.fail(HttpStatus.NO_CONTENT, "Cannot find any published tutorials!",
            beginTimeMs);
      }
      return ResultDTO.ok(tutorials, beginTimeMs);
    } catch (Throwable e) {
      return ResultDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e, beginTimeMs);
    }
  }
}
