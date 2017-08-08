package yycg.business.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import yycg.base.pojo.po.Dictinfo;
import yycg.base.pojo.vo.PageQuery;
import yycg.base.pojo.vo.SysuserCustom;
import yycg.base.pojo.vo.SysuserQueryVo;
import yycg.base.process.context.Config;
import yycg.base.process.result.DataGridResultInfo;
import yycg.base.process.result.ResultInfo;
import yycg.base.process.result.ResultUtil;
import yycg.base.process.result.SubmitResultInfo;
import yycg.base.service.SystemConfigService;
import yycg.business.pojo.po.Ypxx;
import yycg.business.pojo.vo.YpxxCustom;
import yycg.business.pojo.vo.YpxxQueryVo;
import yycg.business.service.YpxxService;
import yycg.util.ExcelExportSXXSSF;


@Controller
@RequestMapping("/ypml")
public class YpxxAction {

	@Autowired
	private YpxxService ypxxService;

	@Autowired
	private SystemConfigService systemConfigService;


	//导出页面展示
	@RequestMapping("/exportYpxx")
	public String exportYpxx(Model model) throws Exception {

		List<Dictinfo> yplblist = systemConfigService.findDictinfoByType("001");

		List<Dictinfo> jyztlist = systemConfigService.findDictinfoByType("003");

		model.addAttribute("yplblist", yplblist);
		model.addAttribute("jyztlist", jyztlist);

		return "/business/ypml/exportYpxx";
	}


	//用户查询页面结果集
	@RequestMapping("/queryypxx_result")
	@ResponseBody
	public DataGridResultInfo queryypxx_result(YpxxQueryVo ypxxQueryVo,int page,int rows) throws Exception{
		ypxxQueryVo = ypxxQueryVo!=null?ypxxQueryVo:new YpxxQueryVo();
		int total = ypxxService.findYpxxCount(ypxxQueryVo);
		PageQuery pageQuery  = new PageQuery();
		pageQuery.setPageParams(total,rows, page);
		ypxxQueryVo.setPageQuery(pageQuery);
		List<YpxxCustom> list = ypxxService.findList(ypxxQueryVo);
		DataGridResultInfo dataGridResultInfo = new DataGridResultInfo();
		dataGridResultInfo.setRows(list);
		dataGridResultInfo.setTotal(total);
		return dataGridResultInfo;
	}

	//导出页面提交
	@RequestMapping("exportYpxxSubmit")
	public @ResponseBody SubmitResultInfo exportYpxxSubmit(YpxxQueryVo ypxxQueryVo) throws Exception{
		//导出文件存放的路径，并且是虚拟目录指向的路径
		String filePath = "d:/upload/linshi/";
		//导出文件的前缀
		String filePrefix="ypxx";
		//-1表示关闭自动刷新，手动控制写磁盘的时机，其它数据表示多少数据在内存保存，超过的则写入磁盘
		int flushRows=100;

		//指导导出数据的title
		List<String> fieldNames=new ArrayList<String>();
		fieldNames.add("流水号");
		fieldNames.add("通用名");
		fieldNames.add("剂型");
		fieldNames.add("规格");
		fieldNames.add("转换系数");
		fieldNames.add("生产企业");
		fieldNames.add("商品名称");
		fieldNames.add("中标价格");
		fieldNames.add("交易状态");

		//告诉导出类数据list中对象的属性，让ExcelExportSXXSSF通过反射获取对象的值
		List<String> fieldCodes=new ArrayList<String>();
		fieldCodes.add("bm");//药品流水号
		fieldCodes.add("mc");//通用名
		fieldCodes.add("jx");
		fieldCodes.add("gg");
		fieldCodes.add("zhxs");
		fieldCodes.add("scqymc");
		fieldCodes.add("spmc");
		fieldCodes.add("zbjg");
		fieldCodes.add("jyzt");



		//注意：fieldCodes和fieldNames个数必须相同且属性和title顺序一一对应，这样title和内容才一一对应


		//开始导出，执行一些workbook及sheet等对象的初始创建
		ExcelExportSXXSSF excelExportSXXSSF = ExcelExportSXXSSF.start(filePath, "/upload/", filePrefix, fieldNames, fieldCodes, flushRows);

		//准备导出的数据，将数据存入list，且list中对象的字段名称必须是刚才传入ExcelExportSXXSSF的名称
		List<YpxxCustom> list = ypxxService.findList(ypxxQueryVo);

		//执行导出
		excelExportSXXSSF.writeDatasByObject(list);
		//输出文件，返回下载文件的http地址
		String webpath = excelExportSXXSSF.exportFile();

		System.out.println(webpath);

		return ResultUtil.createSubmitResult(ResultUtil.createSuccess(Config.MESSAGE,313, new Object[]{list.size(),webpath}));
	}



	//添加药品信息界面
	@RequestMapping("/addypxx")
	public String addsysuser(Model model) throws Exception{
		List<Dictinfo> yplblist = systemConfigService.findDictinfoByType("001");
		List<Dictinfo> jyztlist = systemConfigService.findDictinfoByType("003");
		model.addAttribute("yplblist", yplblist);
		model.addAttribute("jyztlist", jyztlist);
		return "/business/ypml/addypxx";
	}


	//修改药品目录
	@RequestMapping("/addypxxsubmit")
	@ResponseBody
	public SubmitResultInfo addypxxSubmit(Model model,YpxxQueryVo ypxxQueryVo) throws Exception{
		ResultInfo resultInfo = new ResultInfo();
		ypxxService.insertYpxx(ypxxQueryVo.getYpxxCustom());
		resultInfo.setType(ResultInfo.TYPE_RESULT_SUCCESS);
		resultInfo.setMessage("添加成功");
		SubmitResultInfo submitResultInfo = new SubmitResultInfo(resultInfo);
		return submitResultInfo;
	}
	
	
	
	   //修改药品信息界面
		@RequestMapping("/editypxx")
		public String editypxx(Model model,String id) throws Exception{
			Ypxx ypxx = ypxxService.findById(id);
			model.addAttribute("ypxx", ypxx);
			List<Dictinfo> yplblist = systemConfigService.findDictinfoByType("001");
			List<Dictinfo> jyztlist = systemConfigService.findDictinfoByType("003");
			model.addAttribute("yplblist", yplblist);
			model.addAttribute("jyztlist", jyztlist);
			return "/business/ypml/editypxx";
		}
		
		
		 //修改药品信息
		@RequestMapping("/editypxxsubmit")
		@ResponseBody
		public SubmitResultInfo editypxxsubmit(String id,YpxxQueryVo ypxxQueryVo) throws Exception{
			
			ypxxService.updateYpxx(id, ypxxQueryVo.getYpxxCustom());
			//ypxxService.updateSysuser(id, sysuserQueryVo.getSysuserCustom());
			return ResultUtil.createSubmitResult(ResultUtil.createSuccess(Config.MESSAGE,906,null));
	
		}
		
		
		/**
		 * 删除药品
		 */
		@RequestMapping(value="/delypxx",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
		@ResponseBody
		public SubmitResultInfo delypxx(String id) throws Exception{
			ypxxService.deleteypxx(id);
			return ResultUtil.createSubmitResult(ResultUtil.createSuccess(Config.MESSAGE,906,null));
		}
		
		
}
