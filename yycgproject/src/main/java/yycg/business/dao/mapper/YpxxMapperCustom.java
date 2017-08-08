package yycg.business.dao.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import yycg.base.pojo.vo.SysuserCustom;
import yycg.base.pojo.vo.SysuserQueryVo;
import yycg.business.pojo.po.Ypxx;
import yycg.business.pojo.po.YpxxExample;
import yycg.business.pojo.vo.YpxxCustom;
import yycg.business.pojo.vo.YpxxQueryVo;

public interface YpxxMapperCustom {
	/**
	 * 药品目录查询
	 */
	public List<YpxxCustom> findList(YpxxQueryVo ypxxQueryVo) throws Exception;
	
	
	/**
	 * 查询总数
	 */
	
	public  int findYpxxCount(YpxxQueryVo ypxxQueryVo) throws Exception;
	
}