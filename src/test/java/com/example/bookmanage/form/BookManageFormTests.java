package com.example.bookmanage.form;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

/**
 * 書籍管理システムのフォーム情報のテストプログラム
 */
@SpringBootTest
class BookManageFormTests {

    /**
     * タイトルの正常なデータ
     */
    private static final String TITLE_SUCCESS = "123456789012345678901234567890";

    /**
     * 著者の正常なデータ
     */
    private static final String AUTHOR_SUCCESS = "１２３４５６７８９０１２３４５６７８９０";

    /**
     * タイトルの空入力エラー用のデータ
     */
    private static final String TITLE_ERROR_NOT_EMPTY = "";

    /**
     * 著者の空入力エラー用のデータ
     */
    private static final String AUTHOR_ERROR_NOT_EMPTY = "";

    /**
     * タイトルの桁の最大値を超えるエラー用のデータ
     */
    private static final String TITLE_ERROR_MAX_SIZE = "1234567890123456789012345678901";

    /**
     * 著者の桁の最大値を超えるエラー用のデータ
     */
    private static final String AUTHOR_ERROR_MAX_SIZE = "１２３４５６７８９０１２３４５６７８９０１";

    /**
     * メッセージリソース
     */
    private static ReloadableResourceBundleMessageSource messageSource;

    /**
     * Validator
     */
    @Autowired
    private Validator validator;

    /**
     * フォーム情報
     */
    private BookManageForm form = new BookManageForm();

    /**
     * BindingResult
     */
    private BindingResult result = new BindException(form, "bookManageForm");

    @BeforeAll
    static void setUpBeforeClass() {
        messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:ValidationMessages");
        messageSource.setDefaultEncoding("UTF-8");
    }

    @BeforeEach
    void setUp() throws Exception {
        // 正常な値を設定する
        form.setTitle(TITLE_SUCCESS);
        form.setAuthor(AUTHOR_SUCCESS);
    }

    @AfterAll
    static void tearDownAfterClass() {
        messageSource = null;
    }

    @Test
    void 正常なケース() {
        validator.validate(form, result);
        assertNull(result.getFieldError());
    }

    @Test
    void タイトルが空の場合() {
        // 想定されるメッセージをリソースから取得する
        String actualMessage = messageSource.getMessage("javax.validation.constraints.NotBlank.message", null, null);

        // 不正な値を設定し、チェックを行う
        form.setTitle(TITLE_ERROR_NOT_EMPTY);
        validator.validate(form, result);

        // エラーが発生したフィールドとメッセージを確認する
        assertEquals(result.getFieldError().getField(), "title");
        assertEquals(result.getFieldError().getDefaultMessage(), actualMessage);
    }

    void タイトルの文字数が最大を超えている場合() {
        // 想定されるメッセージをリソースから取得する
        String actualMessage = messageSource.getMessage("validation.max-size", null, null);

        // 不正な値を設定し、チェックを行う
        form.setTitle(TITLE_ERROR_MAX_SIZE);
        validator.validate(form, result);

        // エラーが発生したフィールドとメッセージを確認する
        assertEquals(result.getFieldError().getField(), "title");
        assertEquals(result.getFieldError().getDefaultMessage(), actualMessage);
    }

    @Test
    void 著者が空の場合() {
        // 想定されるメッセージをリソースから取得する
        String actualMessage = messageSource.getMessage("javax.validation.constraints.NotBlank.message", null, null);

        // 不正な値を設定し、チェックを行う
        form.setAuthor(AUTHOR_ERROR_NOT_EMPTY);
        validator.validate(form, result);

        // エラーが発生したフィールドとメッセージを確認する
        assertEquals(result.getFieldError().getField(), "author");
        assertEquals(result.getFieldError().getDefaultMessage(), actualMessage);
    }

    void 著者の文字数が最大を超えている場合() {
        // 想定されるメッセージをリソースから取得する
        String actualMessage = messageSource.getMessage("validation.max-size", null, null);

        // 不正な値を設定し、チェックを行う
        form.setAuthor(AUTHOR_ERROR_MAX_SIZE);
        validator.validate(form, result);

        // エラーが発生したフィールドとメッセージを確認する
        assertEquals(result.getFieldError().getField(), "author");
        assertEquals(result.getFieldError().getDefaultMessage(), actualMessage);
    }

}
