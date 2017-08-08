package yycg.base.service;

import java.util.List;

import yycg.base.pojo.po.Sysuser;
import yycg.base.pojo.po.Usergys;
import yycg.base.pojo.po.Userjd;
import yycg.base.pojo.po.Useryy;
import yycg.base.pojo.vo.ActiveUser;
import yycg.base.pojo.vo.SysuserCustom;
import yycg.base.pojo.vo.SysuserQueryVo;

public interface UserService {
	
	//根据用户id查询用户信息
	public Sysuser findSysuserById(String id) throws Exception;
	
	//查询用户列表
	public List<SysuserCustom> findSysuserList(SysuserQueryVo sysuserQueryVo) throws Exception;
	
	//查询总数
	public int findSysuserCount(SysuserQueryVo sysuserQueryVo) throws Exception;
	
	//向sysuser 表插入一条记录
	public void insertSysuser(SysuserCustom sysuserCustom) throws Exception;
	
	//根据用户账号查询用户
	public Sysuser findSysuserByUserId(String userId) throws Exception;
	
	
	//根据单位id查询单位
	public Userjd findUserjdByMc(String mc) throws Exception;
	
	public Useryy findUseryyByMc(String mc) throws Exception;
	
	public Usergys findUsergysByMc(String mc) throws Exception;
	
	//用户删除
	public void deletesysysuser(String id) throws Exception;
	
	
	//用户更新
	public void updateSysuser(String id,SysuserCustom sysuserCustom) throws Exception;
	
	
	//根据用户的id查询用户
	public SysuserCustom findUserById(String id) throws Exception;
	
	
	//校验用户信息
	public ActiveUser checkUserInfo(String userid,String pwd) throws Exception;
	
	
}
