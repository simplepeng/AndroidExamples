package demo.simple.example_ffmpeg;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private void toast(final String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvVersion = findViewById(R.id.tvVersion);
        String version = String.format("ffmpeg version == %s", getVersion());
        tvVersion.setText(version);

        Log.d(TAG, version);

        findViewById(R.id.btnGetCover).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reqPermission(new OnGetPermissionListener() {
                    @Override
                    public void onGranted() {
                        getBitmap();
                    }
                });
            }
        });

        findViewById(R.id.btnExeCmd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reqPermission(new OnGetPermissionListener() {
                    @Override
                    public void onGranted() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                exeCmd();
                            }
                        }).start();
                    }
                });
            }
        });
    }

    interface OnGetPermissionListener {
        void onGranted();
    }

    private void reqPermission(final OnGetPermissionListener listener) {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.READ_EXTERNAL_STORAGE,
                        Permission.WRITE_EXTERNAL_STORAGE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        listener.onGranted();
                    }
                }).start();
    }

    private void getBitmap() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                + "get_cover1.mp4";
//        String path = "http://vfx.mtime.cn/Video/2019/03/09/mp4/190309153658147087.mp4";
//        if (!new File(path).exists()) {
//            toast("path不存在");
//            return;
//        }
//        Bitmap bitmap = Bitmap.createBitmap(getCover(path), 1080, 1920, Bitmap.Config.RGB_565);
        Bitmap bitmap = getCover(path);
        if (bitmap == null) {
            toast("native bitmap result == null");
            return;
        }

        Log.d(TAG, "bitmap width == " + bitmap.getWidth());
        Log.d(TAG, "bitmap height == " + bitmap.getHeight());
        Log.d(TAG, "bitmap config == " + bitmap.getConfig().name());
        Log.d(TAG, "bitmap byteCount == " + bitmap.getByteCount());

        ImageView ivCover = findViewById(R.id.ivCover);
        ivCover.setImageBitmap(bitmap);
    }

    //https://juejin.im/post/5b9613265188255c6375ea55
    private void exeCmd() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                + "get_cover1.mp4";
        String outPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                + "video.flv";
        File outFile = new File(outPath);
        if (outFile.exists()) {
            outFile.delete();
        }
        //裁剪个10s视频
        String cmd = "ffmpeg -ss 00:00:00 -t 00:00:1 -i " + path + " -vcodec copy -acodec copy " + outPath;
        String[] cmdArr = cmd.split(" ");
        int result = exeCmd(cmdArr);
        Log.d(TAG, "exe cmd result == " + result);
    }

    static {
        System.loadLibrary("utils");
        System.loadLibrary("avcodec");
        System.loadLibrary("avdevice");
        System.loadLibrary("avfilter");
        System.loadLibrary("avformat");
        System.loadLibrary("avutil");
        System.loadLibrary("swresample");
        System.loadLibrary("swscale");
        System.loadLibrary("avresample");
        System.loadLibrary("yuv");
    }

    public static native String getVersion();

    public static native Bitmap getCover(String path);

    public static native int exeCmd(String[] cmd);
}
