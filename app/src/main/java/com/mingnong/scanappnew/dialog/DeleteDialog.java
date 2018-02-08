package com.mingnong.scanappnew.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mingnong.scanappnew.R;

/**
 * Created by wyw on 2016/11/18.
 */

public class DeleteDialog extends Dialog {
    private EditText etNumber;
    private Button btConfirm,btCancel;
    public DeleteDialog(final Context context, final OnClickListener listener) {
        super(context);
        View parent = LayoutInflater.from(getContext()).inflate(R.layout.dialog_delete,null);
        setContentView(parent);
        setTitle("删除");
        etNumber = (EditText) parent.findViewById(R.id.et_number);
        btConfirm = (Button) parent.findViewById(R.id.bt_confirm);
        btCancel = (Button) parent.findViewById(R.id.bt_cancel);
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etNumber.getText().toString())) {
                    Toast.makeText(context, "请输入数据", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (listener != null) {
                    listener.onClick(Integer.valueOf(etNumber.getText().toString()));
                }
                dismiss();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    @Override
    public void show() {
        super.show();
        etNumber.setText("");
    }

    public interface OnClickListener{
        void onClick(int position);
    }
}
