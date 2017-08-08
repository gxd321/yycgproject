package yycg.base.service.impl;

import java.util.List;

import org.bouncycastle.crypto.digests.MD5Digest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import yycg.base.dao.mapper.SysuserMapper;
import yycg.base.dao.mapper.SysuserMapperCustom;
import yycg.base.dao.mapper.UsergysMapper;
import yycg.base.dao.mapper.UserjdMapper;
import yycg.base.dao.mapper.UseryyMapper;
import yycg.base.pojo.po.Sysuser;
import yycg.base.pojo.po.SysuserExample;
import yycg.base.pojo.po.Usergys;
import yycg.base.pojo.po.UsergysExample;
import yycg.base.pojo.po.Userjd;
import yycg.base.pojo.po.UserjdExample;
import yycg.base.pojo.po.Useryy;
import yycg.base.pojo.po.UseryyExample;
import yycg.base.pojo.vo.ActiveUser;
import yycg.base.pojo.vo.SysuserCustom;
import yycg.base.pojo.vo.SysuserQueryVo;
import yycg.base.process.context.Config;
import yycg.base.process.result.ExceptionResultInfo;
import yycg.base.process.result.ResultInfo;
import yycg.base.process.result.ResultUtil;
import yycg.base.service.UserService;
import yycg.util.MD5;
import yycg.util.ResourcesUtil;
import yycg.util.UUIDBuild;

@Service
public class UserServiceImpl implements UserService {

	// 注入 mapper
	@Autowired
	private SysuserMapper sysuserMapper;

	@Autowired
	private SysuserMapperCustom sysuserMapperCustom;

	@Autowired
	private UserjdMapper userjdMapper;

	@Autowired
	private UseryyMapper useryyMapper;

	@Autowired
	private UsergysMapper usergysMapper;

