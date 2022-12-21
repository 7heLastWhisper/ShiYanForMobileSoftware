package com.jnu.booklistapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BookListMainActivity {
    public static class Book {
        private final String bookName;
        private final int bookImageId;
        private final int bookType;

        public Book(String text, int imageId, int type) {
            this.bookImageId = imageId;
            this.bookName = text;
            this.bookType = type;
        }

        public String getTitle() {
            return bookName;
        }

        public int getCoverResourceId() {
            return bookImageId;
        }

        public int getBookType() {
            return bookType;
        }
    }

    public static List<Book> getListBooks() {
        return new ArrayList<Book>();
    }

    public static class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final List<Book> mBookList;

        public BookAdapter(List<Book> bookList) {
            this.mBookList = bookList;
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
                return new ShowHolder(itemView);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof BookHolder) {
                BookHolder viewHolder = (BookHolder) holder;
                //为控件设置对应的资源
                viewHolder.bookName.setText(mBookList.get(position).getTitle());
                viewHolder.bookCover.setBackgroundResource(mBookList.get(position).getCoverResourceId());
            } else if (holder instanceof ShowHolder) {
                ShowHolder viewHolder = (ShowHolder) holder;
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

        static class BookHolder extends RecyclerView.ViewHolder {
            //实现普通条目的ViewHolder
            TextView bookName;
            ImageView bookCover;

            public BookHolder(@NonNull View itemView) {
                super(itemView);
                this.bookCover = itemView.findViewById(R.id.image_view_common_book_cover);
                this.bookName = itemView.findViewById(R.id.text_view_common_book_title);
            }
        }

        static class ShowHolder extends RecyclerView.ViewHolder {
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