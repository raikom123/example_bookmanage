package com.example.bookmanage.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.bookmanage.exception.BookNotFoundException;
import com.example.bookmanage.form.BookManageForm;
import com.example.bookmanage.service.BookManageService;

import lombok.extern.slf4j.Slf4j;

/**
 * 書籍管理システムのMVCコントローラ
 */
@Slf4j
@Controller
public class BookManageController {

    /**
     * 書籍管理システムのビュー名
     */
    private static final String BOOKS = "books";

    /**
     * リダイレクトのURL
     */
    private static final String REDIRECT_TO = "redirect:/" + BOOKS;

    /**
     * 書籍管理システムのサービス
     */
    private BookManageService service;

    /**
     * メッセージソース
     */
    private MessageSource messageSource;

    /**
     * コンストラクタ
     * 
     * @param service 書籍管理システムのサービス
     */
    @Autowired
    public BookManageController(BookManageService service, MessageSource messageSource) {
        this.service = service;
        this.messageSource = messageSource;
    }

    /**
     * 書籍一覧を読み込む。
     * 
     * @return モデルビュー
     */
    @GetMapping(value = "/books")
    public ModelAndView readBooks() {
        BookManageForm form = service.initForm();
        ModelAndView modelAndView = toBookPages();
        modelAndView.addObject("form", form);
        return modelAndView;
    }

    /**
     * ビュー名を設定したモデルビューを返却する。
     * 
     * @return return ビュー名を設定したモデルビュー
     */
    private ModelAndView toBookPages() {
        return new ModelAndView(BOOKS);
    }

    /**
     * 指定したIDに該当する書籍を読み込む。
     *
     * @param id 書籍のID
     * @return モデルビュー
     * @throws Throwable ビジネス例外以外の例外が発生した場合、throwされる
     */
    @GetMapping(value = "/books/{id}")
    public ModelAndView readOneBook(@PathVariable long id) throws Throwable {
        ModelAndView modelAndView = toBookPages();
        try {
            BookManageForm form = service.readOneBook(id);
            modelAndView.addObject("bookId", id);
            modelAndView.addObject("form", form);
            return modelAndView;
        } catch (Throwable t) {
            return handleException(t);
        }
    }

    /**
     * フォーム情報から書籍を新規登録する。
     *
     * @param form フォーム情報
     * @param result Validatorの結果
     * @return モデルビュー
     * @throws Throwable ビジネス例外以外の例外が発生した場合、throwされる
     */
    @PostMapping(value = "/books")
    public ModelAndView createOneBook(@ModelAttribute BookManageForm form, BindingResult result) throws Throwable {
        try {
            service.createBook(form);
        } catch (Throwable t) {
            return handleException(form, t);
        }
        return new ModelAndView(REDIRECT_TO);
    }

    /**
     * 指定したIDの書籍をフォーム情報の内容に更新する。
     *
     * @param id 書籍のID
     * @param form フォーム情報
     * @param result Validatorの結果
     * @return モデルビュー
     * @throws Throwable ビジネス例外以外の例外が発生した場合、throwされる
     */
    @PutMapping(value = "/books/{id}")
    public ModelAndView updateOneBook(@PathVariable long id, @ModelAttribute BookManageForm form, BindingResult result)
            throws Throwable {
        try {
            service.updateBook(id, form);
        } catch (Exception e) {
            ModelAndView mav = handleException(form, e);
            mav.addObject("bookId", id);
            return mav;
        }
        return new ModelAndView(REDIRECT_TO);
    }

    /**
     * 指定したIDの書籍を削除する。
     *
     * @param id 書籍のID
     * @return モデルビュー
     * @throws Throwable ビジネス例外以外の例外が発生した場合、throwされる
     */
    @DeleteMapping(value = "/books/{id}")
    public ModelAndView deleteOneBook(@PathVariable long id) throws Throwable {
        try {
            service.deleteBook(id);
        } catch (Throwable t) {
            return handleException(t);
        }
        return new ModelAndView(REDIRECT_TO);
    }

    /**
     * 例外を処理する。<br />
     * ビジネス例外の場合、エラーメッセージを設定したモデルビューを返却する。
     *
     * @param t 例外
     * @return モデルビュー
     * @throws Throwable ビジネス例外以外の例外が発生した場合、throwされる
     */
    private ModelAndView handleException(Throwable t) throws Throwable {
        BookManageForm form = new BookManageForm();
        form.setNewBook(true);
        return handleException(form, t);
    }

    /**
     * 例外を処理する。<br />
     * ビジネス例外の場合、エラーメッセージを設定したモデルビューを返却する。
     *
     * @param form フォーム情報
     * @param t 例外
     * @return モデルビュー
     * @throws Throwable ビジネス例外以外の例外が発生した場合、throwされる
     */
    private ModelAndView handleException(BookManageForm form, Throwable t) throws Throwable {
        if (t instanceof BookNotFoundException) {
            // 書籍が取得出来ない場合
            String message = messageSource.getMessage("error.booknotfound", null, null);
            log.warn(message, t);
            return toBookPageForError(form, message);
        } else if (t instanceof ObjectOptimisticLockingFailureException) {
            // 楽観排他でエラーが発生した場合
            String message = messageSource.getMessage("error.optlockfailure", null, null);
            log.warn(message, t);
            return toBookPageForError(form, message);
        }

        throw t;
    }

    /**
     * エラーメッセージを設定したモデルビューを返却する。<br />
     * 書籍一覧の設定も行う。
     *
     * @param form フォーム情報
     * @param errorMessage エラーメッセージ
     * @return モデルビュー
     */
    private ModelAndView toBookPageForError(BookManageForm form, String errorMessage) {
        // 書籍一覧を取得し直す
        BookManageForm initForm = service.initForm();
        form.setBooks(initForm.getBooks());
        ModelAndView modelAndView = toBookPages();
        modelAndView.addObject("form", form);
        modelAndView.addObject("errorMessage", errorMessage);
        return modelAndView;
    }

}
