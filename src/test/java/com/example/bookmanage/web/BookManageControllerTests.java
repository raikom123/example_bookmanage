package com.example.bookmanage.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.example.bookmanage.BookmanageApplication;
import com.example.bookmanage.form.BookManageForm;

/**
 * BookManageControllerのテストプログラム
 */
@AutoConfigureMockMvc
@SpringBootTest(classes = BookmanageApplication.class)
public class BookManageControllerTests {

    /**
     * Httpリクエスト・レスポンスを扱うためのMockオブジェクト
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * getリクエストでbooksを指定し、httpステータスとビュー名とモデルに設定されている変数で成否を判定
     * @throws Exception MockMvcのメソッド呼び出し時に発生する
     */
    @Test
    public void books処理のステータスとビューとモデルの確認() throws Exception {
        // getリクエストでbooksを指定する
        MvcResult result = this.mockMvc.perform(get("/books")).andDo(print())
                .andExpect(status().isOk()) // HTTPステータスが200か否か
                .andExpect(view().name("books")) // ビュー名が"books"か否か
                .andReturn();

        // モデルからformを取得する
        BookManageForm form = (BookManageForm)result.getModelAndView().getModel().get("form");

        // 変数を評価する
        assertNull(form.getTitle());
        assertNull(form.getAuthor());
        assertEquals(form.isNewBook(), true);
        assertEquals(form.getVersion(), 0);
        assertNotNull(form.getBooks());
        assertEquals(form.getBooks().size(), 0);
    }

}
