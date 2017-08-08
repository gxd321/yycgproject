package yycg.base.action;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import yycg.base.pojo.vo.ActiveUser;
import yycg.base.process.context.Config;
import yycg.base.process.result.ResultUtil;
import yycg.base.process.result.SubmitResultInfo;
import yycg.base.service.UserService;

/**
 * 用户认证
 * @author Administrator
 *
 */
@Controller
public class LoginAction {
	@Autowired
	private UserService userService;
	
	
	//用户登录界面
	@RequestMapping("/login")
	
	public String login(Model model) throws Exception{
		return "/base/login";
	}
	
	
	//用户登录提交
	@RequestMapping("/loginsubmit")
	public @ResponseBody SubmitResultInfo loginSubmit(HttpSession session,String userid,String pwd,String randomcode) throws Exception{
		String validateCode = (String) session.getAttribute("validateCode");
		/**
		 * 校验验证码
		 */
		if(!randomcode.equals(validateCode)){
			ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE, 113,
					null));
		}
		//service 用户认证
		ActiveUser activeUser = userService.checkUserInfo(userid, pwd);
		session.setAttribute(Config.ACTIVEUSER_KEY, activeUser);
		return ResultUtil.createSubmitResult(ResultUtil.createSuccess(Config.MESSAGE,107, new Object[]{}));
		
	}
	
	@RequestMapping("/logout")
	public String logout(HttpSession session){
		session.invalidate();
		return "redirect:login.action";
	}
}
