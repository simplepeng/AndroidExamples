package me.simple.example_lottie;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageView;

import com.airbnb.lottie.FontAssetDelegate;
import com.airbnb.lottie.ImageAssetDelegate;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieImageAsset;
import com.airbnb.lottie.LottieListener;
import com.airbnb.lottie.LottieTask;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView imageView = findViewById(R.id.imageView);

        final LottieDrawable drawable = new LottieDrawable();
        drawable.setImageAssetDelegate(new ImageAssetDelegate() {
            @Nullable
            @Override
            public Bitmap fetchBitmap(LottieImageAsset asset) {
                String name = asset.getId().replace("image_", "img_");
                try {
                    return BitmapFactory.decodeStream(getAssets().open(name + ".png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            }
        });
        drawable.setFontAssetDelegate(new FontAssetDelegate(){
            @Override
            public Typeface fetchFont(String fontFamily) {
                return Typeface.createFromAsset(getAssets(),"Source Han Sans CN.ttf");
            }
        });
        imageView.setImageDrawable(drawable);
        LottieTask<LottieComposition> task = LottieCompositionFactory.fromAsset(this, "data.json");
        task.addListener(new LottieListener<LottieComposition>() {
            @Override
            public void onResult(LottieComposition result) {
                drawable.setComposition(result);
                drawable.loop(true);
                drawable.playAnimation();


            }
        });
    }
}
