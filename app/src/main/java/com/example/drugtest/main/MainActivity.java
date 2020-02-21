package com.example.drugtest.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.example.drugtest.R;
import com.example.drugtest.databinding.ActivityMainBinding;
import com.example.drugtest.model.Feature;

import java.util.List;

public class MainActivity extends AppCompatActivity implements  MainContract.View {
    private ProgressDialog mProgressDialog;
    private MainPresenter mainPresenter;
    ActivityMainBinding activityMainBinding;
    private MainAdapter mAdapter=new MainAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainPresenter=new MainPresenter();
        mainPresenter.attachView(this);
        mainPresenter.getMapInformation();
        activityMainBinding= DataBindingUtil.setContentView(this,
                R.layout.activity_main);

    }

    @Override
    public void onGetList(List<Feature> mlist) {
        activityMainBinding.recycleview.setLayoutManager(new LinearLayoutManager(this));
        activityMainBinding.recycleview.setAdapter(mAdapter);
        mAdapter.setListData(mlist);
    }

    @Override
    public void showProgressDialog(String text) {
        dismissProgressDialog();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("請等待載入");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }


    @Override
    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }
}
