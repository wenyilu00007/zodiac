package com.hoau.zodiac.core.util;

import com.hoau.zodiac.core.util.string.StringUtils;

/**
* @Title: WaybillVerifyUtils 
* @Package com.hoau.zodiac.core.util 
* @Description: 物流单号校验
* @author 陈宇霖  
* @date 2017/10/14 11:01
* @version V1.0   
*/
public class WaybillVerifyUtils {

    private static final int[] weights = {9,7,5,3,1,8,6,4,2};

    private static final int begin = 4;

    private static final int digit = 9;

    private static final int weightAvg = 9;

    private static final int verifyNum = 8;

    /**
     * 校验物流单是否合法
     * @param waybillNo
     * @return
     * @author 陈宇霖
     * @date 2017年10月14日11:25:38
     */
    public static boolean verifyWaybill(String waybillNo) {
        //物流单总长度25位
        if (StringUtils.isBlank(waybillNo) || waybillNo.length() != 25) {
            return false;
        }
        //校验位检查
        //流水号为第3-14位
        String flowNum = waybillNo.substring(2, 14);
        if (!StringUtils.isNumeric(flowNum)) {
            return false;
        }
        String serverVerifyFlag = verifyCodeGenerate(flowNum);
        //校验位为第15位
        String verifyFlag = waybillNo.substring(14, 15);
        if (!serverVerifyFlag.equals(verifyFlag)) {
            return false;
        }
        return true;
    }

    /**
     * 校验数字生成规则：
     * 1、将运单编号4-12位置的每个数字与相关的权重数字相乘；
     * 2、各项相乘结果加和；
     * 3、把总数除以9，获取余数；
     * 4、余数+8，所得之和的个位数为验证数字。
     * @param flowNum 12位流水号
     * @return
     */
    public static String verifyCodeGenerate(String flowNum){
        String verifyCode = null;
        int sum = 0 ;
        for(int i = 0 ; i < digit ; i++){
            sum = sum + Integer.parseInt(""+flowNum.charAt( i+ begin -1 )) * weights[i];
        }
        verifyCode = "" + (sum % weightAvg + verifyNum) % 10 ;
        return verifyCode;
    }


    public static void main(String[] args) {
        String trackNumber = "2012345678901210000000000";
        System.out.println(verifyWaybill(trackNumber));
    }
}
