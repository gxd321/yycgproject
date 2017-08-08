<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/base/tag.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- 引用jquery easy ui的js库及css -->
<LINK rel="stylesheet" type="text/css"
	href="${baseurl}js/easyui/styles/default.css">
<%@ include file="/WEB-INF/jsp/base/common_css.jsp"%>
<%@ include file="/WEB-INF/jsp/base/common_js.jsp"%>
<title>用户管理</title>

<script type="text/javascript">
	//datagrid列定义
	var columns_v = [ [ {
		field : 'bm',//对应json中的key
		title : '流水号',
		width : 120
	}, {
		field : 'mc',//对应json中的key
		title : '通用名 ',
		width : 180
	}, {
		field : 'lbmc',//对应json中的key
		title : '药品类别',
		width : 120
	}, {
		field : 'jyztmc',//对应json中的key
		title : '交易状态',
		width : 120,
	},{
		field : 'opt2',//对应json中的key
		title : '修改',
		width : 120,
		formatter : function(value, row, index) {//通过此方法格式化显示内容,value表示从json中取出该单元格的值，row表示这一行的数据，是一个对象,index:行的序号
			return "<a href=javascript:edituser('"+row.id+"')>修改</a>";
		}
	},{
		field : 'opt1',//对应json中的key
		title : '删除',
		width : 120,
		formatter : function(value, row, index) {//通过此方法格式化显示内容,value表示从json中取出该单元格的值，row表示这一行的数据，是一个对象,index:行的序号
			return "<a href=javascript:deluser('"+row.id+"')>删除</a>";
		}
	} ] ];

	//定义 datagird工具
	var toolbar_v = [ {//工具栏
		id : 'btnadd',
		text : '添加',
		iconCls : 'icon-add',
		handler : function() {
			//打开一个窗口，用户添加页面
			//参数：窗口的title、宽、高、url地址
			createmodalwindow("添加药品信息", 800, 250, '${baseurl}ypml/addypxx.action');
		}
	},{
		id : 'btnexport',
		text : '导出',
		iconCls : 'icon-redo',
		handler : function() {
			$.ajax({
				type : "GET",
				url : "${baseurl}ypml/exportYpxxSubmit.action",
				data : $(".sysuserqueryForm").serialize(),
				dataType : "json",
				success : function(data) {
					console.log(data);
					message_alert(data);
				}
			})

		}
	} ];

	//加载datagrid

	$(function() {
		$('#ypxxlist').datagrid({
			title : '药品目录导出',//数据列表标题
			nowrap : true,//单元格中的数据不换行，如果为true表示不换行，不换行情况下数据加载性能高，如果为false就是换行，换行数据加载性能不高
			striped : true,//条纹显示效果
			url : '${baseurl}ypml/queryypxx_result.action',//加载数据的连接，引连接请求过来是json数据
			idField : 'id',//此字段很重要，数据结果集的唯一约束(重要)，如果写错影响 获取当前选中行的方法执行
			loadMsg : '',
			columns : columns_v,
			pagination : true,//是否显示分页
			rownumbers : true,//是否显示行号
			pageList : [ 15, 30, 50 ],
			toolbar : toolbar_v
		});
	});

	//查询方法
	function queryypxx() {
		//datagrid的方法load方法要求传入json数据，最终将 json转成key/value数据传入action
		//将form表单数据提取出来，组成一个json
		var formdata = $("#sysuserqueryForm").serializeJson();

		console.log(formdata);

		$('#ypxxlist').datagrid('load', formdata);
	}
	
	
	function edituser(id){
		createmodalwindow("修改用户信息", 800, 250, '${baseurl}ypml/editypxx.action?id='+id);
	}
	
	function deluser(id){
		_confirm('您确定删除吗', null, function(){
			$("#delete_id").val(id);
			jquerySubByFId("sysuserdeleteform", userdel_callback, null,"json");
		});
	}
	

	//删除方法回调
	function userdel_callback(data){
		message_alert(data);
		if(data.resultInfo.type==1){
			queryypxx();
		}
	}
	
</script>

</head>
<body>

	<!-- html的静态布局 -->
	<form id="sysuserqueryForm">
		<!-- 查询条件 -->

		<TABLE class="table_search">
			<TBODY>
				<TR>
					<TD class="left">流水号：</td>
					<td><INPUT type="text" name="ypxxCustom.bm" /></TD>
					<TD class="left">通用名：</TD>
					<td><INPUT type="text" name="ypxxCustom.mc" /></TD>

					<TD class="left">药品类别：</TD>
					<td><select name="ypxxCustom.lb">
							<option value="">全部</option>
							<c:forEach items="${yplblist}" var="value">
								<option value="${value.id}">${value.info}</option>
							</c:forEach>
					</select></TD>

					<TD class="left">交易状态：</TD>
					<td><select name="ypxxCustom.jyzt">
							<option value="">全部</option>
							<c:forEach items="${jyztlist}" var="value">
								<option value="${value.dictcode}">${value.info}</option>
							</c:forEach>
					</select></td>
					<td><a id="btn" href="#" onclick="queryypxx()"
						class="easyui-linkbutton" iconCls='icon-search'>查询</a></td>
				</TR>
			</TBODY>
		</TABLE>

		<!-- 查询列表 -->
		<TABLE border=0 cellSpacing=0 cellPadding=0 width="99%" align=center>
			<TBODY>
				<TR>
					<TD>
						<table id="ypxxlist"></table>
					</TD>
				</TR>
			</TBODY>
		</TABLE>

	</form>
	
	<form id="sysuserdeleteform" action="${baseurl}ypml/delypxx.action" method="post">
		<input type="hidden" id="delete_id" name="id">
	</form>
</body>
</html>