package com.mobile.healthmate.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.healthmate.R;
import com.mobile.healthmate.app.App;
import com.mobile.healthmate.app.BaseActivity;
import com.mobile.healthmate.app.lib.listener.SimpleTextWatcher;
import com.mobile.healthmate.app.lib.utils.Encoder;
import com.mobile.healthmate.app.lib.viewinject.FindViewById;
import com.mobile.healthmate.app.lib.viewinject.ViewInjecter;
import com.mobile.healthmate.manager.OnResultListener;
import com.mobile.healthmate.manager.user.UserManager;
import com.mobile.healthmate.ui.main.MainActivity;
import com.mobile.healthmate.view.CheckableImageView;

public class LoginActivity extends BaseActivity {

    @FindViewById(R.id.login_username)
    protected EditText usernameView;
    @FindViewById(R.id.login_input_pwd)
    protected EditText pwdView;
    @FindViewById(R.id.login_clear_username)
    private ImageView clearUsernameView;
    @FindViewById(R.id.login_pwd_check)
    private CheckableImageView checkPwdView;

    @FindViewById(R.id.view_pwd_line)
    private View lineView;

    @FindViewById(R.id.login_clear_pwd)
    private ImageView clearPwdView;

    @FindViewById(R.id.login_btn)
    private Button login;

    private boolean[] inputStatus = {false, false};

    @App.Manager
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ViewInjecter.inject(this);

        pwdView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onLoginClick(textView);
                    return true;
                }
                return false;
            }
        });

        pwdView.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (pwdView.getText().length() != 0) {
                    clearPwdView.setVisibility(View.VISIBLE);
                    lineView.setVisibility(View.VISIBLE);
                    inputStatus[0] = true;
                    if (inputStatus[1]) {
                        login.setBackgroundColor(getResources().getColor(R.color.green));
                        login.setEnabled(true);
                    }
                } else {
                    clearPwdView.setVisibility(View.GONE);
                    lineView.setVisibility(View.GONE);
                    inputStatus[0] = false;
                    if (!inputStatus[1]) {
                        login.setBackgroundColor(getResources().getColor(R.color.login_btn_gray));
                        login.setEnabled(false);
                    }
                }
            }
        });
        clearUsernameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernameView.setText("");
            }
        });
        clearPwdView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pwdView.setText("");
            }
        });
        checkPwdView.setOnCheckedChangeListener(new CheckableImageView.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean isChecked) {
                if (isChecked) {
                    pwdView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    pwdView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                pwdView.setSelection(pwdView.getText().length());
            }
        });
        usernameView.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (usernameView.getText().toString().length() == 0) {
                    clearUsernameView.setVisibility(View.GONE);
                } else {
                    clearUsernameView.setVisibility(View.VISIBLE);
                }
                if (usernameView.getText().length() != 0) {
                    inputStatus[1] = true;
                    if (inputStatus[0]) {
                        login.setBackgroundColor(getResources().getColor(R.color.green));
                        login.setEnabled(true);
                    }
                } else {
                    inputStatus[1] = false;
                    if (!inputStatus[0]) {
                        login.setBackgroundColor(getResources().getColor(R.color.login_btn_gray));
                        login.setEnabled(false);
                    }
                }
            }
        });
    }

    public void onTestClick(View view) {
//        startActivity(MainActivity.class);
        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    public void onLoginClick(View view) {
        String username = usernameView.getText().toString();
        String pwd = pwdView.getText().toString();

        showLoadingProgressDialog();
        userManager.login(username, Encoder.encodeByMD5(pwd), new OnResultListener() {
            @Override
            public void onResult(int result) {
                dismissLoadingProgressDialog();
                if (result == RESULT_CODE_OK) {
                    //startActivity(MainActivity.class);
                    startActivity(new Intent(getActivity(), MainActivity.class));
                } else {
                    showToast(R.string.login_result_fail);
                }
            }
        });


    }


}
