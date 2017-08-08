package yycg.business.service.impl;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

import yycg.base.dao.mapper.SysuserMapper;
import yycg.base.pojo.vo.SysuserQueryVo;
import yycg.base.process.context.Config;
import yycg.base.process.result.ExceptionResultInfo;
import yycg.base.process.result.ResultUtil;
import yycg.business.dao.mapper.YpxxMapper;
import yycg.business.dao.mapper.YpxxMapperCustom;
import yycg.business.pojo.po.Ypxx;
import yycg.business.pojo.vo.YpxxCustom;
import yycg.business.pojo.vo.YpxxQueryVo;
import yycg.business.service.YpxxService;
import yycg.util.UUIDBuild;

public class YpxxServiceImpl implements YpxxService{
	@Autowired
	private YpxxMapperCustom YpxxMapperCustom;
	
	
	@Autowired
	private YpxxMapper ypxxMapper;
	
	
	
	@Override
	public List<YpxxCustom> findList(YpxxQueryVo ypxxQueryVo) throws Exception {
		return YpxxMapperCustom.findList(ypxxQueryVo);
	}

	@Override
	public int findYpxxCount(YpxxQueryVo ypxxQueryVo) throws Exception {
		return YpxxMapperCustom.findYpxxCount(ypxxQueryVo);
	}

	@Override
	public void insertYpxx(YpxxCustom ypxxCustom) throws Exception {
		ypxxCustom.setId(UUIDBuild.getUUID());
		ypxxCustom.setScqymc("海口奇力制药股份有限公司");
		ypxxCustom.setSpmc("替硝唑");
		ypxxCustom.setZbjg(0.67f);
		ypxxCustom.setDw("盒");
		ypxxCustom.setJx("片剂");
		ypxxCustom.setGg("0.25"+Math.random());
		ypxxCustom.setZhxs("100");
		ypxxCustom.setPinyin("xsdm");
		ypxxMapper.insert(ypxxCustom);
	}

	@Override
	public Ypxx findById(String id) throws Exception {
		return  ypxxMapper.selectByPrimaryKey(id);
	}

	@Override
	public void updateYpxx(String id, YpxxCustom ypxxCustom) throws Exception {
		Ypxx ypxx = ypxxMapper.selectByPrimaryKey(id);
		ypxx.setMc(ypxxCustom.getMc());
		ypxx.setLb(ypxxCustom.getLb());
		ypxx.setJyzt(ypxxCustom.getJyzt());
		ypxxMapper.updateByPrimaryKeySelective(ypxx);
	}

	@Override
	public void deleteypxx(String id) throws Exception {
		Ypxx ypxx = ypxxMapper.selectByPrimaryKey(id);
		if(ypxx==null){
			throw new ExceptionResultInfo(ResultUtil.createFail(Config.MESSAGE, 212, null));
		}
		ypxxMapper.deleteByPrimaryKey(id);
		
	}
}
