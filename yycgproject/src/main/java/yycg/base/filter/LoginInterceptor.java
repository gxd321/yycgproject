package yycg.base.filter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import yycg.base.pojo.vo.ActiveUser;
import yycg.base.process.context.Config;
import yycg.base.process.result.ResultUtil;
import yycg.util.ResourcesUtil;

public class LoginInterceptor  implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		ActiveUser activeUser = (ActiveUser) request.getSession().getAttribute(Config.ACTIVEUSER_KEY);
		if(activeUser!=null){
			return true;
		}
		//用户访问的uri
		String path = request.getRequestURI();
		
		//校验用户访问的是否是公开资源地址
		List<String> open_urls = ResourcesUtil.gekeyList(Config.ANONYMOUS_ACTIONS);
		for(String open_url:open_urls){
			if(path.indexOf(open_url)>=0){
				return true;
			}
		}
		
		//request.getRequestDispatcher("/WEB-INF/jsp/base/login.jsp").forward(request, response);
		
		//抛出异常。需要登录后才能继续
		ResultUtil.throwExcepion(ResultUtil.createWarning(Config.MESSAGE,106, null));
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}

}
