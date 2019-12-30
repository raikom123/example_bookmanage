package com.example.bookmanage.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.example.bookmanage.BookmanageApplication;
import com.example.bookmanage.domain.Book;
import com.example.bookmanage.exception.BookNotFoundException;
import com.example.bookmanage.form.BookManageForm;
import com.example.bookmanage.service.BookManageService;

/**
 * BookManageControllerのテストプログラム
 */
@SpringBootTest(classes = BookmanageApplication.class)
public class BookManageControllerTests {

    /**
     * テストデータのID
     */
    private static final long TEST_ID = 1;

    /**
     * 不正なテストデータのID
     */
    private static final long INVALID_TEST_ID = 2;

    /**
     * テストデータのタイトル
     */
    private static final String TEST_TITLE = "書籍のタイトル";

    /**
     * テストデータの著者名
     */
    private static final String TEST_AUTHOR = "書籍の著者名";

    /**
     * テストデータのバージョン
     */
    private static final long TEST_VERSION = 2;

    /**
     * テストデータの書籍
     */
    private Book testBook;

    /**
     * 書籍管理システムのController
     */
    @InjectMocks
    private BookManageController controller;

    /**
     * 書籍管理システムのサービス
     */
    @Mock
    private BookManageService service;

    /**
     * メッセージソースのモック
     */
    @Mock
    private MessageSource mockMessageSource;
    
    /**
     * メッセージソース
     */
    @Autowired
    private MessageSource messageSource;

    /**
     * Httpリクエスト・レスポンスを扱うためのMockオブジェクト
     */
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        // テストデータを生成
        testBook = Book.builder()
                .id(TEST_ID)
                .title(TEST_TITLE)
                .author(TEST_AUTHOR)
                .build();
        testBook.setVersion(TEST_VERSION);

