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
		field : 'userid',//对应json中的key
		title : '账号',
		width : 120
	}, {
		field : 'username',//对应json中的key
		title : '名称 ',
		width : 180
	}, {
		field : 'groupname',//对应json中的key
		title : '用户类型',
		width : 120,

	}, {
		field : 'sysmc',//对应json中的key
		title : '所属单位',
		width : 120
	}, {
		field : 'statename',//对应json中的key
		title : '状态',
		width : 120,
	},{
		field : 'opt1',//对应json中的key
		title : '删除',
		width : 120,
		formatter : function(value, row, index) {//通过此方法格式化显示内容,value表示从json中取出该单元格的值，row表示这一行的数据，是一个对象,index:行的序号
			return "<a href=javascript:deleteuser('"+row.id+"')>删除</a>";
		}
	},{
		field : 'opt2',//对应json中的key
		title : '修改',
		width : 120,
		formatter : function(value, row, index) {//通过此方法格式化显示内容,value表示从json中取出该单元格的值，row表示这一行的数据，是一个对象,index:行的序号
			return "<a href=javascript:edituser('"+row.id+"')>修改</a>";
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
			createmodalwindow("添加用户信息", 800, 250, '${baseurl}user/addsysuser.action');
		}
	} ];

	//加载datagrid

	$(function() {
		$('#sysuserlist').datagrid({
			title : '用户查询',//数据列表标题
			nowrap : true,//单元格中的数据不换行，如果为true表示不换行，不换行情况下数据加载性能高，如果为false就是换行，换行数据加载性能不高
			striped : true,//条纹显示效果
			url : '${baseurl}user/queryuser_result.action',//加载数据的连接，引连接请求过来是json数据
			idField : 'id',//此字段很重要，数据结果集的唯一约束(重要)，如果写错影响 获取当前选中行的方法执行
			loadMsg : '',
			columns : columns_v,
			pagination : true,//是否显示分页
			rownumbers : true,//是否显示行号
			pageList:[15,30,50],
			toolbar : toolbar_v
		});
	});
	
	//查询方法
	function queryuser(){
		//datagrid的方法load方法要求传入json数据，最终将 json转成key/value数据传入action
		//将form表单数据提取出来，组成一个json
		var formdata = $("#sysuserqueryForm").serializeJson();
		console.log(formdata);
		$('#sysuserlist').datagrid('load',formdata);
	}
	
	//删除方法
	function deleteuser(id){
		_confirm('您确定删除吗', null, function(){
			$("#delete_id").val(id);
			jquerySubByFId("sysuserdeleteform", userdel_callback, null,"json");
		});
	}
	
	
	function edituser(id){
		createmodalwindow("修改用户信息", 800, 250, '${baseurl}user/edituser.action?id='+id);
	}
	
	
	//删除方法回调
	function userdel_callback(data){
		console.log(data);
		message_alert(data);
		if(data.resultInfo.type==1){
			queryuser();
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
				<TD class="left">用户账号：</td>
				<td><INPUT type="text" name="sysuserCustom.userid" /></TD>
				<TD class="left">用户名称：</TD>
				<td><INPUT type="text" name="sysuserCustom.username" /></TD>

				<TD class="left">单位名称：</TD>
				<td><INPUT type="text" name="sysuserCustom.sysmc" /></TD>
				<TD class="left">用户类型：</TD>
				<td><select name="sysuserCustom.groupid">
					<!-- 	<option value="">请选择</option>
						<option value="1">卫生局</option>
						<option value="2">卫生院</option>
						<option value="3">卫生室</option>
						<option value="4">供货商</option>
						<option value="0">系统管理员</option> -->
					<c:forEach items="${Dictinfos}" var="dictinfo">
						<option value="${dictinfo.dictcode}">${dictinfo.info}</option>							
					</c:forEach>	
				</select></TD>
				
				<TD class="left">用户状态：</TD>
				<td>
					<select name="sysuserCustom.userstate">
						<c:forEach items="${dfs}" var ="df">
							<option value="${df.dictcode}">${df.info}</option>
						</c:forEach>
					</select>
				</td>
				
				<td><a id="btn" href="#" onclick="queryuser()"
					class="easyui-linkbutton" iconCls='icon-search'>查询</a></td>
			</TR>


		</TBODY>
	</TABLE>

	<!-- 查询列表 -->
	<TABLE border=0 cellSpacing=0 cellPadding=0 width="99%" align=center>
		<TBODY>
			<TR>
				<TD>
					<table id="sysuserlist"></table>
				</TD>
			</TR>
		</TBODY>
	</TABLE>
	
</form>

<form id="sysuserdeleteform" action="${baseurl}/user/deletesysuser.action">
		<input type="hidden" id="delete_id" name="id">
	</form>
</body>
</html>