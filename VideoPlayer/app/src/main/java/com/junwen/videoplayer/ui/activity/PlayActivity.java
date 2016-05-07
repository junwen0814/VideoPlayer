package com.junwen.videoplayer.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.junwen.videoplayer.R;
import com.junwen.videoplayer.config.PlayConstant;
import com.junwen.videoplayer.imservice.manager.IMPlayManager;
import com.junwen.videoplayer.utils.DateUtils;
import com.junwen.videoplayer.utils.L;
import com.junwen.videoplayer.utils.ScreenUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zhy.autolayout.AutoRelativeLayout;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.VideoView;


/**
 * 描述 : 视频播放界面
 * 作者 : 卜俊文
 * 日期 : 2016/3/13 15:47
 * 邮箱：344176791@qq.com
 */
public class PlayActivity extends Activity implements MediaPlayer.OnPreparedListener, View.OnClickListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnInfoListener {

    @ViewInject(R.id.play_videoview)
    private VideoView mVideoView; //视频播放控件

    @ViewInject(R.id.play_action)
    private ImageView mAction; //开始活着暂停

    @ViewInject(R.id.play_seebar)
    private SeekBar mSeebar; //进度条

    @ViewInject(R.id.play_currtime)
    private TextView mCurrentTime; //当前时间

    @ViewInject(R.id.play_duration)
    private TextView mDuration; //当前视频的总时长

    @ViewInject(R.id.play_control_view)
    private AutoRelativeLayout rv_control; //控制界面

    @ViewInject(R.id.play_tv_progress)
    private TextView tv_playProgress; //快进进度文本

    @ViewInject(R.id.play_progress)
    private ProgressBar mProgress; //当前缓冲进度条

    @ViewInject(R.id.play_buff_progress_tv)
    private TextView tv_buff_progress; //当前缓冲进度

    @ViewInject(R.id.play_down_speed)
    private TextView tv_down_speed; //Donw Speeds

    @ViewInject(R.id.play_next)
    private ImageView play_next; //下一首

    private boolean isDestory = false; //是否已经关闭

    private boolean isShowControl = true; //是否已经显示控制条

    private GestureDetector mGestureDetector; //触摸事件

    private Vibrator vibrator; //震动

    private AudioManager audiomanager; //声音管理器

    private boolean isLight = false;  //当前调节的是否是亮度

    private int maxVolume; //当前最大值音量

    private int currentVolume; //当前音量

    private boolean isLocal; //是否播放本地视频

    private float y1;

    private float x1;

    private String urlPath; //视频URL

    private int distance;

    private android.os.Handler handler = new android.os.Handler();


    private Runnable progressRunable = new Runnable() {
        @Override
        public void run() {
            tv_playProgress.setVisibility(View.GONE);
        }
    };
    /**
     * 描述 : 执行关闭工具栏
     * 作者 : 卜俊文
     * 日期 : 2016/3/17 21:54
     * 邮箱：344176791@qq.com
     */
    private Runnable controlRunable = new Runnable() {
        @Override
        public void run() {
            //5秒后触发这里,先进行删除任务，避免多个任务的问题
            handler.removeCallbacksAndMessages(controlRunable);
            if (isShowControl) {
                //再次判断，如果现在是显示状态，再关闭
                updateControl(isShowControl);
            }
        }
    };
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (state != 1) {
                int currentPosition = (int) mVideoView.getCurrentPosition();
                mCurrentTime.setText(DateUtils.getInstance().stringForTime(currentPosition));
                int duration = (int) mVideoView.getDuration();
                mDuration.setText(DateUtils.getInstance().stringForTime(duration));
                L.i("runnable");
                mSeebar.setProgress(currentPosition);
                if (!isDestory) {
                    //间隔重新调用次Runable刷新UI
                    handler.postDelayed(runnable, PlayConstant.PLAY_REFER_TIME);
                }
                updateActionState();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        if (!LibsChecker.checkVitamioLibs(this))
            return;
        ViewUtils.inject(this);
        init();
        initData();
        initListener();
    }

    /**
     * 描述 : 初始化
     * 作者 : 卜俊文
     * 日期 : 2016/3/15 21:10
     * 邮箱：344176791@qq.com
     */
    private void init() {
        //设置视频播放全屏
//        IMPlayManager.getInstance().toFullScreen(mVideoView);
        mGestureDetector = new GestureDetector(this, new MyGestureListener());
        audiomanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    }


