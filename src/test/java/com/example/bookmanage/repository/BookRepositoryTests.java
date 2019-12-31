package com.example.bookmanage.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.bookmanage.domain.Book;

/**
 * BookRepositoryのテストプログラム<br />
 * 主に自動設定される変数の確認を行う。
 */
@SpringBootTest
class BookRepositoryTests {

    /**
     * テストデータのタイトル(新規作成時)
     */
    private static final String TEST_TITLE_NEW = "testタイトル";

    /**
     * テストデータのタイトル(更新時)
     */
    private static final String TEST_TITLE_UPD = "testタイトル(更新)";

    /**
     * テストデータの著者名(新規登録時)
     */
    private static final String TEST_AUTHOR_NEW = "test著者名";

    /**
     * テストデータの著者名(更新時)
     */
    private static final String TEST_AUTHOR_UPD = "test著者名(更新)";

    /**
     * Long値の初期値
     */
    private static final long DEFAULT_LONG_VALUE = 0;

    /**
     * versionの初期値
     */
    private static final long FIRST_VERSION = 0;

    /**
     * 書籍のリポジトリ
     */
    @Autowired
    private BookRepository repository;

    @Test
    void 新規登録時にエンティティに設定した変数と自動設定される変数が設定されることの確認() {
        // 書籍を新規登録
        Book newBook = insertBook();

        // エンティティに設定した変数を検証
        assertEquals(newBook.getTitle(), TEST_TITLE_NEW);
        assertEquals(newBook.getAuthor(), TEST_AUTHOR_NEW);
        // 自動設定される変数を検証
        assertNotEquals(newBook.getId(), DEFAULT_LONG_VALUE);
        assertEquals(newBook.getVersion(), FIRST_VERSION);
        assertNotNull(newBook.getCreatedDateTime());
        assertNotNull(newBook.getUpdatedDateTime());
    }

    @Test
    void 更新時にエンティティに設定した変数と自動設定される変数が更新されることの確認() {
        // 書籍を新規登録
        Book newBook = insertBook();

        // 書籍の内容を更新
        Book book = new ModelMapper().map(newBook, Book.class);
        book.setTitle(TEST_TITLE_UPD);
        book.setAuthor(TEST_AUTHOR_UPD);
        Book updBook = repository.saveAndFlush(book);

        // エンティティに設定した変数を検証
        assertEquals(updBook.getTitle(), TEST_TITLE_UPD);
        assertEquals(updBook.getAuthor(), TEST_AUTHOR_UPD);
        // 自動設定される変数を検証
        assertEquals(updBook.getId(), newBook.getId());
        assertNotEquals(updBook.getVersion(), newBook.getVersion());
        assertEquals(updBook.getCreatedDateTime(), newBook.getCreatedDateTime());
        assertNotEquals(updBook.getUpdatedDateTime(), newBook.getUpdatedDateTime());
    }

    /**
     * 書籍を新規登録する
     * 
     * @return 書籍
     */
    private Book insertBook() {
        // テストデータ生成
        Book book = Book.builder()
                .title(TEST_TITLE_NEW)
                .author(TEST_AUTHOR_NEW)
                .build();

        // DBに新規登録
        return repository.saveAndFlush(book);
    }

}
