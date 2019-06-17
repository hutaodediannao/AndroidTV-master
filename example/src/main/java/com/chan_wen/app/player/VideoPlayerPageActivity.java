
package com.chan_wen.app.player;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.utils.ConvertUtils;
import com.blankj.utilcode.utils.Utils;
import com.chan_wen.app.MPlayerUtil.IMPlayListener;
import com.chan_wen.app.MPlayerUtil.IMPlayer;
import com.chan_wen.app.MPlayerUtil.MPlayer;
import com.chan_wen.app.MPlayerUtil.MPlayerException;
import com.chan_wen.app.MPlayerUtil.MinimalDisplay;
import com.chan_wen.app.MPlayerUtil.StringUtils;
import com.chan_wen.app.R;
import com.chan_wen.app.entity.Movie;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class VideoPlayerPageActivity extends Activity implements SeekBar.OnSeekBarChangeListener {

    public static final String MOVIE = "movieParmas";
    public static final String VOICE_KEY = "voiceKeyParmas";
    private SurfaceView mPlayerView;
    private MPlayer player;
    private SeekBar seekBar;
    private Timer timer;
    private TimerTask timerTask;
    private String url;
    private ImageButton btn_play_pause;
    private TextView tv_time_total, tv_time_current, tv_file_name, tvMoveTime;
    private static final int HIDE = 1, SHOW = 2, FINISH = 3, ENABLE_SEEK = 4, ENABLE_PLAY = 5;
    int timeout = 1000;//超时时间，毫秒
    boolean flag_enable_seek = false;//是否允许快进，默认打开播放器后5s内不允许快进

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            long pos;
            switch (msg.what) {
                case HIDE:
                    hideSeekbar();
                    break;
                case SHOW:
                    showSeekbar();
                    break;
                case FINISH:
                    finish();
                    break;
                case ENABLE_SEEK:
                    flag_enable_seek = true;
                    break;
                case ENABLE_PLAY:
                    try {
                        player.play();
                    } catch (MPlayerException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    };


    private RelativeLayout llSeekbar;
    private int videoDuration;
    private LoadingDialog playerLoadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myplayer2);
        initView();
        initData();
        initPlayer();
        String voiceKey = getIntent().getStringExtra(VOICE_KEY);
        if (com.blankj.utilcode.utils.StringUtils.isEmpty(voiceKey)) {
            //1、标识非语音唤醒
            url = getIntent().getStringExtra(MOVIE);
            starPlayUrl(url);
        } else {
            //2、语音唤醒
            queryNetData(voiceKey);
        }
    }

    /**
     * 请求视频数据源
     */
    private void queryNetData(final String voiceKey) {
        BmobQuery<Movie> query = new BmobQuery<>();
        query.setLimit(500);
        query.order("createdAt");
        //v3.5.0版本提供`findObjectsByTable`方法查询自定义表名的数据
        query.findObjects(new FindListener<Movie>() {
            @Override
            public void done(List<Movie> list, BmobException e) {
                if (list != null) {
                    Movie targetMv = null;
                    for (Movie mv : list) {
                        if (!com.blankj.utilcode.utils.StringUtils.isEmpty(mv.getTitle()) && mv.getTitle().contains(voiceKey)) {
                            targetMv = mv;
                            break;
                        }
                    }

                    if (targetMv != null) {
                        url = targetMv.getVideoFile().getFileUrl();
                        starPlayUrl(url);
                        return;
                    } else {
                        finish();
                    }
                } else {
                    finish();
                }
            }
        });
    }

    private void initView() {
        seekBar = findViewById(R.id.seekBar);
        mPlayerView = findViewById(R.id.mPlayerView);
        btn_play_pause = findViewById(R.id.btn_play_pause);
        tv_time_total = findViewById(R.id.tv_time_total);
        tv_time_current = findViewById(R.id.tv_time_current);
        llSeekbar = findViewById(R.id.llSeekbar);
        tv_file_name = findViewById(R.id.tv_file_name);
        tvMoveTime = findViewById(R.id.tvMoveTime);
        seekBar.setOnSeekBarChangeListener(this);
        playerLoadingDialog = new LoadingDialog(this)
                .setSuccessText("加载成功")//显示加载成功时的文字
                .setFailedText("加载失败")
                .setLoadingText("正在缓冲...");//设置loading时显示的文字
        playerLoadingDialog.show();
    }

    private void initData() {
        Utils.init(VideoPlayerPageActivity.this);
    }

    private void initPlayer() {
        player = new MPlayer();
        player.setDisplay(new MinimalDisplay(mPlayerView));
        playerOnPrepared();
        playerSurfaceDestroyed();
        playerOnBufferUpdateListen();
        seedCompletedListen();
    }

    private void playerOnBufferUpdateListen() {
        player.setImPlayerBufferUpdate(new MPlayer.IMPlayerBufferUpdate() {
            @Override
            public void onBufferUpdate(final int percent) {
                VideoPlayerPageActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (videoDuration > 0)
                            seekBar.setSecondaryProgress((int) ((percent / 100.0) * videoDuration));
                    }
                });
            }
        });
    }

    private void playerSurfaceDestroyed() {
        player.setiSurfaceDestroyed(new MPlayer.IMPlayerSurfaceDestroyed() {
            @Override
            public void onSurfaceDestroyed() {
                mHandler.removeMessages(HIDE);
                mHandler.removeMessages(SHOW);//快进到最后，特别处理
                mHandler.removeMessages(FINISH);
                mHandler.removeMessages(ENABLE_SEEK);
                mHandler.removeMessages(ENABLE_PLAY);
                if (timer != null) timer.cancel();
                if (timerTask != null) timerTask.cancel();
            }
        });
    }

    private void seedCompletedListen() {
        player.setiSeekCompleted(new MPlayer.ISeekCompleted() {
            @Override
            public void onSeekCompleted() {
                mHandler.removeMessages(HIDE);
                mHandler.sendMessageDelayed(mHandler.obtainMessage(HIDE), timeout);
            }
        });
    }

    private void playerSetListen() {
        player.setPlayListener(new IMPlayListener() {
            @Override
            public void onStart(IMPlayer player) {

            }

            @Override
            public void onPause(IMPlayer player) {
            }

            @Override
            public void onResume(IMPlayer player) {
            }

            @Override
            public void onComplete(IMPlayer player) {
                mHandler.sendMessageDelayed(mHandler.obtainMessage(FINISH), 0);
            }
        });
    }

    private void hideSeekbar() {
        if (player.isPlaying()) {
            llSeekbar.setVisibility(View.GONE);
            btn_play_pause.setVisibility(View.GONE);
            tv_file_name.setVisibility(View.GONE);
        }
    }

    private void showSeekbar() {
        llSeekbar.setVisibility(View.VISIBLE);
        tv_file_name.setVisibility(View.VISIBLE);
        if (!player.isPlaying()) {
            btn_play_pause.setVisibility(View.VISIBLE);
        }
    }

    private void starPlayUrl(String mUrl) {
        if (mUrl.length() > 0) {
            try {
                player.setSource(mUrl);
                player.play();

            } catch (MPlayerException e) {
                e.printStackTrace();
            }
        }
    }

    private void playerOnPrepared() {
        player.setOnPrepared(new MPlayer.IMPlayerPrepared() {
            @Override
            public void onMPlayerPrepare() {
                playerLoadingDialog.close();
                mHandler.sendMessageDelayed(mHandler.obtainMessage(ENABLE_SEEK), timeout);//播放器准备好后开始计时5秒内不允许快进
                playerSetListen();
                videoDuration = player.getDuration();
                seekBar.setMax(videoDuration);
                seekBar.setProgress(player.getCurrentPosition());
                String time = StringUtils.generateTime(videoDuration);
                tv_time_total.setText(" / " + time);
                tv_time_current.setText("00:00");
                mHandler.sendMessageDelayed(mHandler.obtainMessage(HIDE), timeout);
                timer = new Timer();
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        final int currentPosition = player.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                        VideoPlayerPageActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String time = StringUtils.generateTime(currentPosition);
                                tv_time_current.setText(time);
                                tvMoveTime.setText(time);
                            }
                        });
                    }
                };
                timer.schedule(timerTask, 0, 100);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        player.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.onDestroy();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int seekbarRealWidth = seekBar.getWidth() - 2 * ConvertUtils.dp2px(15);//15在xml中写死了
        float average = (float) (seekbarRealWidth * 1.0 / seekBar.getMax());
        int left = (int) (progress * average) + tvMoveTime.getWidth() / 4;
        //当前播放时间的位置跟随seekbar
        tvMoveTime.setX(left);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (flag_enable_seek) player.seekto(seekBar.getProgress());
        String time = StringUtils.generateTime(player.getCurrentPosition());
        tv_time_current.setText(time);
        tvMoveTime.setText(time);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(HIDE), 0);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(HIDE), timeout);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        mHandler.sendMessageDelayed(mHandler.obtainMessage(SHOW), 0);
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                if (player.isPlaying()) {
                    player.pause();
                    btn_play_pause.setImageResource(R.drawable.mediacontroller_pause);
                    btn_play_pause.setVisibility(View.VISIBLE);
                } else {
                    try {
                        player.play();
                        btn_play_pause.setImageResource(R.drawable.mediacontroller_play);
                        mHandler.removeMessages(HIDE);
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(HIDE), 0);
                    } catch (MPlayerException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case KeyEvent.KEYCODE_SHIFT_RIGHT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (flag_enable_seek) player.seekto(player.getCurrentPosition() + 5000);
                break;
            case KeyEvent.KEYCODE_SHIFT_LEFT:
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (flag_enable_seek) player.seekto(player.getCurrentPosition() - 5000);
                break;
            case KeyEvent.KEYCODE_BACK:
                finish();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

}
