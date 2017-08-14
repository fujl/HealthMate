package com.mobile.healthmate.ui.HealthTest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mobile.healthmate.R;
import com.mobile.healthmate.app.BaseActivity;
import com.mobile.healthmate.app.lib.viewinject.FindViewById;
import com.mobile.healthmate.app.lib.viewinject.ViewInjecter;
import com.mobile.healthmate.model.HealthTest.HealthReport;
import com.mobile.healthmate.view.CommonItemView;
import com.mobile.healthmate.view.TopBar;

import static com.mobile.healthmate.app.HConstant.RESULT_CODE_REG_REPORT_FINISH;
import static com.mobile.healthmate.ui.HealthTest.FaceActivity.KEY_HEALTH_REPORT;

public class HealthTestSubmitActivity extends BaseActivity {

    public static final int GENDER = 1;
    public static final int AGE = 2;
    public static final int COLD = 3;
    public static final int SWEATING = 4;
    public static final int PAIN = 5;
    public static final int SLEEP = 6;

    public final String[] ages = {"20", "20-25", "25-30", "30-35", "35-40", "40-45", "50-55", "55-60", "60"};
    public final String[] agesString = {"20岁以下", "20-25岁", "25-30岁", "30-35岁", "35-40岁", "40-45岁", "50-55岁", "55-60岁", "60以上"};

    private String age = "";

    @FindViewById(R.id.test_right_btn)
    private TopBar mTopBar;

    @FindViewById(R.id.user_gender)
    private CommonItemView mUserGender;

    @FindViewById(R.id.user_age)
    private CommonItemView mUserAge;

    @FindViewById(R.id.user_cold)
    private CommonItemView mUserCold;

    @FindViewById(R.id.user_sweating)
    private CommonItemView mUserSweating;

    @FindViewById(R.id.user_pain)
    private CommonItemView mUserPain;

    @FindViewById(R.id.user_sleep)
    private CommonItemView mUserSleep;

    public static void startActivity(Context context) {
        Intent intent = new Intent();
//        intent.putExtra(KEY_INQUIRY_ID, inquiryId);
        intent.setClass(context, HealthTestSubmitActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_test_submit);
        ViewInjecter.inject(this);
        registerListener();
    }

    private void registerListener() {
        mTopBar.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickNext();
            }
        });
        mUserGender.setOnContentClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] genders = {"男", "女"};
                int selectId = 0;
                if (mUserGender.getContentString().equals("女")) {
                    selectId = 1;
                }
                selectItemType(GENDER, selectId, genders, "请选择您的性别");
            }
        });
        mUserAge.setOnContentClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectId = 0;
                if (age.equals("20-25")) {
                    selectId = 1;
                } else if (age.equals("25-30")) {
                    selectId = 2;
                } else if (age.equals("30-35")) {
                    selectId = 3;
                } else if (age.equals("35-40")) {
                    selectId = 4;
                } else if (age.equals("40-45")) {
                    selectId = 5;
                } else if (age.equals("50-55")) {
                    selectId = 6;
                } else if (age.equals("55-60")) {
                    selectId = 7;
                } else if (age.equals("60")) {
                    selectId = 8;
                }
                selectItemType(AGE, selectId, agesString, "请选择您的年龄");
            }
        });
        mUserCold.setOnContentClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] colds = {"偏寒", "正常", "偏热"};
                int selectId = 0;
                if (mUserCold.getContentString().equals("正常")) {
                    selectId = 1;
                } else if (mUserCold.getContentString().equals("偏热")) {
                    selectId = 2;
                }
                selectItemType(COLD, selectId, colds, "请选择您的寒热情况");
            }
        });
        mUserSweating.setOnContentClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] sweatings = {"常出汗", "正常", "少出汗"};
                int selectId = 0;
                if (mUserSweating.getContentString().equals("正常")) {
                    selectId = 1;
                } else if (mUserSweating.getContentString().equals("少出汗")) {
                    selectId = 2;
                }
                selectItemType(SWEATING, selectId, sweatings, "请选择您的出汗情况");
            }
        });
        mUserPain.setOnContentClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] pains = {"头疼", "胸疼", "胃疼", "无"};
                int selectId = 0;
                if (mUserPain.getContentString().equals("胸疼")) {
                    selectId = 1;
                } else if (mUserPain.getContentString().equals("胃疼")) {
                    selectId = 2;
                } else if (mUserPain.getContentString().equals("无")) {
                    selectId = 3;
                }
                selectItemType(PAIN, selectId, pains, "请选择您的疼痛情况");
            }
        });
        mUserSleep.setOnContentClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] sleeps = {"失眠", "正常", "嗜睡"};
                int selectId = 0;
                if (mUserSleep.getContentString().equals("正常")) {
                    selectId = 1;
                } else if (mUserSleep.getContentString().equals("嗜睡")) {
                    selectId = 2;
                }
                selectItemType(SLEEP, selectId, sleeps, "请选择您的睡眠质量");
            }
        });
    }

    private void selectItemType(final int type, int selectId, final String[] items, String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(HealthTestSubmitActivity.this);
        builder.setTitle(title);
        builder.setSingleChoiceItems(items, selectId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (type) {
                    case GENDER:
                        mUserGender.setContent(items[which]);
                        break;
                    case AGE:
                        mUserAge.setContent(items[which]);
                        age = ages[which];
                        break;
                    case COLD:
                        mUserCold.setContent(items[which]);
                        break;
                    case SWEATING:
                        mUserSweating.setContent(items[which]);
                        break;
                    case PAIN:
                        mUserPain.setContent(items[which]);
                        break;
                    case SLEEP:
                        mUserSleep.setContent(items[which]);
                        break;
                }

                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void clickNext() {
        if (mUserGender.getContentString().equals("")) {
            Toast.makeText(this, "请选择您的性别", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mUserAge.getContentString().equals("")) {
            Toast.makeText(this, "请选择您的年龄段", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mUserCold.getContentString().equals("")) {
            Toast.makeText(this, "请选择您寒热情况", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mUserSweating.getContentString().equals("")) {
            Toast.makeText(this, "请选择您出汗情况", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mUserPain.getContentString().equals("")) {
            Toast.makeText(this, "请选择您疼痛情况", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mUserPain.getContentString().equals("")) {
            Toast.makeText(this, "请选择您睡眠质量", Toast.LENGTH_SHORT).show();
            return;
        }

        HealthReport healthReport = new HealthReport();
        healthReport.setGender(mUserGender.getContentString());
        healthReport.setAge(mUserAge.getContentString());
        healthReport.setCold(mUserCold.getContentString());
        healthReport.setSweating(mUserSweating.getContentString());
        healthReport.setPain(mUserPain.getContentString());
        healthReport.setSleep(mUserSleep.getContentString());

        Intent intent = new Intent();
        intent.putExtra(KEY_HEALTH_REPORT, healthReport);
        intent.setClass(this, FaceActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CODE_REG_REPORT_FINISH) {
            setResult(RESULT_CODE_REG_REPORT_FINISH);
            finish();
        }
    }
}
