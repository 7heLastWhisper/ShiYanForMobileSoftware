package com.jnu.booklistapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_ADD_DATA = 200;
    public static final int RESULT_CODE_ADD_OK = REQUEST_CODE_ADD_DATA + 1;
    public static final int RESULT_CODE_EDIT_OK = REQUEST_CODE_ADD_DATA + 2;
    public static final int REQUEST_CODE_EDIT_DATA = REQUEST_CODE_ADD_DATA + 3;
    RecyclerView recyclerView;
    List<BookList.Book> mBookList;
    BookAdapter mAdapter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_DATA) {
            if (resultCode == RESULT_CODE_ADD_OK) {
                String name = data.getStringExtra("bookName");
                int type = data.getIntExtra("bookType", 0);
                mBookList.add(new BookList.Book(name, R.drawable.book_no_name, type));
                mAdapter.notifyItemInserted(mBookList.size());
            }
        }
        if (requestCode == REQUEST_CODE_EDIT_DATA) {
            if (resultCode == RESULT_CODE_EDIT_OK) {
                assert data != null;
                String name = data.getStringExtra("bookName");
                int position = data.getIntExtra("position", mBookList.size());
                mBookList.get(position).setTitle(name);
                mAdapter.notifyItemChanged(position);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);

        recyclerView = findViewById(R.id.recycle_view_books);
        mBookList = BookList.getListBooks();
        initData();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new BookAdapter(mBookList);
        recyclerView.setAdapter(mAdapter);
        //添加分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(
                MainActivity.this, DividerItemDecoration.VERTICAL));
    }

    public void initData() {
        mBookList.add(new BookList.Book("软件项目管理案例教程（第4版）", R.drawable.book_2, 1));
        mBookList.add(new BookList.Book("创新工程实践", R.drawable.book_no_name, 1));
        mBookList.add(new BookList.Book("创新工程实践", R.drawable.book_no_name, 2));
        mBookList.add(new BookList.Book("信息安全数学基础（第2版）", R.drawable.book_1, 1));
        mBookList.add(new BookList.Book("信息安全数学基础（第2版）", R.drawable.book_1, 2));
        mBookList.add(new BookList.Book("信息安全数学基础（第2版）", R.drawable.book_1, 1));
    }

    public class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int BOOK_ADD = 1;
        private static final int BOOK_EDIT = 2;
        private static final int BOOK_DELETE = 3;

        private final List<BookList.Book> adpBookList;

        public BookAdapter(List<BookList.Book> bookList) {
            this.adpBookList = bookList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView;
            if (viewType == 1) {
                //普通条目的layout
                itemView = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_common_layout, parent, false);
                return new BookHolder(itemView);
            } else {
                //广告条目的layout
                itemView = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_show_layout, parent, false);
                return new BookHolder(itemView).new ShowHolder(itemView);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof BookHolder) {
                BookHolder viewHolder = (BookHolder) holder;
                //为控件设置对应的资源
                viewHolder.bookName.setText(mBookList.get(position).getTitle());
                viewHolder.bookCover.setBackgroundResource(mBookList.get(position).getCoverResourceId());
            } else if (holder instanceof BookHolder.ShowHolder) {
                BookHolder.ShowHolder viewHolder = (BookHolder.ShowHolder) holder;
                viewHolder.bookName.setText(mBookList.get(position).getTitle());
                viewHolder.bookCover.setBackgroundResource(mBookList.get(position).getCoverResourceId());
            }
        }

        @Override
        public int getItemViewType(int position) {
            return mBookList.get(position).getBookType();
        }

        @Override
        public int getItemCount() {
            return mBookList.size();
        }

        class BookHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
            //实现普通条目的ViewHolder
            private TextView bookName;
            private ImageView bookCover;

            public BookHolder(@NonNull View itemView) {
                super(itemView);
                this.bookCover = itemView.findViewById(R.id.image_view_common_book_cover);
                this.bookName = itemView.findViewById(R.id.text_view_common_book_title);

                //让Item可以响应创建菜单的点击事件
                itemView.setOnCreateContextMenuListener(this);
            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                //添加三个菜单项
                MenuItem menuItemBookAdd = menu.add(Menu.NONE, BOOK_ADD, 1, "添加");
                MenuItem menuItemBookEdit = menu.add(Menu.NONE, BOOK_EDIT, 2, "编辑");
                MenuItem menuItemBookDelete = menu.add(Menu.NONE, BOOK_DELETE, 3, "删除");
                //菜单项的响应事件
                menuItemBookAdd.setOnMenuItemClickListener(this);
                menuItemBookEdit.setOnMenuItemClickListener(this);
                menuItemBookDelete.setOnMenuItemClickListener(this);
            }

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int position = getAdapterPosition();
                Intent intent;
                //获取长按菜单对应的条目在RecyclerView中的位置（即看上去所在的位置）
                switch (item.getItemId()) { //item.getItemId表示按的是右键菜单中的哪一项
                    case BOOK_ADD:
                        intent = new Intent(MainActivity.this, AddBookActivity.class);
                        MainActivity.this.startActivityForResult(intent, REQUEST_CODE_ADD_DATA);
                        break;
                    case BOOK_EDIT:
                        //跳转到EditBookActivity进行编辑
                        intent = new Intent(MainActivity.this, EditBookActivity.class);
                        intent.putExtra("position", position);
                        intent.putExtra("Name", mBookList.get(position).getTitle());
                        MainActivity.this.startActivityForResult(intent, REQUEST_CODE_EDIT_DATA);
                        break;
                    case BOOK_DELETE:
                        //弹出对话框，只有文本提示、确定和取消，可以不写自定义布局
                        AlertDialog.Builder bookDeleteDialog = new AlertDialog.Builder(MainActivity.this);
                        bookDeleteDialog.setMessage("确定要删除此书籍吗？");
                        bookDeleteDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mBookList.remove(position);
                                BookAdapter.this.notifyItemRemoved(position);
                            }
                        });
                        bookDeleteDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                        bookDeleteDialog.create().show();
                        break;
                }
                return false;
            }

            class ShowHolder extends RecyclerView.ViewHolder {
                //实现广告条目的ViewHolder
                TextView bookName;
                ImageView bookCover;

                public ShowHolder(@NonNull View itemView) {
                    super(itemView);
                    this.bookCover = itemView.findViewById(R.id.image_view_show_book_cover);
                    this.bookName = itemView.findViewById(R.id.text_view_show_book_title);
                }
            }
        }
    }
}