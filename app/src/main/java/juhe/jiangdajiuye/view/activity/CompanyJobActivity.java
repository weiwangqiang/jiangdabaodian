package juhe.jiangdajiuye.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.adapter.CompanyUrlAdapter;
import juhe.jiangdajiuye.base.BaseListActivity;
import juhe.jiangdajiuye.bean.CompanyUrl;
import juhe.jiangdajiuye.ui.recyclerView.adapter.AbsAdapter;

public class CompanyJobActivity extends BaseListActivity<CompanyUrl> {
    private CompanyUrlAdapter adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_job);
        supportToolbar(R.id.company_url_toolbar,"各大公司");
    }

    @Override
    protected boolean needInitRefresh() {
        return true ;
    }

    @NonNull
    @Override
    protected AbsAdapter<CompanyUrl> getAdapter() {
        adapter  =new CompanyUrlAdapter(this,R.layout.item_company_url);
        adapter.setOnItemClickListener(new AbsAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, Object o, int position) {

            }
        });
        return adapter;
    }

    /**
     * 重置请求参数
     *
     * @return
     */
    @Override
    protected void resetParams() {

    }

    /**
     * 进行网络请求工作
     * 有3处：上拉，下拉，初始化（needInitRefresh返回true的情况）
     */
    @Override
    protected void requestMes() {

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
