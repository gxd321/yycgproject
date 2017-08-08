package yycg.business.service;

import java.util.List;

import yycg.base.pojo.po.Sysuser;
import yycg.base.pojo.po.Usergys;
import yycg.base.pojo.po.Userjd;
import yycg.base.pojo.po.Useryy;
import yycg.base.pojo.vo.ActiveUser;
import yycg.base.pojo.vo.SysuserCustom;
import yycg.base.pojo.vo.SysuserQueryVo;
import yycg.business.pojo.po.Ypxx;
import yycg.business.pojo.vo.YpxxCustom;
import yycg.business.pojo.vo.YpxxQueryVo;



public interface YpxxService {
	/**
	 * 药品目录查询
	 */
	public List<YpxxCustom> findList(YpxxQueryVo ypxxQueryVo) throws Exception;
	
	/**
	 * 药品查询总数
	 */
	public  int findYpxxCount(YpxxQueryVo ypxxQueryVo) throws Exception;
	
	//向药品信息表插入一条目录
	public void insertYpxx(YpxxCustom ypxxCustom) throws Exception;
	
	//根据药品信息id 查询出一条药品记录
	public Ypxx  findById(String id) throws Exception;
	
	//药品信息更新
    public void updateYpxx(String id,YpxxCustom ypxxCustom) throws Exception;
    
    
    //药品信息删除
  	public void deleteypxx(String id) throws Exception;
	
}
