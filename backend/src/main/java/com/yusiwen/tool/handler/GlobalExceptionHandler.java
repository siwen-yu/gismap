package com.yusiwen.tool.handler;

import com.yusiwen.tool.dto.ResponseMsg;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author yusiwen
 * @date 2020/7/21 15:44
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseMsg myErrorHandler(MethodArgumentNotValidException ex) {
        return ResponseMsg.failed("缺少必要参数");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseMsg handleException(Exception e) {
        e.printStackTrace();
        return ResponseMsg.failed("服务器处理错误" + e.getMessage());
    }

    @ExceptionHandler({IOException.class, SQLException.class, ClassNotFoundException.class})
    @ResponseBody
    public ResponseMsg handleAreaException(Exception e) {
        return ResponseMsg.failed("data parse error：" + e.getMessage());
    }
}
