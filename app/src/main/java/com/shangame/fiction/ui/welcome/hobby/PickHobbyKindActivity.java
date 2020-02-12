package com.shangame.fiction.ui.welcome.hobby;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiction.bar.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.core.manager.AppSetting;
import com.shangame.fiction.net.response.PickHobbyKindResponse;
import com.shangame.fiction.ui.bookstore.BookStoreChannel;
import com.shangame.fiction.ui.main.MainFrameWorkActivity;
import com.shangame.fiction.ui.main.MainItemType;

public class PickHobbyKindActivity extends BaseActivity implements View.OnClickListener, PickHobbyKindContacts.View {

    private PickHobbyKindPresenter pickHobbyKindPresenter;

    private int maleChannel = BookStoreChannel.All;

    private ImageView mImageBoyChecked;
    private ImageView mImageGirlChecked;
    private TextView mTextPrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_hobby_kind);
        initView();
        initPresenter();
    }

    private void initView() {
        findViewById(R.id.img_boy).setOnClickListener(this);
        findViewById(R.id.img_girl).setOnClickListener(this);
        findViewById(R.id.image_boy_checked).setOnClickListener(this);
        findViewById(R.id.image_girl_checked).setOnClickListener(this);
        findViewById(R.id.img_into).setOnClickListener(this);

        mImageBoyChecked = findViewById(R.id.image_boy_checked);
        mImageGirlChecked = findViewById(R.id.image_girl_checked);
        mTextPrompt = findViewById(R.id.text_prompt);
    }

    private void initPresenter() {
        pickHobbyKindPresenter = new PickHobbyKindPresenter();
        pickHobbyKindPresenter.attachView(this);
    }

    @Override
    public void onClick(View v) {
        long id = v.getId();
        if (id == R.id.img_into) {
            if (maleChannel == BookStoreChannel.All) {
                showToast("请先选中类型");
            } else {
                commitPickHobbyKind(maleChannel);

                AppSetting appSetting = AppSetting.getInstance(mContext);
                appSetting.putBoolean(SharedKey.FIRST_LUNCH, false);

                Intent intent = new Intent(mActivity, MainFrameWorkActivity.class);
                intent.putExtra("CurrentItem", MainItemType.BOOK_STORE);
                intent.putExtra("malechannel", maleChannel);
                startActivity(intent);
                finish();
            }
        } else if (id == R.id.image_boy_checked || id == R.id.img_boy) {
            maleChannel = BookStoreChannel.BOY;
            mImageBoyChecked.setSelected(true);
            mImageGirlChecked.setSelected(false);
            mTextPrompt.setText("优先为您推荐男生喜欢的书籍~");
        } else if (id == R.id.image_girl_checked || id == R.id.img_girl) {
            maleChannel = BookStoreChannel.GIRL;
            mImageBoyChecked.setSelected(false);
            mImageGirlChecked.setSelected(true);
            mTextPrompt.setText("优先为您推荐女生喜欢的书籍~");
        }
    }

    private void commitPickHobbyKind(int maleChannel) {
        AppSetting appSetting = AppSetting.getInstance(mContext);
        appSetting.putInt(SharedKey.MALE_CHANNEL, maleChannel);
        pickHobbyKindPresenter.commitMaleChannel(maleChannel);
    }

    @Override
    public void commitPickHobbyKindSuccess(String kinds, PickHobbyKindResponse pickHobbyKindResponse) {
    }

    @Override
    public void commitMaleChannelSuccess() {
    }
}
