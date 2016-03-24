package com.genius.todolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by 112 on 2016/3/15.
 */
public class EditActivity extends AppCompatActivity {

    /**
     * 输入框
     **/
    private EditText editText;
    /** 按钮——确定 **/
    private ImageView imageView_comfirm;
    /** 按钮——取消 **/
    private ImageView imageView_cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        setActionBar();
        init();

    }

    private void init()
    {
        imageView_comfirm= (ImageView) findViewById(R.id.activity_edit_button);
        imageView_cancel = (ImageView) findViewById(R.id.activity_edit_button2);
        editText = (EditText) findViewById(R.id.activity_edit_editText);

        imageView_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageView_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString(MainActivity.REQUEST_STRING, editText.getText().toString());
                intent.putExtras(bundle);
                setResult(MainActivity.REQUEST_CODE, intent);
                finish();
            }
        });
    }

    private void setActionBar() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        View view = LayoutInflater.from(this).inflate(R.layout.view_actionbar_title, null);
//        editText = (EditText) view.findViewById(R.id.content_main_editText);
//        btn_confirm = (Button) view.findViewById(R.id.content_main_button);
        getSupportActionBar().setCustomView(R.layout.view_actionbar_title);

    }
}
