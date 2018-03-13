package juhe.jiangdajiuye.view.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.bean.bmobUser.UserBean;
import juhe.jiangdajiuye.sql.SharePreferHelper;
import juhe.jiangdajiuye.user.UserManager;
import juhe.jiangdajiuye.utils.ResourceUtils;
import juhe.jiangdajiuye.utils.ToastUtils;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2018-02-05
 */

public class LoginFragment extends BaseTooBarFragment {
    private View viewRoot ;

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(viewRoot != null)
            return viewRoot ;
        viewRoot = inflater.inflate(R.layout.fragment_login,container,false);
        init();
        return viewRoot;
    }

    private void init() {
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) viewRoot.findViewById(R.id.email);
        mEmailView.setText(SharePreferHelper.
                getInstance().getString(SharePreferHelper.KEY_USER_NAME,""));
//        populateAutoComplete();
        toolbar = (Toolbar) viewRoot.findViewById(R.id.Library_toolbar);
        initToolBar();
        mPasswordView = (EditText) viewRoot.findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) viewRoot.findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = viewRoot.findViewById(R.id.login_form);
        mProgressView = viewRoot.findViewById(R.id.login_progress);
        if(!TextUtils.isEmpty(mEmailView.getText())){
            mPasswordView.requestFocus();
        }
    }

    private void initToolBar() {
        toolbar.setTitle(ResourceUtils.getString(R.string.title_login));
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
//        email = "we@";
//        password = "111111";
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            UserBean userBean = new UserBean();
            userBean.setName(email);
            userBean.setPassWord(password);
            executeLogin(userBean);
        }
    }

    private void executeLogin(final UserBean userBean) {
        BmobQuery<UserBean> query = new BmobQuery<>();
        query.addWhereEqualTo("name", userBean.getName());
        query.setLimit(1);
        query.findObjects(new FindListener<UserBean>() {
            @Override
            public void done(List<UserBean> object, BmobException e) {
                if (e == null && object.size() != 0) {
                    if(!object.get(0).equals(userBean)){
                        ToastUtils.showToast(ResourceUtils.getString(R.string.toast_user_password_error));
                        showProgress(false);
                        return;
                    }
                    ToastUtils.showToast("登陆成功");
                    UserManager.getInStance().setLogin(object.get(0));
                    loginSuccess(object.get(0));
                } else {
                    registerUserMessage(userBean);
                }
            }
        });
    }

    private void loginSuccess(UserBean userBean) {
        SharePreferHelper.getInstance().setString(SharePreferHelper.KEY_USER_NAME,userBean.getName());
        getActivity().setResult(Activity.RESULT_OK);
        finish();
    }

    //注册用户
    private void registerUserMessage(final UserBean userBean) {
        userBean.save(new SaveListener<String>() {

            @Override
            public void done(String s, BmobException e) {
                if (null == e) {
                    executeLogin(userBean);
//                    ToastUtils.showToast("注册成功");
//                    UserManager.getInStance().setLogin(userBean);
//                    loginSuccess(userBean);
                }else{
                    ToastUtils.showToast(ResourceUtils.getString(R.string.toast_register_failure));
                    showProgress(false);
                }
            }
        });
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.length() >3;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {

        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


}
