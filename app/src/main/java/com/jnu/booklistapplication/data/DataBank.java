package com.jnu.booklistapplication.data;

import android.content.Context;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DataBank {

    public static final String DATA_FILE_NAME = "data";
    private final Context context;
    List<BookList.Book> bookList;

    public DataBank(Context context) {

        this.context = context;
    }

    // 从文件中读数据
    public List<BookList.Book> loadData() {

        bookList = new ArrayList<>();

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(context.openFileInput(DATA_FILE_NAME));
            bookList = (ArrayList<BookList.Book>) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookList;
    }

    // 数据保存到文件
    public void saveData() {

        ObjectOutputStream objectOutputStream = null;

        try {
            objectOutputStream = new ObjectOutputStream(context.openFileOutput(DATA_FILE_NAME, Context.MODE_PRIVATE));
            objectOutputStream.writeObject(bookList);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { // 关闭输出流
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}