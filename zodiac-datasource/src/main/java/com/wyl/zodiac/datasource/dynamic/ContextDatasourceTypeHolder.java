package com.wyl.zodiac.datasource.dynamic;
/**   
* @Title: ContextDatasourceTypeHolder 
* @Package com.wyl.zodiac.datasource.dynamic
* @Description: 当前处理的数据源类型Holder
* @author
* @date 2017/8/8 14:40
* @version V1.0   
*/
public class ContextDatasourceTypeHolder {

    private static ThreadLocal<String> datasourceTypeHolder = new ThreadLocal<String>();

    /**
     * 甚至当前操作的数据源类型
     * @param dataSourceType
     * @author
     * @date 2017年08月08日14:42:59
     */
    public static void setDatasourceType(String dataSourceType) {
        datasourceTypeHolder.set(dataSourceType);
    }

    /**
     * 获取当前操作的数据源类型
     * @return
     * @author
     * @date 2017年08月08日14:43:15
     */
    public static String getDatasourceType() {
        return datasourceTypeHolder.get();
    }

    /**
     * 清除当前操作的数据源类型，使用默认的了
     * @author
     * @date 2017年08月08日14:43:38
     */
    public static void clearDatasourceType() {
        datasourceTypeHolder.remove();
    }

}
