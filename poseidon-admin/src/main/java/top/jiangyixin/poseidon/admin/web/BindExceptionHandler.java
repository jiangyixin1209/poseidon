package top.jiangyixin.poseidon.admin.web;

import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.jiangyixin.poseidon.admin.pojo.vo.R;
import top.jiangyixin.poseidon.core.exception.PoseidonException;

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
	
	/**
	 * 处理Get请求中 使用@Valid 验证路径中请求实体校验失败后抛出的异常
	 * @param request   request
	 * @param bindException bindException
	 * @return  R
	 */
	@ExceptionHandler(BindException.class)
	public R<String> handleBindException(HttpServletRequest request, BindException bindException) {
		List<FieldError> allErrors = bindException.getFieldErrors();
		StringBuilder sb = new StringBuilder();
		for (FieldError errorMessage : allErrors) {
			sb.append(errorMessage.getField()).append(": ").append(errorMessage.getDefaultMessage()).append(", ");
		}
		return R.fail(sb.toString());
	}
	
	/**
	 * 处理请求参数格式错误 @RequestBody上validate失败后抛出的异常
	 * @param request   request
	 * @param exception exception
	 * @return  R
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public R<String> handleMethodArgumentNotValidException(HttpServletRequest request,
	                                                       MethodArgumentNotValidException exception) {
		List<FieldError> allErrors = exception.getBindingResult().getFieldErrors();
		StringBuilder sb = new StringBuilder();
		for (FieldError errorMessage : allErrors) {
			sb.append(errorMessage.getField()).append(": ").append(errorMessage.getDefaultMessage()).append(", ");
		}
		return R.fail(sb.toString());
	}

	@ExceptionHandler(PoseidonException.class)
	public R<String> handlePoseidonException(HttpServletRequest request, PoseidonException poseidonException) {
		return R.fail(poseidonException.getMessage());
	}
}