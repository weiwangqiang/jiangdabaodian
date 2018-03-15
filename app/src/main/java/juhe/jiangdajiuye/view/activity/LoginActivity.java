package juhe.jiangdajiuye.view.activity;

import android.os.Bundle;
import android.view.View;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.core.BaseActivity;
import juhe.jiangdajiuye.view.fragment.LoginFragment;
import juhe.jiangdajiuye.view.fragment.LogoutFragment;
import juhe.jiangdajiuye.user.UserManager;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity  {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String TAG = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(UserManager.getInStance().isLogin()){
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.activity_login_fragment_layout,new LogoutFragment()).commit();
        }else{
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.activity_login_fragment_layout,new LoginFragment()).commit();
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }

}

