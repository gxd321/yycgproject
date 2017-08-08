package yycg.base.process.exception;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import yycg.base.process.result.ExceptionResultInfo;
import yycg.base.process.result.ResultInfo;

/**
 * 全局异常处理器
 * @author Administrator
 */
public class ExceptionResolverCustom implements HandlerExceptionResolver {
	//json转换器,将异常信息转成json
	private HttpMessageConverter<ExceptionResultInfo> jsonMessageConverter;
 	
	
	public HttpMessageConverter<ExceptionResultInfo> getJsonMessageConverter() {
		return jsonMessageConverter;
	}
	public void setJsonMessageConverter(HttpMessageConverter<ExceptionResultInfo> jsonMessageConverter) {
		this.jsonMessageConverter = jsonMessageConverter;
	}


  
	//前端控制器调用此方法执行异常处理
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		ex.printStackTrace();
		
		HandlerMethod handlerMethod = (HandlerMethod)handler;
		//取出方法
		Method method = handlerMethod.getMethod();
		//判断方法是否返回json
		//只要方法上是否有@responseBody的表示返回json
		ResponseBody responseBody = AnnotationUtils.findAnnotation(method, ResponseBody.class);
		if(responseBody!=null){
			//将异常信息转成json输入
			return this.resolveJsonException(request,response,handlerMethod,ex);
		}
		//说明action返回的是jsp界面
		//解析异常
		ExceptionResultInfo exceptionResultInfo = resolveExceptionCustom(ex);
		
		String views = "/base/error";
		int mcode = exceptionResultInfo.getResultInfo().getMessageCode();
		if(mcode==106){
			//views = "/base/login";
			try {
				response.sendRedirect("/yycgproject/login.action");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//将异常信息在异常界面显示
		request.setAttribute("exceptionResultInfo", exceptionResultInfo.getResultInfo());
		//转向错误页面
		ModelAndView modelAndView  = new ModelAndView();
		modelAndView.addObject("exceptionResultInfo", exceptionResultInfo.getResultInfo());
		modelAndView.setViewName(views);
		return modelAndView;
	}
	//将异常信息转json
	private ModelAndView resolveJsonException(HttpServletRequest request, HttpServletResponse response,
			HandlerMethod handlerMethod, Exception ex) {
		//解析异常
		ExceptionResultInfo exceptionResultInfo = resolveExceptionCustom(ex);
		
		HttpOutputMessage httpOutputMessage = new ServletServerHttpResponse(response);
		
		try {
			//将exceptionResultInfo转成json输出
			jsonMessageConverter.write(exceptionResultInfo,MediaType.APPLICATION_JSON, httpOutputMessage);
		} catch (HttpMessageNotWritableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ModelAndView();
	}
	//异常信息解析方法
	private ExceptionResultInfo resolveExceptionCustom(Exception ex) {
		ResultInfo resultInfo = null;
		if(ex instanceof ExceptionResultInfo){
			//抛出系统自定义的异常
			resultInfo = ((ExceptionResultInfo)ex).getResultInfo();
		}else{
			//重新构造未知错误异常
			resultInfo = new ResultInfo();
			resultInfo.setType(ResultInfo.TYPE_RESULT_FAIL);
			resultInfo.setMessage("未知错误!");
		}
		return new ExceptionResultInfo(resultInfo);
	}
	
	

}
