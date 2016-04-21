package com.zombie.utils.file;

import com.zombie.utils.base.RandomUtils;
import com.zombie.utils.date.DateUtils;

import java.io.File;

/**
 * 文件工具类
 */
public class FileUtils {
    /**
     * 判断指定路径是否存在，如果不存在，根据参数决定是否新建
     *
     * @param filePath 指定的文件路径
     * @param isNew    true：新建、false：不新建
     *
     * @return 存在返回TRUE，不存在返回FALSE
     */
    public static boolean isExist(String filePath, boolean isNew) {
        File file = new File(filePath);
        if (!file.exists() && isNew) {
            return file.mkdirs();    //新建文件路径
        }
        return false;
    }

    /**
     * 获取文件名，构建结构为 prefix + yyyyMMddHH24mmss + 10位随机数 + suffix + .type
     *
     * @param type   文件类型
     * @param prefix 前缀
     * @param suffix 后缀
     *
     * @return 文件名
     */
    public static String getFileName(String type, String prefix, String suffix) {
        String date = DateUtils.getCurrentTime("yyyyMMddHH24mmss");   //当前时间
        String random = RandomUtils.generateNumberString(10);   //10位随机数

        //返回文件名
        return prefix + date + random + suffix + "." + type;
    }

    /**
     * 获取文件名，文件名构成:当前时间 + 10位随机数 + .type
     *
     * @param type 文件类型
     *
     * @return 文件名
     */
    public static String getFileName(String type) {
        return getFileName(type, "", "");
    }

    /**
     * 获取文件名，文件构成：当前时间 + 10位随机数
     *
     * @return 文件名
     */
    public static String getFileName() {
        String date = DateUtils.getCurrentTime("yyyyMMddHH24mmss");   //当前时间
        String random = RandomUtils.generateNumberString(10);   //10位随机数

        //返回文件名
        return date + random;
    }
}