    /**
     * 描述 : 初始化数据
     * 作者 : 卜俊文
     * 日期 : 2016/3/13 15:56
     * 邮箱：344176791@qq.com
     */
    private void initData() {
        String str_intent = getIntent().getStringExtra(PlayConstant.VIDEO_TYPE_TAG);
        if (!TextUtils.isEmpty(str_intent)) {
            //获取URL地址
            urlPath = getIntent().getStringExtra(PlayConstant.VIDEO_URL);
            //判断是网络还是本地视频
            if (str_intent.equals(PlayConstant.VIDEO_TYPE_LOCAL)) {
                //处理本地视频处理
                L.i("本地视频" + urlPath);
                mVideoView.setVideoPath(urlPath);
                isLocal = true;
            } else {
                //处理网络视频配置
                L.i("网络视频");
                mVideoView.setVideoURI(Uri.parse(urlPath));
                isLocal = false;
            }
        } else {
            //如果为空，判读是否是别的视频用我的视频播放了
            Uri data = getIntent().getData();
            if (data != null) {
                if (!TextUtils.isEmpty(data.toString())) {
                    mVideoView.setVideoURI(Uri.parse(data.toString()));
                    isLocal = false;
                }
            } else {
                Toast.makeText(PlayActivity.this, "没有播放地址，无法播放!", Toast.LENGTH_SHORT).show();
            }
        }
        //获取当前最大音量和当前音量
        maxVolume = audiomanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);// 获取系统最大音量
        currentVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC);// 获取当前值
        distance = ScreenUtils.getScreenHeight(PlayActivity.this) / 15;
    }

    /**
     * 描述 : 初始化监听
     * 作者 : 卜俊文
     * 日期 : 2016/3/13 16:12
     * 邮箱：344176791@qq.com
     */
    private void initListener() {
        mVideoView.setOnPreparedListener(this);
        mAction.setOnClickListener(this);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnErrorListener(this);
        mSeebar.setOnSeekBarChangeListener(this);
        mVideoView.setOnBufferingUpdateListener(this);
        mVideoView.setOnInfoListener(this);
        play_next.setOnClickListener(this);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        L.i("onPrepared");
        mVideoView.start();
        //设置一下播放按钮的图片
        updateActionState();
        long duration = mVideoView.getDuration();
        mSeebar.setMax((int) duration);
        handler.post(runnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView != null) {
            mVideoView.pause();
        }
        isDestory = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        L.i("onStop");
        isDestory = true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mVideoView != null) {
            isDestory = false;
            handler.post(runnable);
            updateActionState();
        }
    }

    private int state;

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Log.d("PlayActivity", "onBufferingUpdate当前进度:" + mVideoView.getBufferPercentage());
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                //开始缓存，暂停播放
                onBuffStart();
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                //缓存完成，继续播放
                onBuffSuccess();
                break;
            case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                //显示 下载速度
                onBuffing(extra);
                Log.d("PlayActivity", "下载extra:" + extra);
                break;
        }
        return true;
    }

    /**
     * 描述:当缓冲开始时
     * 作者:卜俊文
     * 邮箱:344176791@qq.com
     * 日期:
     */
    private void onBuffStart() {
        if (mVideoView.isPlaying()) {
            mVideoView.pause();
        }
        mProgress.setVisibility(View.VISIBLE);
        if (!isLocal) {
            tv_down_speed.setVisibility(View.VISIBLE);
            tv_buff_progress.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 描述:当正在缓冲时
     * 作者:卜俊文
     * 邮箱:344176791@qq.com
     * 日期:
     */
    private void onBuffing(int extra) {
        tv_down_speed.setText("下载速度:" + String.valueOf(extra) + " KB");
        tv_buff_progress.setText("进度:" + String.valueOf(mVideoView.getBufferPercentage()) + " %");
    }

    /**
     * 描述:当缓冲完成时,更新UI
     * 作者:卜俊文
     * 邮箱:344176791@qq.com
     * 日期:
     */
    private void onBuffSuccess() {
        mVideoView.start();
        mProgress.setVisibility(View.INVISIBLE);
        tv_down_speed.setVisibility(View.INVISIBLE);
        tv_buff_progress.setVisibility(View.INVISIBLE);
        handler.postDelayed(controlRunable, PlayConstant.PLAY_CONTROL_TIMEOUT);
    }

    /**
     * 描述 : 屏幕触摸监听
     * 作者 : 卜俊文
     * 日期 : 2016/3/19 18:53
     * 邮箱：344176791@qq.com
     */
    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        public boolean onDown(MotionEvent e) {
            L.i("onDown");
            state = 0;
            //按下的时候，记录当前要调节的是音量还是亮度
//            isLight = IMPlayManager.getInstance().getScreenByX(e.getX(), PlayActivity.this);
            y1 = e.getY(); //记录滑动第一次的Y坐标，便于之后滑动的距离判断
            x1 = e.getX();
            return false;
        }

        public void onLongPress(MotionEvent e) {
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY) {
            return true;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            float y2 = e2.getY();
            float x2 = e2.getX();
            currentVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (state == 0 || state == 1) {
                handler.removeCallbacks(runnable);
                //如果是未确定状态，则进入左右滑动
                //处理左右滑动
                if (x2 - x1 > 8f) {
                    //往右滑动
                    state = 1;
                    if (x2 - x1 > distance) {
                        handler.removeCallbacks(progressRunable);
//                        mVideoView.seekTo(mVideoView.getCurrentPosition() + 5000);
                        L.i("向右1" + mSeebar.getProgress());
                        mSeebar.setProgress(mSeebar.getProgress() + 5000);
                        L.i("向右2" + mSeebar.getProgress());
                        tv_playProgress.setVisibility(View.VISIBLE);
                        tv_playProgress.setText(DateUtils.getInstance().stringForTime(mSeebar.getProgress()));
                        handler.postDelayed(progressRunable, 2000);
                        x1 = x2;
                    }
                } else if (x1 - x2 > 8f) {
                    //往左滑动
                    state = 1;
                    if (x1 - x2 > distance) {
                        handler.removeCallbacks(progressRunable);
//                        mVideoView.seekTo(mVideoView.getCurrentPosition() - 5000);
                        L.i("向左");
                        mSeebar.setProgress(mSeebar.getProgress() - 5000);
                        tv_playProgress.setVisibility(View.VISIBLE);
                        tv_playProgress.setText(DateUtils.getInstance().stringForTime(mSeebar.getProgress()));
                        handler.postDelayed(progressRunable, 2000);
                        x1 = x2;
                    }
                }
            }
            if (state == 0 || state == 2 || state == 3) {
                L.i("处理上下");
                //处理上下滑动
                boolean screenByX = IMPlayManager.getInstance().getScreenByX(x2, PlayActivity.this);
                if (screenByX) {
                    state = 2;
                } else {
                    state = 3;
                }
                //如果是调节音量
                if (y1 > y2) {
                    //如果滑动的Y坐标小于第一次按下的Y坐标，肯定是往上滑动
                    if (y1 - y2 > distance) {
                        //第一次的Y坐标-去当前的Y坐标，如果大于间隔距离，则让音量+1，这样给一个间距
                        //给一个约束，不至于一下子加很多音量
                        if (state == 2) {
                            IMPlayManager.getInstance().adjustLight(PlayActivity.this, 0.1f);
                        } else if (state == 3) {
                            currentVolume++;
                            IMPlayManager.getInstance().adjustVolume(currentVolume, PlayActivity.this);
                        }
                        //加上音量后，把当前的Y坐标赋值给当前Y1，便于下次再次判断
                        y1 = y2;
                    }
                } else if (y2 > y1) {
                    //往下滑动
                    if (y2 - y1 > distance) {
                        if (state == 2) {
                            IMPlayManager.getInstance().adjustLight(PlayActivity.this, -0.1f);
                        } else if (state == 3) {
                            currentVolume--;
                            IMPlayManager.getInstance().adjustVolume(currentVolume, PlayActivity.this);
                        }
                        y1 = y2;
                    }
                }
            }
            return true;
        }

        public void onShowPress(MotionEvent e) {
            // TODO Auto-generated method stub
        }
    }

    /**
     * 描述 : 视频播放监听
     * 作者 : 卜俊文
     * 日期 : 2016/3/15 21:02
     * 邮箱：344176791@qq.com
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_action:
                if (mVideoView.isPlaying()) {
                    onPauseVideo();
                } else {
                    tv_down_speed.setVisibility(View.GONE);
                    mVideoView.start();
                }
                //当点击开始或者暂停的时候，也停止计时
                removeAndStart();
                updateActionState();
                break;
            case R.id.play_next:
                break;
        }

    }

    /**
     * 描述:当停止播放时
     * 作者:卜俊文
     * 邮箱:344176791@qq.com
     * 日期:
     */
    private void onPauseVideo() {
        mVideoView.pause();
        if (!isLocal) {
            tv_down_speed.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 描述 : 删除计时，并且重新计时
     * 作者 : 卜俊文
     * 日期 : 2016/3/19 14:52
     * 邮箱：344176791@qq.com
     */
    public void removeAndStart() {
        if (handler != null) {
            if (controlRunable != null) {
                handler.removeCallbacks(controlRunable);
            }
            handler.postDelayed(controlRunable, PlayConstant.PLAY_CONTROL_TIMEOUT);
        }
    }

    /**
     * 描述 : 更新按钮状态
     * 作者 : 卜俊文
     * 日期 : 2016/3/13 16:23
     * 邮箱：344176791@qq.com
     */
    public void updateActionState() {
        mAction.setImageBitmap(BitmapFactory.decodeResource(getResources(), IMPlayManager.getInstance().getActionImgByState(mVideoView.isPlaying())));
    }

    /**
     * 描述 : 当播放完毕的时候
     * 作者 : 卜俊文
     * 日期 : 2016/3/15 22:23
     * 邮箱：344176791@qq.com
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        finish();
    }

    /**
     * 描述 : 当播放错误的时候
     * 作者 : 卜俊文
     * 日期 : 2016/3/15 22:24
     * 邮箱：344176791@qq.com
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Toast.makeText(PlayActivity.this, "播放出错，请稍后重试!", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mVideoView.seekTo(progress);
            if (handler != null && controlRunable != null) {
                handler.removeCallbacks(controlRunable);
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (isShowControl) {
            handler.postDelayed(controlRunable, PlayConstant.PLAY_CONTROL_TIMEOUT);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = mGestureDetector.onTouchEvent(event);
        if (!result) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // getVideoInfosfromPath(filePath);
                if (state == 0) {
                    endGesture();
                }
            }
            result = super.onTouchEvent(event);
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            L.i("state:" + state);
            if (state == 1) {
                mVideoView.seekTo(mSeebar.getProgress());
                handler.postDelayed(runnable, 300);
            } else {
            }
            state = 0;
        }
        return result;
    }

    /**
     * 描述 : 停止
     * 作者 : 卜俊文
     * 日期 : 2016/3/19 16:02
     * 邮箱：344176791@qq.com
     */
    private void endGesture() {
        if (handler != null && controlRunable != null) {
            handler.removeCallbacks(controlRunable);
        }
        //更新控制栏
        updateControl(isShowControl);
        //当用户点击松开后，开始计时,5秒后进行关闭控制条
        if (isShowControl) {
            handler.postDelayed(controlRunable, PlayConstant.PLAY_CONTROL_TIMEOUT);
        }
    }

    /**
     * 描述 : 根据状态显示或者隐藏控制条
     * 作者 : 卜俊文
     * 日期 : 2016/3/17 21:34
     * 邮箱：344176791@qq.com
     */
    private void updateControl(boolean isShow) {
        Animation animation = null;
        if (isShow) {
            //如果当前正在显示，则启动隐藏动画
            animation = new TranslateAnimation(0, 0, 0, rv_control.getHeight());
        } else {
            animation = new TranslateAnimation(0, 0, rv_control.getHeight(), 0);
        }
//        if(isShow){
//            ViewHelper.setTranslationY(rv_control,rv_control.getHeight());
//        }else{
//            ViewHelper.setTranslationY(rv_control,-300);
//        }
        animation.setDuration(500);
        animation.setFillAfter(true);
        rv_control.startAnimation(animation);
        isShowControl = !isShow;
    }

}
