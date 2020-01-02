package edu.bjtu.android.util;

public class Utils {

    /**
     * 生成6位随机验证码
     * @return code
     */
    public static String generateEmailCode(){
        String str = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder code = new StringBuilder();
        for(int i = 0;i < 6;i++){
            int index = (int) (Math.random() * str.length());
            code.append(str.charAt(index));
        }
        return code.toString();
    }


}
