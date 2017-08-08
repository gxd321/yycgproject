package yycg.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import yycg.base.dao.mapper.DictinfoMapper;
import yycg.base.pojo.po.Dictinfo;
import yycg.base.pojo.po.DictinfoExample;
import yycg.base.service.SystemConfigService;

public class SystemConfigServiceImpl implements SystemConfigService{
	@Autowired
	DictinfoMapper dictinfoMapper;
	
	@Override
	public List<Dictinfo> findDictinfoByType(String typecode) throws Exception {
		DictinfoExample example = new DictinfoExample();
		example.createCriteria().andTypecodeEqualTo(typecode);
		example.setOrderByClause("dictsort");
	    return dictinfoMapper.selectByExample(example);
	}

}
