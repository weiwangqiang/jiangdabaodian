package juhe.jiangdajiuye.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.user.UserManager;
import juhe.jiangdajiuye.utils.ResourceUtils;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2018-02-05
 */

public class LogoutFragment extends BaseTooBarFragment implements View.OnClickListener {
    private View viewRoot  ;
    private Toolbar toolbar;
    private TextView tvPrompt;
    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(viewRoot != null){
            return viewRoot ;
        }
        viewRoot = inflater.inflate(R.layout.fragment_logout,container,false);
        init() ;
        return viewRoot ;
    }

    private void init() {
        toolbar = (Toolbar) viewRoot.findViewById(R.id.fragment_logout_toolbar);
        viewRoot.findViewById(R.id.fragment_logout_button).setOnClickListener(this);
        tvPrompt = (TextView) viewRoot.findViewById(R.id.fragment_logout_textview);
        initToolBar();
        String hello = "\thello：";
        StringBuilder sb = new StringBuilder(hello);
        sb.append(UserManager.getInStance().getUserBean().getName());
        sb.append("\n\n");
        sb.append(ResourceUtils.getString(R.string.textview_logout_prompt));
        SpannableString spanStr = new SpannableString(sb.toString());
        spanStr.setSpan(new AbsoluteSizeSpan(50),0,
                hello.length()+UserManager.getInStance().getUserBean()
                        .getName().length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvPrompt.setText(spanStr);
    }

    private void initToolBar() {
        toolbar.setTitle(ResourceUtils.getString(R.string.title_fragment_logout));
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_logout_button:
                UserManager.getInStance().setLogout();
                getActivity().setResult(Activity.RESULT_CANCELED);
                finish();
                break;
        }
    }
}
