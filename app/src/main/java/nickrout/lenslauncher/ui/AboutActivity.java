package nickrout.lenslauncher.ui;

import android.animation.Animator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import nickrout.lenslauncher.R;

/**
 * Created by nickrout on 2016/04/06.
 */
public class AboutActivity extends BaseActivity {

    private static final String TAG = "AboutActivity";

    @Bind(R.id.text_view_about)
    TextView mTextViewAbout;

    @Bind(R.id.backdrop)
    ImageView mImageAbout;

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mCollapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, R.color.colorTransparent));
        setupText();
        mImageAbout.postDelayed(new Runnable() {
            @Override
            public void run() {
                circularRevealAboutImage();
            }
        }, 150);
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
        mTextViewAbout.setText(Html.fromHtml(getString(R.string.about)));
        mTextViewAbout.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
