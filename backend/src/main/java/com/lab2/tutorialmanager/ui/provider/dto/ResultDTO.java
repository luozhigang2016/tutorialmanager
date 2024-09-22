package com.lab2.tutorialmanager.ui.provider.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.io.PrintWriter;
import java.io.StringWriter;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * 项目： tutorialmanager
 * <p>
 *
 * @author : Bo Wang
 * @date : 8/31/2024
 */
@Schema(title = "OpenAPI Result DTO")
@Data
public class ResultDTO<T> {

  @Schema(title = "ResultCode", description = "Use HTTP Code", requiredMode = RequiredMode.REQUIRED)
  private int code;
  @Schema(title = "Timestamp", description = "Java ms", format = "int64", requiredMode = RequiredMode.REQUIRED)
  private long timestamp = System.currentTimeMillis();
  @Schema(title = "Message", requiredMode = RequiredMode.REQUIRED)
  private String message;
  @Schema(title = "Detail Message")
  private String detail;
  @Schema(description = "DataPayload")
  private T data;
  @Schema(title = "executeTime", description = "ms. -1 means N/A", format = "int32")
  private long executeTime = -1L;

  static public <T> ResultDTO<T> ok(HttpStatus code, String msg, T data, long beginTimeMs) {
    if (!code.is2xxSuccessful()) {
      throw new IllegalArgumentException("Cannot call OK with error http code:" + code);
    }
    ResultDTO<T> result = new ResultDTO<>();
    result.code = code.value();
    result.message = msg;
    result.detail = "Success";
    result.data = data;
    if (beginTimeMs > 0) {
      result.executeTime = result.timestamp - beginTimeMs;
    }
    return result;
  }

  static public <T> ResultDTO<T> ok(HttpStatus code, T data, long beginTimeMs) {
    return ok(code, "OK", data, beginTimeMs);
  }

  static public <T> ResultDTO<T> ok(T data) {
    return ok(HttpStatus.OK, "OK", data, -1L);
  }

  static public <T> ResultDTO<T> ok(T data, long beginTimeMs) {
    return ok(HttpStatus.OK, "OK", data, beginTimeMs);
  }

  static private <T> ResultDTO<T> fail(int code, String msg, String detail, long beginTimeMs) {
    ResultDTO<T> result = new ResultDTO<>();
    result.code = code;
    result.message = msg;
    result.detail = detail;
    if (beginTimeMs > 0) {
      result.executeTime = result.timestamp - beginTimeMs;
    }
    return result;
  }

  static public <T> ResultDTO<T> fail(HttpStatus code, String msg) {
    return fail(code.value(), msg, null, -1);
  }

  static public <T> ResultDTO<T> fail(HttpStatus code, String msg, long beginTimeMs) {
    return fail(code.value(), msg, null, beginTimeMs);
  }

  static public <T> ResultDTO<T> fail(HttpStatus code, String msg, Throwable exception) {
    return fail(code.value(), msg, getDetailFromException(exception), -1L);
  }

  static public <T> ResultDTO<T> fail(HttpStatus code, String msg, Throwable exception,
      long beginTimeMs) {
    return fail(code.value(), msg, getDetailFromException(exception), beginTimeMs);
  }

  static private String getDetailFromException(Throwable exception) {
    StringWriter sw = new StringWriter();
    exception.printStackTrace(new PrintWriter(sw));
    String detail = sw.toString();
    return detail;
  }
}
