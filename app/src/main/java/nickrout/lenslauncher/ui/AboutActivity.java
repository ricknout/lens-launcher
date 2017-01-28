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

import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;
import butterknife.ButterKnife;
import nickrout.lenslauncher.R;
import nickrout.lenslauncher.background.NightModeObservable;

/**
 * Created by nickrout on 2016/04/06.
 */
public class AboutActivity extends BaseActivity implements Observer {

    private static final String TAG = "AboutActivity";

    @BindView(R.id.text_view_about)
    TextView mTextViewAbout;

    @BindView(R.id.backdrop)
    ImageView mImageAbout;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;

    @BindView(R.id.toolbar)
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
        NightModeObservable.getInstance().addObserver(this);
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

    @Override
    public void update(Observable observable, Object o) {
        if (observable instanceof NightModeObservable) {
            updateNightMode();
        }
    }
}
