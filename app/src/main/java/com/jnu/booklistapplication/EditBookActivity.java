package com.jnu.booklistapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditBookActivity extends AppCompatActivity {
    Button btnEditOk;
    Button btnEditCancel;
    EditText etEditBookName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        //接收从MainActivity传过来的位置信息

        btnEditOk = findViewById(R.id.btn_item_edit_book_ok);
        btnEditCancel = findViewById(R.id.btn_item_edit_book_cancel);
        etEditBookName = findViewById(R.id.et_item_edit_book_name);
        btnEditOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEditBookName.getText().toString().isEmpty()) {
                    Toast.makeText(EditBookActivity.this, "书名不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("bookName", etEditBookName.getText().toString());
                    intent.putExtra("position", position);
                    setResult(MainActivity.RESULT_CODE_EDIT_OK, intent);
                    //只传状态码可以只要第一个参数，传数据务必加上第二个参数！！
                    EditBookActivity.this.finish();
                }
            }
        });
        btnEditCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditBookActivity.this.finish();
            }
        });
    }
}