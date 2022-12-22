package com.jnu.booklistapplication;

import static com.jnu.booklistapplication.BookFragment.RESULT_CODE_ADD_OK;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class AddBookActivity extends AppCompatActivity {

    RadioGroup rgAddBookType;
    EditText etAddBookName;
    Button btnAddBookOk;
    Button btnAddBookCancel;
    RadioButton rbAddBookType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        etAddBookName = findViewById(R.id.et_item_add_book_name);
        rgAddBookType = findViewById(R.id.rg_item_add_book_type);
        btnAddBookCancel = findViewById(R.id.btn_item_add_book_cancel);
        btnAddBookOk = findViewById(R.id.btn_item_add_book_ok);

        rgAddBookType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                rbAddBookType = findViewById(group.getCheckedRadioButtonId());
            }
        });

        btnAddBookOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                //按下OK，回传数据：书名和书类型
                intent.putExtra("bookName", etAddBookName.getText().toString());
                intent.putExtra("bookType", Integer.parseInt(rbAddBookType.getText().toString()));

                setResult(RESULT_CODE_ADD_OK, intent);
                AddBookActivity.this.finish();
            }
        });
        btnAddBookCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AddBookActivity.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        //直接返回也回传数据
        intent.putExtra("bookName", etAddBookName.getText().toString());
        intent.putExtra("bookType", Integer.parseInt(rbAddBookType.getText().toString()));

        setResult(RESULT_CODE_ADD_OK, intent);
        finish();
    }
}