package nickrout.lenslauncher.ui;

import android.animation.Animator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import nickrout.lenslauncher.R;

/**
 * Created by nickrout on 2016/04/06.
 */
public class AboutActivity extends BaseActivity {

    private static final String TAG = "AboutActivity";

    private TextView mTextViewAbout;
    private ImageView mImageAbout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupText();
        mImageAbout = (ImageView) findViewById(R.id.image_about);
        mImageAbout.post(new Runnable() {
            @Override
            public void run() {
                circularRevealAboutImage();
            }
        });
    }

    private void circularRevealAboutImage() {
        if (mImageAbout != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int cx = mImageAbout.getWidth() / 2;
                int cy = mImageAbout.getHeight() / 2;
                float finalRadius = (float) Math.hypot(cx, cy);
                Animator anim =
                        ViewAnimationUtils.createCircularReveal(mImageAbout, cx, cy, 0, finalRadius);
                mImageAbout.setVisibility(View.VISIBLE);
                anim.start();
            } else {
                mImageAbout.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setupText() {
        mTextViewAbout = (TextView) findViewById(R.id.text_view_about);
        mTextViewAbout.setText(Html.fromHtml(getString(R.string.about)));
        mTextViewAbout.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
