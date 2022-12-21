package com.jnu.booklistapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jnu.booklistapplication.BookListMainActivity.Book;
import com.jnu.booklistapplication.BookListMainActivity.BookAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Book> mBookList;
    BookAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);

        recyclerView = findViewById(R.id.recycle_view_books);
        mBookList = BookListMainActivity.getListBooks();
        mBookList.add(new Book("软件项目管理案例教程（第4版）", R.drawable.book_2, 1));
        mBookList.add(new Book("创新工程实践", R.drawable.book_no_name, 1));
        mBookList.add(new Book("创新工程实践", R.drawable.book_no_name, 2));
        mBookList.add(new Book("信息安全数学基础（第2版）", R.drawable.book_1, 1));
        mBookList.add(new Book("信息安全数学基础（第2版）", R.drawable.book_1, 2));
        mBookList.add(new Book("信息安全数学基础（第2版）", R.drawable.book_1, 1));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new BookAdapter(mBookList);
        recyclerView.setAdapter(mAdapter);
        //添加分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(
                MainActivity.this, DividerItemDecoration.VERTICAL));
    }
}