        // [Circular view path]の例外が発生するため、ViewResolverを設定する
        String prefix = "/WEB-INF/pages/";
        String suffix = ".html";
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver(prefix, suffix);
        // MVCモックを生成
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new BookManageExceptionHandler())
                .setViewResolvers(viewResolver)
                .alwaysDo(log())
                .build();
    }

    /**
     * 登録データが0件の時にgetリクエストでbooksを指定し、
     * httpステータスとビュー名とモデルに設定されている変数で成否を判定
     * 
     * @throws Exception MockMvcのメソッド呼び出し時に発生する
     */
    @Test
    public void books処理でデータが登録されていない時のステータスとビューとモデルの確認() throws Exception {
        // モックを登録
        BookManageForm initForm = BookManageForm.builder()
                .newBook(true)
                .books(Arrays.asList())
                .build();
        when(service.initForm()).thenReturn(initForm);

        // getリクエストでbooksを指定する
        MvcResult result = this.mockMvc.perform(get("/books")).andDo(print())
                .andExpect(status().isOk()) // HTTPステータスが200か否か
                .andExpect(view().name("books")) // ビュー名が"books"か否か
                .andReturn();

        // モデルからformを取得する
        BookManageForm form = (BookManageForm) result.getModelAndView().getModel().get("form");

        // 変数を評価する
        assertNull(form.getTitle());
        assertNull(form.getAuthor());
        assertEquals(form.isNewBook(), true);
        assertEquals(form.getVersion(), 0);
        assertNotNull(form.getBooks());
        assertEquals(form.getBooks().size(), 0);
    }

    /**
     * 登録データが1件の時にgetリクエストでbooksを指定し、
     * httpステータスとビュー名とモデルに設定されている変数で成否を判定
     * 
     * @throws Exception MockMvcのメソッド呼び出し時に発生する
     */
    @Test
    public void books処理でデータが1件登録されている時のステータスとビューとモデルの確認() throws Exception {
        // モックを登録
        BookManageForm initForm = BookManageForm.builder()
                .newBook(true)
                .books(Arrays.asList(testBook))
                .build();
        when(service.initForm()).thenReturn(initForm);

        // getリクエストでbooksを指定する
        MvcResult result = this.mockMvc.perform(get("/books")).andDo(print())
                .andExpect(status().isOk()) // HTTPステータスが200か否か
                .andExpect(view().name("books")) // ビュー名が"books"か否か
                .andReturn();

        // モデルからformを取得する
        BookManageForm form = (BookManageForm) result.getModelAndView().getModel().get("form");

        // 変数を評価する
        assertNull(form.getTitle());
        assertNull(form.getAuthor());
        assertEquals(form.isNewBook(), true);
        assertEquals(form.getVersion(), 0);
        assertNotNull(form.getBooks());
        assertEquals(form.getBooks().size(), 1);
    }

    /**
     * getリクエストでbooks/{id}を指定し、存在しないidを指定した時のhttpステータスとビュー名とモデルに設定されている変数で成否を判定
     * 
     * @throws Exception MockMvcのメソッド呼び出し時に発生する
     */
    @Test
    public void books_id処理で存在するidを指定した時のステータスとビューとモデルの確認() throws Exception {
        // モックを登録
        BookManageForm readOneForm = BookManageForm.builder()
                .title(TEST_TITLE)
                .author(TEST_AUTHOR)
                .newBook(false)
                .version(TEST_VERSION)
                .books(Arrays.asList(testBook))
                .build();
        when(service.readOneBook(TEST_ID)).thenReturn(readOneForm);

        // getリクエストでbooks/{id}を指定する
        MvcResult result = mockMvc.perform(get("/books/1")).andDo(print())
                .andExpect(status().isOk()) // HTTPステータスが200か否か
                .andExpect(view().name("books")) // ビュー名が"books"か否か
                .andReturn();

        // モデルからformを取得する
        BookManageForm form = (BookManageForm) result.getModelAndView().getModel().get("form");

        // 変数を評価する
        assertEquals(form.getTitle(), TEST_TITLE);
        assertEquals(form.getAuthor(), TEST_AUTHOR);
        assertEquals(form.isNewBook(), false);
        assertEquals(form.getVersion(), TEST_VERSION);
        assertNotNull(form.getBooks());
        assertEquals(form.getBooks().size(), 1);

        // モデルからメッセージを取得し、リソースファイルのメッセージと同じか評価する
        String message = (String) result.getModelAndView().getModel().get("errorMessage");
        assertNull(message);
    }

    /**
     * getリクエストでbooks/{id}を指定し、存在しないidを指定した時のhttpステータスとビュー名とモデルに設定されている変数で成否を判定
     * 
     * @throws Exception MockMvcのメソッド呼び出し時に発生する
     */
    @Test
    public void books_id処理で存在しないidを指定した時のステータスとビューとモデルの確認() throws Exception {
        String actualMessage = messageSource.getMessage("error.booknotfound", null, null);

        // モックを登録
        when(service.readOneBook(INVALID_TEST_ID)).thenThrow(new BookNotFoundException(INVALID_TEST_ID));
        BookManageForm initForm = BookManageForm.builder()
                .newBook(true)
                .books(Arrays.asList(testBook))
                .build();
        when(service.initForm()).thenReturn(initForm);
        when(mockMessageSource.getMessage("error.booknotfound", null, null)).thenReturn(actualMessage);

        // getリクエストでbooks/{id}を指定する
        MvcResult result = mockMvc.perform(get("/books/2")).andDo(print())
                .andExpect(status().isOk()) // HTTPステータスが200か否か
                .andExpect(view().name("books")) // ビュー名が"books"か否か
                .andReturn();

        // モデルからformを取得する
        BookManageForm form = (BookManageForm) result.getModelAndView().getModel().get("form");

        // 変数を評価する
        assertNull(form.getTitle());
        assertNull(form.getAuthor());
        assertEquals(form.isNewBook(), true);
        assertEquals(form.getVersion(), 0);
        assertNotNull(form.getBooks());
        assertEquals(form.getBooks().size(), 1);

        // モデルからメッセージを取得し、リソースファイルのメッセージと同じか評価する
        String message = (String) result.getModelAndView().getModel().get("errorMessage");
        assertEquals(message, actualMessage);
    }

}
