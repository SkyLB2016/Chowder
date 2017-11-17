package liuyongxiang.robert.com.testtime.wheelview;

import java.text.SimpleDateFormat;

public class JudgeDate {

	public static  boolean isDate(String str_input,String rDateFormat){
		if (str_input!=null) {
	         SimpleDateFormat formatter = new SimpleDateFormat(rDateFormat);
	         formatter.setLenient(false);
	         try {
	             formatter.format(formatter.parse(str_input));
	         } catch (Exception e) {
	             return false;
	         }
	         return true;
	     }
		return false;
	}
}