package com.mobile.healthmate.ui.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;

import com.mobile.healthmate.R;
import com.mobile.healthmate.app.BaseActivity;
import com.mobile.healthmate.app.lib.viewinject.FindViewById;
import com.mobile.healthmate.app.lib.viewinject.ViewInjecter;
import com.mobile.healthmate.model.HealthTest.HealthReport;
import com.mobile.healthmate.model.online.CmsContentModel;
import com.mobile.healthmate.view.TopBar;

import static com.mobile.healthmate.ui.HealthTest.FaceActivity.KEY_CMS_CONTENT;
import static com.mobile.healthmate.ui.HealthTest.FaceActivity.KEY_HEALTH_REPORT;

public class WebActivity extends BaseActivity {
    private CmsContentModel cmsContentModel;

    @FindViewById(R.id.test_right_btn)
    private TopBar mTopBar;

    @FindViewById(R.id.web_view)
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ViewInjecter.inject(this);
        cmsContentModel = getIntent().getParcelableExtra(KEY_CMS_CONTENT);
        mTopBar.setTitle(cmsContentModel.getTitle());
        webView.loadData(cmsContentModel.getTxt(),"text/html;; charset=UTF-8", null);
    }
}
