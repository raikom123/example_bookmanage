package com.example.bookmanage.exception;

import lombok.Getter;

/**
 * 
 */
@SuppressWarnings("serial")
@Getter
public class BookNotFoundException extends RuntimeException {

    /**
     * 書籍のID
     */
    private long id;

    /**
     * コンストラクタ
     * 
     * @param id 書籍のID
     */
    public BookNotFoundException(long id) {
        this.id = id;
    }

}
