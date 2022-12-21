package com.jnu.booklistapplication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BookList implements Serializable {
    private static final long serialVersionUID = 123;

    public static class Book implements Serializable {
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
