package com.example.bookmanage.form;

import java.util.List;

import com.example.bookmanage.domain.Book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookManageForm {

    /**
     * タイトル
     */
    private String title;

    /**
     * 著者
     */
    private String author;

    /**
     * 新規登録か否か
     */
    private boolean newBook;
    
    /**
     * バージョン
     */
    private long version;

    /**
     * 書籍の一覧
     */
    private List<Book> books;

    /**
     * コンストラクタ
     * 
     * @param newBook 新規登録か否か
     * @param books 書籍の一覧
     */
    public BookManageForm(boolean newBook, List<Book> books) {
        this.newBook = newBook;
        this.books = books;
    }

}
