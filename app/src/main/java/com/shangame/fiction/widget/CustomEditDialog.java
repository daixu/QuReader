package com.shangame.fiction.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fiction.bar.R;

public class CustomEditDialog extends Dialog {
    public CustomEditDialog(@NonNull Context context) {
        super(context);
    }

    public CustomEditDialog(@NonNull Context context, int theme) {
        super(context, theme);
    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        if (null != window) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            window.getDecorView().setPadding(0, 0, 0, 0);
            window.setAttributes(layoutParams);
        }
    }

    @Override
    public void dismiss() {
        View view = getCurrentFocus();
        if (view instanceof TextView) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (null != inputMethodManager) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
            }
        }
        super.dismiss();
    }

    public static class Builder {

        private Context context;
        private CharSequence positiveButtonText;
        private CharSequence titleText;
        private CharSequence contentText;

        private OnClickListener positiveButtonListener, defaultListener = new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        };

        public Builder(Context context) {
            this.context = context;
        }

        public CustomEditDialog.Builder setTitle(CharSequence sequence) {
            this.titleText = sequence;
            return this;
        }

        public CustomEditDialog.Builder setTitle(int strId) {
            this.titleText = context.getText(strId);
            return this;
        }

        public CustomEditDialog.Builder setContent(CharSequence sequence) {
            this.contentText = sequence;
            return this;
        }

        public CustomEditDialog.Builder setContent(int strId) {
            this.contentText = context.getText(strId);
            return this;
        }

        public CustomEditDialog.Builder setPositiveButton(CharSequence text, OnClickListener listener) {
            this.positiveButtonText = text;
            this.positiveButtonListener = listener == null ? defaultListener
                    : listener;
            return this;
        }

        public CustomEditDialog.Builder setPositiveButton(int strId, OnClickListener listener) {
            this.positiveButtonText = context.getText(strId);
            this.positiveButtonListener = listener == null ? defaultListener
                    : listener;
            return this;
        }

        public CustomEditDialog create() {
            final CustomEditDialog dialog = new CustomEditDialog(context, R.style.CustomDialogStyle);
            LayoutInflater inflater = LayoutInflater.from(context);
            View contentView = inflater.inflate(R.layout.custom_edit_dialog, null);
            dialog.addContentView(contentView, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView titleView = contentView.findViewById(R.id.tv_dialog_title);
            EditText editContent = contentView.findViewById(R.id.edit_dialog_content);
            Button positiveButton = contentView.findViewById(R.id.btn_positive);
            ImageView imgClose = contentView.findViewById(R.id.img_close);

            // 赋值和添加事件

            settings(titleView, editContent);
            addListener(positiveButton, imgClose, dialog);
            dialog.setContentView(contentView);
            return dialog;
        }

        private void settings(TextView titleView, EditText editContent) {
            if (TextUtils.isEmpty(titleText)) {
                ((View) titleView.getParent()).setVisibility(View.GONE);
            } else {
                ((View) titleView.getParent()).setVisibility(View.VISIBLE);
                titleView.setText(titleText);
            }

            if (TextUtils.isEmpty(contentText)) {
                editContent.setVisibility(View.GONE);
            } else {
                editContent.setVisibility(View.VISIBLE);
                editContent.setHint(contentText);
            }
        }

        private void addListener(Button positiveButton, ImageView imgClose, final CustomEditDialog dialog) {
            if (TextUtils.isEmpty(positiveButtonText)) {
                ((View) (positiveButton.getParent().getParent())).setVisibility(View.GONE);
            } else {
                ((View) (positiveButton.getParent().getParent())).setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(positiveButtonText)) {
                    positiveButton.setVisibility(View.GONE);
                } else {
                    positiveButton.setVisibility(View.VISIBLE);
                    positiveButton.setText(positiveButtonText);
                    positiveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            positiveButtonListener.onClick(dialog,
                                    DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }
            }

            imgClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
    }
}
