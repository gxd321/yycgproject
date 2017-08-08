package yycg.business.pojo.vo;

import yycg.base.pojo.vo.PageQuery;

public class YpxxQueryVo {
	
	//分页参数
    private PageQuery pageQuery;
	public PageQuery getPageQuery() {
		return pageQuery;
	}

	public void setPageQuery(PageQuery pageQuery) {
		this.pageQuery = pageQuery;
	}

	public YpxxCustom ypxxCustom;

	public YpxxCustom getYpxxCustom() {
		return ypxxCustom;
	}

	public void setYpxxCustom(YpxxCustom ypxxCustom) {
		this.ypxxCustom = ypxxCustom;
	}
	
}
