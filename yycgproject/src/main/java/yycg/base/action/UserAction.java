package yycg.base.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import yycg.base.pojo.po.Dictinfo;
import yycg.base.pojo.vo.PageQuery;
import yycg.base.pojo.vo.SysuserCustom;
import yycg.base.pojo.vo.SysuserQueryVo;
import yycg.base.process.context.Config;
import yycg.base.process.result.DataGridResultInfo;
import yycg.base.process.result.ExceptionResultInfo;
import yycg.base.process.result.ResultInfo;
import yycg.base.process.result.ResultUtil;
import yycg.base.process.result.SubmitResultInfo;
import yycg.base.service.SystemConfigService;
import yycg.base.service.UserService;


/**
 * 
 * <p>Title: UserAction</p>
 * <p>Description:系统用户管理 </p>
 * <p>Company: www.itcast.com</p> 
 * @author	苗润土
 * @date	2014年11月26日上午10:56:09
 * @version 1.0
 */
@Controller
@RequestMapping("/user")
public class UserAction {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SystemConfigService systemConfigService;
	
	//用户查询页面
	@RequestMapping("/queryuser")
	public String queryuser(Model model)throws Exception{
		List<Dictinfo> Dictinfos = systemConfigService.findDictinfoByType("s01");
		List<Dictinfo> dfs = systemConfigService.findDictinfoByType("002");
		
		model.addAttribute("Dictinfos", Dictinfos);
		model.addAttribute("dfs", dfs);
		//将页面所需要的数据取出，传到页面
		return "/base/user/queryuser";
	}
	
	//用户查询页面结果集
	@RequestMapping("/queryuser_result")
	@ResponseBody
	public DataGridResultInfo queryUser_result(SysuserQueryVo sysuserQueryVo,int page,int rows) throws Exception{
	
		sysuserQueryVo = sysuserQueryVo!=null?sysuserQueryVo:new SysuserQueryVo();
		int total = userService.findSysuserCount(sysuserQueryVo);
		PageQuery pageQuery  = new PageQuery();
		pageQuery.setPageParams(total,rows, page);
		sysuserQueryVo.setPageQuery(pageQuery);
		List<SysuserCustom> customs = userService.findSysuserList(sysuserQueryVo);
		DataGridResultInfo dataGridResultInfo = new DataGridResultInfo();
 		dataGridResultInfo.setRows(customs);
		dataGridResultInfo.setTotal(total);
		return dataGridResultInfo;
	}
	
	
	//添加用户界面
	@RequestMapping("/addsysuser")
	public String addsysuser(Model model) throws Exception{
		return "/base/user/addsysuser";
	}
	
	//添加用户提交
	@RequestMapping("/addsysusersubmit")
	@ResponseBody
	public SubmitResultInfo addsysusersubmit(SysuserQueryVo sysuserQueryVo) throws Exception{
		
		ResultInfo resultInfo = new ResultInfo();
		/*try {
			userService.insertSysuser(sysuserQueryVo.getSysuserCustom());
			resultInfo.setType(ResultInfo.TYPE_RESULT_SUCCESS);
			resultInfo.setMessage("添加成功");
		} catch (Exception e) {
			if(e instanceof ExceptionResultInfo){
				//抛出系统自定义的异常
				resultInfo = ((ExceptionResultInfo) e).getResultInfo();
			}else{
				//重新构造未知错误异常
				resultInfo = new ResultInfo();
				resultInfo.setType(ResultInfo.TYPE_RESULT_FAIL);
				resultInfo.setMessage("未知错误异常");
			}
		}*/
		
		//使用全局异常处理器后不要自动抛出异常
		userService.insertSysuser(sysuserQueryVo.getSysuserCustom());
		resultInfo.setType(ResultInfo.TYPE_RESULT_SUCCESS);
		resultInfo.setMessage("添加成功");
		SubmitResultInfo submitResultInfo = new SubmitResultInfo(resultInfo);
		return submitResultInfo;
	}
	
	/**
	 * 用户删除
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/deletesysuser")
	public SubmitResultInfo deltesysuser(String id) throws Exception{
		//用户删除
		userService.deletesysysuser(id);
		return ResultUtil.createSubmitResult(ResultUtil.createSuccess(Config.MESSAGE,906,null));
	}
	
	
	//用户修改界面
	@RequestMapping("/edituser")
	public String editsysuser(String id,Model model) throws Exception{
		SysuserCustom sysuserCustom = userService.findUserById(id);
		model.addAttribute("sysuserCustom",sysuserCustom);
		return "/base/user/editsysuser";
	}
	
	//修改用户提交
	@ResponseBody
	@RequestMapping("/editsysusersubmit")
	public SubmitResultInfo  editsysusersubmit(String id,SysuserQueryVo sysuserQueryVo) throws Exception{
		 userService.updateSysuser(id, sysuserQueryVo.getSysuserCustom());
		 return ResultUtil.createSubmitResult(ResultUtil.createSuccess(Config.MESSAGE,906,null));
	}
}