	@Override
	public Sysuser findSysuserById(String id) throws Exception {
		// 调用mapper查询用户信息
		return sysuserMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<SysuserCustom> findSysuserList(SysuserQueryVo sysuserQueryVo) throws Exception {
		return sysuserMapperCustom.findSysuserList(sysuserQueryVo);
	}

	@Override
	public int findSysuserCount(SysuserQueryVo sysuserQueryVo) throws Exception {
		return sysuserMapperCustom.findSysuserCount(sysuserQueryVo);
	}

	@Override
	public void insertSysuser(SysuserCustom sysuserCustom) throws Exception {
		// 参数校验
		// 通用的参数合法性校验，非空校验，长度校验，使用工具类进行校验

		// 业务数据的合法性校验
		// 输入的账号重复校验
		Sysuser sysuser = findSysuserByUserId(sysuserCustom.getUserid());
		if (sysuser != null) {
			// throw new Exception("账号重复");
			// ResultInfo resultInfo = new ResultInfo();
			// resultInfo.setType(ResultInfo.TYPE_RESULT_FAIL);
			// resultInfo.setMessage("账号重复");
			// String message =
			// ResourcesUtil.getValue("resources.message","213");
			// resultInfo.setMessage(message);
			// throw new ExceptionResultInfo(resultInfo);
			// 使用ResultUtil来构造resultInfo
			ResultInfo resultInfo = ResultUtil.createFail(Config.MESSAGE, 213, null);
			throw new ExceptionResultInfo(resultInfo);
		}

		// 输入的单位名称存在校验
		String gid = sysuserCustom.getGroupid();
		String sysid = "";

		if (gid.equals("1") || gid.equals("2")) {
			Userjd userjd = findUserjdByMc(sysuserCustom.getSysmc());
			if (userjd == null) {
				// throw new Exception("单位名称输入错误");
				// 使用系统自定义的异常类
				ResultInfo resultInfo = new ResultInfo();
				resultInfo.setType(ResultInfo.TYPE_RESULT_FAIL);
				resultInfo.setMessage("单位名称重复");
				throw new ExceptionResultInfo(resultInfo);
			}
			sysid = userjd.getId();

		} else if (gid.equals("3")) {
			Useryy useryy = findUseryyByMc(sysuserCustom.getSysmc());

			if (useryy == null) {
				// throw new Exception("单位名称输入错误");
				ResultInfo resultInfo = new ResultInfo();
				resultInfo.setType(ResultInfo.TYPE_RESULT_FAIL);
				resultInfo.setMessage("单位名称重复");
				throw new ExceptionResultInfo(resultInfo);
			}
			sysid = useryy.getId();
		} else if (gid.equals("4")) {
			Usergys usergys = findUsergysByMc(sysuserCustom.getSysmc());
			if (usergys == null) {
				// throw new Exception("单位名称输入错误");
				ResultInfo resultInfo = new ResultInfo();
				resultInfo.setType(ResultInfo.TYPE_RESULT_FAIL);
				resultInfo.setMessage("单位名称重复");
				throw new ExceptionResultInfo(resultInfo);
			}
			sysid = usergys.getId();
		}

		sysuserCustom.setId(UUIDBuild.getUUID());
		sysuserCustom.setSysid(sysid);
		sysuserCustom.setPwd(new MD5().getMD5ofStr(sysuserCustom.getPwd()));
		sysuserMapper.insert(sysuserCustom);
		
	}

	@Override
	public Sysuser findSysuserByUserId(String userId) throws Exception {
		SysuserExample example = new SysuserExample();
		example.createCriteria().andUseridEqualTo(userId);
		List<Sysuser> list = sysuserMapper.selectByExample(example);
		if (list != null && list.size() == 1) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public Userjd findUserjdByMc(String mc) throws Exception {
		UserjdExample example = new UserjdExample();
		example.createCriteria().andMcEqualTo(mc);
		List<Userjd> list = userjdMapper.selectByExample(example);
		if (list != null && list.size() == 1) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public Useryy findUseryyByMc(String mc) throws Exception {
		UseryyExample example = new UseryyExample();
		example.createCriteria().andMcEqualTo(mc);
		List<Useryy> list = useryyMapper.selectByExample(example);
		if (list != null && list.size() == 1) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public Usergys findUsergysByMc(String mc) throws Exception {
		UsergysExample example = new UsergysExample();
		example.createCriteria().andMcEqualTo(mc);
		List<Usergys> list = usergysMapper.selectByExample(example);
		if (list != null && list.size() == 1) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public void deletesysysuser(String id) throws Exception {
		// 校验，验证用户是否存在
		Sysuser sysuser = sysuserMapper.selectByPrimaryKey(id);
		if (sysuser == null) {
			throw new ExceptionResultInfo(ResultUtil.createFail(Config.MESSAGE, 212, null));
		}
		// 执行删除
		sysuserMapper.deleteByPrimaryKey(id);
	}

	// 用户更新
	@Override
	public void updateSysuser(String id, SysuserCustom sysuserCustom) throws Exception {
		// 页面提交的账号
		String page_uid = sysuserCustom.getUserid();

		Sysuser sysuser = sysuserMapper.selectByPrimaryKey(id);
		if (sysuser == null) {
			throw new ExceptionResultInfo(ResultUtil.createFail(Config.MESSAGE, 711, null));
		}
		String userid = sysuser.getUserid();
		// 不一样的时候代表更新了
		if (!page_uid.equals(userid)) {
			// 根据页面传的uid查询对象,如果查询到了说明别人已经存在这个账号了
			Sysuser suser = findSysuserByUserId(page_uid);
			if (suser != null) {
				throw new ExceptionResultInfo(ResultUtil.createFail(Config.MESSAGE, 213, null));
			}
		}

		String gid = sysuserCustom.getGroupid();
		String sysid = "";

		if (gid.equals("1") || gid.equals("2")) {
			Userjd userjd = findUserjdByMc(sysuserCustom.getSysmc());
			if (userjd == null) {
				// throw new Exception("单位名称输入错误");
				// 使用系统自定义的异常类
				throw new ExceptionResultInfo(ResultUtil.createFail(Config.MESSAGE, 217, null));
			}
			sysid = userjd.getId();

		} else if (gid.equals("3")) {
			Useryy useryy = findUseryyByMc(sysuserCustom.getSysmc());

			if (useryy == null) {
				// throw new Exception("单位名称输入错误");
				throw new ExceptionResultInfo(ResultUtil.createFail(Config.MESSAGE, 217, null));
			}
			sysid = useryy.getId();
		} else if (gid.equals("4")) {
			Usergys usergys = findUsergysByMc(sysuserCustom.getSysmc());
			if (usergys == null) {
				// throw new Exception("单位名称输入错误");
				throw new ExceptionResultInfo(ResultUtil.createFail(Config.MESSAGE, 217, null));
			}
			sysid = usergys.getId();
		}

		String pwd_page = sysuserCustom.getPwd().trim();
		String pwd_md5 = null;
		

		// 更新用户信息
		// 先根据id查询出要更新的
		Sysuser sysuser_update = sysuserMapper.selectByPrimaryKey(id);
		sysuser_update.setUserid(sysuserCustom.getUserid());
		sysuser_update.setUsername(sysuserCustom.getUsername());
		// 说明用户修改密码了
		if (pwd_page != null && pwd_page != "") {
			pwd_md5 = new MD5().getMD5ofStr(pwd_page);
			sysuser_update.setPwd(pwd_md5);
		}
		
		sysuser_update.setGroupid(sysuserCustom.getGroupid());
		sysuser_update.setUserstate(sysuserCustom.getUserstate());
		sysuser_update.setSysid(sysid);

		sysuserMapper.updateByPrimaryKey(sysuser_update);

	}

	@Override
	public SysuserCustom findUserById(String id) throws Exception {
		Sysuser sysuser = sysuserMapper.selectByPrimaryKey(id);
		String gid = sysuser.getGroupid();
		String sysid = sysuser.getSysid();
		String sysmc = "";

		if (gid.equals("1") || gid.equals("2")) {
			Userjd userjd = userjdMapper.selectByPrimaryKey(sysid);
				// throw new Exception("单位名称输入错误");
				// 使用系统自定义的异常类
			if(userjd==null){
				throw new ExceptionResultInfo(ResultUtil.createFail(Config.MESSAGE, 217, null));
			}
			sysmc = userjd.getMc();

		} else if (gid.equals("3")) {
			Useryy useryy = useryyMapper.selectByPrimaryKey(sysid);
			if (useryy == null) {
				// throw new Exception("单位名称输入错误");
				throw new ExceptionResultInfo(ResultUtil.createFail(Config.MESSAGE, 217, null));
			}
			sysmc = useryy.getMc();
		} else if (gid.equals("4")) {
			Usergys usergys = usergysMapper.selectByPrimaryKey(sysid);;
			if (usergys == null) {
				// throw new Exception("单位名称输入错误");
				throw new ExceptionResultInfo(ResultUtil.createFail(Config.MESSAGE, 217, null));
			}
			sysmc = usergys.getMc();
		}
		SysuserCustom sysuserCustom = new SysuserCustom();
		BeanUtils.copyProperties(sysuser,sysuserCustom);
		sysuserCustom.setSysmc(sysmc);
		return sysuserCustom;
	}

	@Override
	public ActiveUser checkUserInfo(String userid, String pwd) throws Exception {
		//校验用户是否存在
		Sysuser sysuser = findSysuserByUserId(userid);
		if(sysuser==null){
			//用户不存在
			ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE,101 ,null));
		}
		//检查密码是否合法
		String pwd_db = sysuser.getPwd();
		String pwd_md5 = new MD5().getMD5ofStr(pwd);
		if(!pwd_md5.equals(pwd_db)){
			ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE, 117, null));
		}
		String gid = sysuser.getGroupid();
		String sysid = sysuser.getSysid();
		String sysmc = "";

		if (gid.equals("1") || gid.equals("2")) {
			Userjd userjd = userjdMapper.selectByPrimaryKey(sysid);
				// throw new Exception("单位名称输入错误");
				// 使用系统自定义的异常类
			if(userjd==null){
				throw new ExceptionResultInfo(ResultUtil.createFail(Config.MESSAGE, 217, null));
			}
			sysmc = userjd.getMc();

		} else if (gid.equals("3")) {
			Useryy useryy = useryyMapper.selectByPrimaryKey(sysid);
			if (useryy == null) {
				// throw new Exception("单位名称输入错误");
				throw new ExceptionResultInfo(ResultUtil.createFail(Config.MESSAGE, 217, null));
			}
			sysmc = useryy.getMc();
		} else if (gid.equals("4")) {
			Usergys usergys = usergysMapper.selectByPrimaryKey(sysid);;
			if (usergys == null) {
				// throw new Exception("单位名称输入错误");
				throw new ExceptionResultInfo(ResultUtil.createFail(Config.MESSAGE, 217, null));
			}
			sysmc = usergys.getMc();
		}
		
		ActiveUser activeUser  = new ActiveUser();
		activeUser.setUserid(userid);
		activeUser.setUsername(sysuser.getUsername());
		activeUser.setGroupid(sysuser.getGroupid());
		activeUser.setSysid(sysuser.getSysid());
		activeUser.setSysmc(sysmc);
		return activeUser;
	}

}
