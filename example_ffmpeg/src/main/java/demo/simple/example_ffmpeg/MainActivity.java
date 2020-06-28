package demo.simple.example_ffmpeg;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvVersion = findViewById(R.id.tvVersion);
        String version = String.format("ffmpeg version == %s", getVersion());
        tvVersion.setText(version);

        Log.d(TAG, version);
    }

    static {
        System.loadLibrary("utils");
        System.loadLibrary("avutil");
    }

    public static native String getVersion();
}
