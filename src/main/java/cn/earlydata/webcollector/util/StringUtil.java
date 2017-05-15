package cn.earlydata.webcollector.util;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Yanjun on 2017/04/26.
 */
public class StringUtil {

    public static String encryptByString(String content) {
        return PasswordUtil.createPassword(content);
    }

    /**
     * 判断有货无货状态
     * @param str
     * @return
     */
    public static String getInventoryNumberInStr(String str){
        if(str.equals("Auf Lager.")){
            return "有货";
        }
        else if(str.startsWith("Nur noch")){
            return getNumberInStr(str);
        }else{
            return "暂时无货";
        }
    }

    /**
     * 获取字符串中的数字
     * @param str
     * @return
     */
    public static String getNumberInStr(String str){
        if(StringUtil.notEmpty(str)){
            Pattern p = Pattern.compile("[^0-9]");
            Matcher m = p.matcher(str);
            String result = m.replaceAll("");
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < result.length(); i++) {
                stringBuilder.append(result.substring(i, i+1));
            }
            return stringBuilder.toString();
        }
        return "0";

    }


    /**
     * 检测字符串是否不为空
     * @param s
     * @return 不为空则返回true，否则返回false
     */
    public static boolean notEmpty(String s){
        return s!=null && !"".equals(s) && !"null".equals(s);
    }

    /**
     * 生成UUID
     * @return
     */
    public static String get32UUID() {
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        return uuid;
    }
}
