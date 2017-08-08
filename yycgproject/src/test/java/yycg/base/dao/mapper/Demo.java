package yycg.base.dao.mapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Demo {
	 public static String replaceBlank(String str) {
	        String dest = "";
	        if (str!=null) {
	            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	         // 定义HTML标签的正则表达式
	            String regEx_html = "<[^>]+>";
	            // 定义一些特殊字符的正则表达式 如：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	            String regEx_special = "\\&[a-zA-Z]{1,10};";
	            Matcher m = p.matcher(str);
	            dest = m.replaceAll("");
	        }
	        return dest;
	    }
	    public static void main(String[] args) {
	        System.out.println(Demo.replaceBlank("just\ndo it!"));
	    }
}
