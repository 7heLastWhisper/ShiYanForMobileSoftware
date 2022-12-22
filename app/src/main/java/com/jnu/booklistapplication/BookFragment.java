package com.jnu.booklistapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jnu.booklistapplication.data.BookList;
import com.jnu.booklistapplication.data.DataBank;

import java.util.List;

public class BookFragment extends Fragment {
    public static final int REQUEST_CODE_ADD_DATA = 200;
    public static final int RESULT_CODE_ADD_OK = REQUEST_CODE_ADD_DATA + 1;
    public static final int RESULT_CODE_EDIT_OK = REQUEST_CODE_ADD_DATA + 2;
    public static final int REQUEST_CODE_EDIT_DATA = REQUEST_CODE_ADD_DATA + 3;
    private static final int BOOK_ADD = 1;
    private static final int BOOK_EDIT = 2;
    private static final int BOOK_DELETE = 3;
    private DataBank dataBank;
    RecyclerView recyclerView;
    FloatingActionButton mFaBtn;
    List<BookList.Book> mBookList;
    BookAdapter mAdapter;

    // -----------接受回传数据------------

    //接收AddBookActivity的回传数据，添加书籍
    ActivityResultLauncher<Intent> AddActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_CODE_ADD_OK) {
                        Log.e("MYTAG", "收到回传");
                        Intent data = result.getData();
                        String name = data.getStringExtra("bookName");
                        int type = data.getIntExtra("bookType", 0);
                        mBookList.add(new BookList.Book(name, R.drawable.book_no_name, type));
                        dataBank.saveData();
                        mAdapter.notifyItemInserted(mBookList.size());
                    }
                }
            });

    //接收AddBookActivity的回传数据，更新书名
    ActivityResultLauncher<Intent> EditActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_CODE_EDIT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        String name = data.getStringExtra("bookName");
                        int position = data.getIntExtra("position", mBookList.size());
                        mBookList.get(position).setTitle(name);
                        dataBank.saveData();
                        mAdapter.notifyItemChanged(position);
                    }
                }
            });

    // ---------BookFragment响应上下文菜单--------


    public BookFragment() {
        // Required empty public constructor
    }

    public static BookFragment newInstance() {
        BookFragment fragment = new BookFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { //在这里传参数
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_book, container, false);
        recyclerView = rootView.findViewById(R.id.recycle_view_books);
        mBookList = BookList.getListBooks();
        initData();
        LinearLayoutManager layoutManager = new LinearLayoutManager(BookFragment.this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new BookAdapter(mBookList);
        recyclerView.setAdapter(mAdapter);
        //添加分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(
                BookFragment.this.getContext(), DividerItemDecoration.VERTICAL));
        //浮动菜单按钮
        mFaBtn = rootView.findViewById(R.id.fabtn_add_book);
        mFaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(mFaBtn);
            }
        });
        return rootView;
    }

    // ------------弹出菜单------------

    private void showPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(BookFragment.this.getContext(), view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //通过Switch选定似乎会直接return false，原因未明

                Intent intent;
                //跳转到AddBookActivity进行添加
                intent = new Intent(getActivity(), AddBookActivity.class);
                AddActivityResultLauncher.launch(intent);
                return false;
            }
        });
        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
            }
        });
        popupMenu.show();
    }


    public void initData() {
        dataBank = new DataBank(getContext());
        mBookList = dataBank.loadData();
    }

    public class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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
                return new BookAdapter.BookHolder(itemView);
            } else {
                //广告条目的layout
                itemView = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_show_layout, parent, false);
                return new BookAdapter.BookHolder(itemView).new ShowHolder(itemView);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof BookAdapter.BookHolder) {
                BookAdapter.BookHolder viewHolder = (BookAdapter.BookHolder) holder;
                //为控件设置对应的资源
                viewHolder.bookName.setText(mBookList.get(position).getTitle());
                viewHolder.bookCover.setBackgroundResource(mBookList.get(position).getCoverResourceId());
            } else if (holder instanceof BookAdapter.BookHolder.ShowHolder) {
                BookAdapter.BookHolder.ShowHolder viewHolder = (BookAdapter.BookHolder.ShowHolder) holder;
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
                int position = getAbsoluteAdapterPosition();
                Intent intent;
                switch (item.getItemId()) { //item.getItemId表示按的是右键菜单中的哪一项
                    case BOOK_ADD:
                        //跳转到AddBookActivity进行添加
                        intent = new Intent(BookFragment.this.getContext(), AddBookActivity.class);
                        AddActivityResultLauncher.launch(intent);
                        break;
                    case BOOK_EDIT:
                        //跳转到EditBookActivity进行编辑
                        intent = new Intent(BookFragment.this.getContext(), EditBookActivity.class);
                        intent.putExtra("position", position);
                        EditActivityResultLauncher.launch(intent);
                        break;
                    case BOOK_DELETE:
                        //弹出对话框，只有文本提示、确定和取消，可以不写自定义布局
                        AlertDialog.Builder bookDeleteDialog = new AlertDialog.Builder(BookFragment.this.getContext());
                        bookDeleteDialog.setMessage("确定要删除此书籍吗？");
                        bookDeleteDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mBookList.remove(position);
                                dataBank.saveData();
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

            class ShowHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
                //实现广告条目的ViewHolder
                TextView bookName;
                ImageView bookCover;

                public ShowHolder(@NonNull View itemView) {
                    super(itemView);
                    this.bookCover = itemView.findViewById(R.id.image_view_show_book_cover);
                    this.bookName = itemView.findViewById(R.id.text_view_show_book_title);

                    //让Item可以响应创建菜单的点击事件
                    itemView.setOnCreateContextMenuListener(this);
                }

                //注意两个Holder都要有菜单项相关函数，否则Position会有问题
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
                    int position = getAbsoluteAdapterPosition();
                    Intent intent;
                    switch (item.getItemId()) { //item.getItemId表示按的是右键菜单中的哪一项
                        case BOOK_ADD:
                            //跳转到AddBookActivity进行添加
                            intent = new Intent(BookFragment.this.getContext(), AddBookActivity.class);
                            AddActivityResultLauncher.launch(intent);
                            break;
                        case BOOK_EDIT:
                            //跳转到EditBookActivity进行编辑
                            intent = new Intent(BookFragment.this.getContext(), EditBookActivity.class);
                            intent.putExtra("position", position);
                            EditActivityResultLauncher.launch(intent);
                            break;
                        case BOOK_DELETE:
                            //弹出对话框，只有文本提示、确定和取消，可以不写自定义布局
                            AlertDialog.Builder bookDeleteDialog = new AlertDialog.Builder(BookFragment.this.getContext());
                            bookDeleteDialog.setMessage("确定要删除此书籍吗？");
                            bookDeleteDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mBookList.remove(position);
                                    dataBank.saveData();
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
            }
        }
    }
}
