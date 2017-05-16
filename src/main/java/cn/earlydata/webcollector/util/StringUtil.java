package cn.earlydata.webcollector.util;

import java.security.MessageDigest;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Yanjun on 2017/04/26.
 */
public class StringUtil {

    //十六进制下数字到字符的映射数组
    private final static String[] hexDigits = {"0", "1", "2", "3", "4",
            "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static String encryptByString(String content) {
        return createPassword(content);
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



    /** 把inputString加密 */
    public static String createPassword(String inputString) {
        return encodeByMD5(inputString);
    }

    /**
     * 验证输入的密码是否正确
     * @param password 真正的密码（加密后的真密码）
     * @param inputString 输入的字符串
     * @return 验证结果，boolean类型
     */
    public static boolean authenticatePassword(String password, String inputString) {
        if(password.equals(encodeByMD5(inputString))) {
            return true;
        } else {
            return false;
        }
    }

    /** 对字符串进行MD5加密 */
    private static String encodeByMD5(String originString) {
        if (originString != null) {
            try {
                //创建具有指定算法名称的信息摘要
                MessageDigest md = MessageDigest.getInstance("MD5");
                //使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
                byte[] results = md.digest(originString.getBytes());
                //将得到的字节数组变成字符串返回
                String resultString = byteArrayToHexString(results);
                return resultString.toUpperCase();
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 转换字节数组为十六进制字符串
     * @param b 字节数组
     * @return 十六进制字符串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    /**
     * 将一个字节转化成十六进制形式的字符串
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }
}
