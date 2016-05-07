package com.junwen.daogenerator;


import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoFile {
    private static String entityPath = "com.junwen.videoplayer.DB.entity";

    public static void main(String[] args) throws Exception {
        int dbVersion = 12;
        Schema schema = new Schema(dbVersion, "com.junwen.videoplayer.DB.dao");
        schema.enableKeepSectionsByDefault();
        // todo 绝对路径,根据自己的路径设定， 例子如下
        addVideo(schema);
        addVideoByServer(schema);
        String path = "/Users/junwen/Android/AndroidWork/VideoPlayer/app/src/main/java";
        new DaoGenerator().generateAll(schema, path);
    }

    /**
     * 描述:添加视频区实体
     * 作者:卜俊文
     * 邮箱:344176791@qq.com
     * 日期:
     */
    private static void addVideoByServer(Schema schema) {
        Entity video = schema.addEntity("VideoServerEntity");
        video.setTableName("VideoServerEntityInfo");
        video.setClassNameDao("VideoServerEntityDao");
        video.setJavaPackage(entityPath);
        video.addLongProperty("vid").primaryKey().autoincrement();
        video.addStringProperty("videoName"); //视频名称
        video.addStringProperty("videoUrl"); //视频url
        video.addStringProperty("thumbnail"); //视频缩略图
        video.setHasKeepSections(true);
    }

    /**
     * 描述:添加网络视频实体
     * 作者:卜俊文
     * 邮箱:344176791@qq.com
     * 日期:
     */
    public static void addVideo(Schema schema) {
        Entity videoInfo = schema.addEntity("NetWorkEntity");
        videoInfo.setTableName("NetWorkInfo");
        videoInfo.setClassNameDao("NetWorkDao");
        videoInfo.setJavaPackage(entityPath);
        videoInfo.addLongProperty("vid").primaryKey().autoincrement();
        videoInfo.addStringProperty("videoName"); //视频名称
        videoInfo.addStringProperty("videoUrl"); //视频url
        videoInfo.addStringProperty("videoType"); //视频类型
        videoInfo.addStringProperty("thumbnail"); //视频缩略图
        videoInfo.setHasKeepSections(true);
    }
}
