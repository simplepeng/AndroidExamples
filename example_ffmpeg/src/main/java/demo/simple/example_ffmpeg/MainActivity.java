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
                reqPermission();
            }
        });
    }

    private void reqPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.READ_EXTERNAL_STORAGE,
                        Permission.WRITE_EXTERNAL_STORAGE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        getBitmap();
                    }
                }).start();
    }

    private void getBitmap() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                + "get_cover.mp4";
        if (!new File(path).exists()) {
            toast("path不存在");
            return;
        }
//        Bitmap bitmap = Bitmap.createBitmap(getCover(path), 1080, 1920, Bitmap.Config.RGB_565);
        Bitmap bitmap = getCover(path);
        if (bitmap == null) {
            toast("native bitmap result == null");
            return;
        }

        ImageView ivCover = findViewById(R.id.ivCover);
        ivCover.setImageBitmap(bitmap);
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
    }

    public static native String getVersion();

    public static native Bitmap getCover(String path);
}
