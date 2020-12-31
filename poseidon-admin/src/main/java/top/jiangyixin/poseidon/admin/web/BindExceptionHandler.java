package top.jiangyixin.poseidon.admin.web;

import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.jiangyixin.poseidon.admin.pojo.vo.R;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * TODO
 * @version 1.0
 * @author jiangyixin
 * @date 2020/12/31 下午4:41
 */
@RestControllerAdvice
public class BindExceptionHandler {
	
	@ExceptionHandler(BindException.class)
	public R<String> handleBindException(HttpServletRequest request, BindException bindException) {
		List<FieldError> allErrors = bindException.getFieldErrors();
		StringBuilder sb = new StringBuilder();
		for (FieldError errorMessage : allErrors) {
			sb.append(errorMessage.getField()).append(": ").append(errorMessage.getDefaultMessage()).append(", ");
		}
		return R.fail(sb.toString());
	}
}