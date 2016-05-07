package com.junwen.videoplayer.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.junwen.videoplayer.DB.dao.DaoMaster;
import com.junwen.videoplayer.DB.dao.DaoSession;
import com.junwen.videoplayer.DB.dao.NetWorkDao;
import com.junwen.videoplayer.DB.entity.NetWorkEntity;
import com.junwen.videoplayer.DB.entity.VideoServerEntity;
import com.junwen.videoplayer.config.DbConstant;
import com.junwen.videoplayer.utils.L;

import java.util.List;

/**
 * 描述 : 数据库操作类
 * 作者 : 卜俊文
 * 日期 : 2016/3/6 20:28
 * 邮箱：344176791@qq.com
 */
public class DBInterface {
    private static DBInterface mDbInterface;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;

    /**
     * 描述 : 单例模式
     * 作者 : 卜俊文
     * 日期 : 2016/3/6 20:30
     * 邮箱：344176791@qq.com
     */
    public static DBInterface getInstance() {
        if (mDbInterface == null) {
            synchronized (DBInterface.class) {
                if (mDbInterface == null) {
                    mDbInterface = new DBInterface();
                }
            }
        }
        return mDbInterface;
    }

    /**
     * 上下文环境的更新
     * 1. 环境变量的clear
     * check
     */
    public void close() {
        if (openHelper != null) {
            openHelper.close();
            openHelper = null;
            context = null;
        }
    }

    /**
     * 创建数据库
     *
     * @param context
     */
    public void createDbHelp(Context context) {
        this.context = context;
        close();
        String DBName = DbConstant.DATABASE_NAME;
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DBName, null);
        this.openHelper = helper;
    }

    /**
     * 描述 : 打开可读Db
     * 作者 : 卜俊文
     * 日期 : 2016/3/6 20:33
     * 邮箱：344176791@qq.com
     */
    private DaoSession openReadableDb() {
        isInitOk();
        SQLiteDatabase db = openHelper.getReadableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        return daoSession;
    }

    /**
     * 描述 : 打开可写可读
     * 作者 : 卜俊文
     * 日期 : 2016/3/6 20:34
     * 邮箱：344176791@qq.com
     */
    private DaoSession openWritableDb() {
        isInitOk();
        SQLiteDatabase db = openHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        return daoSession;
    }

    /**
     * 描述 : 检查状态
     * 作者 : 卜俊文
     * 日期 : 2016/3/6 20:34
     * 邮箱：344176791@qq.com
     */
    private void isInitOk() {
        if (openHelper == null) {
            L.i("openHelper == null ");
            // 抛出异常 todo
            throw new RuntimeException("DBInterface#isInit not success or start,cause by openHelper is null");
        }
    }

    /**
     * 处理网络存储              *
     */

    /**
     * 描述 : 存储视频数据集合
     * 作者 : 卜俊文
     * 日期 : 2016/3/20 14:42
     * 邮箱：344176791@qq.com
     */
    public void saveNetworkList(List<NetWorkEntity> data) {
        openWritableDb().getNetWorkDao().insertOrReplaceInTx(data);
    }

    /**
     * 描述 : 获取所有视频根据类型
     * 作者 : 卜俊文
     * 日期 : 2016/3/20 15:28
     * 邮箱：344176791@qq.com
     */
    public List<NetWorkEntity> getNetworkList(String videoType) {
        return openWritableDb().getNetWorkDao().queryBuilder().where(NetWorkDao.Properties.VideoType.eq(videoType)).list();
    }

    /**
     * 描述:获取所有的视频集合
     * 作者:卜俊文
     * 邮箱:344176791@qq.com
     * 日期:
     */
    public List<VideoServerEntity> getVideoList() {
        return openWritableDb().getVideoServerEntityDao().queryBuilder().list();
    }

    public void saveVideoServet(VideoServerEntity videoServerEntity) {
        openWritableDb().getVideoServerEntityDao().insertOrReplace(videoServerEntity);
    }
}
