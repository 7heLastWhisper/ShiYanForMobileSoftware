package com.jnu.booklistapplication;

import java.util.ArrayList;
import java.util.List;

public class BookList {
    public static class Book {
        private String bookName;
        private int bookImageId;
        private int bookType;

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

        public void setTitle(String title) {
            this.bookName = title;
        }
    }

    public static List<Book> getListBooks() {
        return new ArrayList<>();
    }
}
