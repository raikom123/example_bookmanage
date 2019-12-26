package com.example.bookmanage.web;

import org.springframework.beans.factory.annotation.Autowired;
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

@Slf4j
@Controller
public class BookManageController {

    static final String BOOKS = "books";

    private static final String REDIRECT_TO = "redirect:/" + BOOKS;

    private BookManageService service;

    @Autowired
    public BookManageController(BookManageService service) {
        this.service = service;
    }

    @GetMapping(value = "/books")
    public ModelAndView readBooks() {
        BookManageForm form = service.initBooks();
        ModelAndView modelAndView = toTaskPages();
        modelAndView.addObject("form", form);
        return modelAndView;
    }

    /**
     * ビュー名を設定したモデルビューを返却する。
     * 
     * @return return ビュー名を設定したモデルビュー
     */
    private ModelAndView toTaskPages() {
        return new ModelAndView(BOOKS);
    }

    @GetMapping(value = "/books/{id}")
    public ModelAndView readOneBook(@PathVariable long id) throws Exception {
        ModelAndView modelAndView = toTaskPages();
        try {
            BookManageForm form = service.readOneBook(id);
            modelAndView.addObject("bookId", id);
            modelAndView.addObject("form", form);
            return modelAndView;
        } catch (Exception e) {
            BookManageForm form = new BookManageForm();
            return handleException(form, e);
        }
    }

    @PostMapping(value = "/books")
    public ModelAndView createOneBook(@ModelAttribute BookManageForm form, BindingResult result) throws Exception {
        try {
            service.createBook(form);
        } catch (Exception e) {
            return handleException(form, e);
        }
        return new ModelAndView(REDIRECT_TO);
    }

    @PutMapping(value = "/books/{id}")
    public ModelAndView updateOneBook(@PathVariable long id, @ModelAttribute BookManageForm form, BindingResult result) throws Exception {
        try {
            service.updateBook(id, form);
        } catch (Exception e) {
            ModelAndView mav = handleException(form, e);
            mav.addObject("bookId", id);
            return mav;
        }
        return new ModelAndView(REDIRECT_TO);
    }

    @DeleteMapping(value = "/books/{id}")
    public ModelAndView deleteOneBook(@PathVariable long id) throws Exception {
        try {
            service.deleteBook(id);
        } catch (Exception e) {
            BookManageForm form = new BookManageForm();
            return handleException(form, e);
        }
        return new ModelAndView(REDIRECT_TO);
    }
    
    private ModelAndView handleException(BookManageForm form, Exception e) throws Exception {
        if (e instanceof BookNotFoundException) {
            log.warn("データが存在しないエラー", e);
            // 書籍が取得出来ない場合
            return handleException(form, "書籍が存在しません。");
        } else if (e instanceof ObjectOptimisticLockingFailureException) {
            log.warn("楽観排他エラー", e);
            // 楽観排他でエラーが発生した場合
            return handleException(form, "他のユーザによって書籍が更新されました。");
        }
        
        throw e;
    }
    
    private ModelAndView handleException(BookManageForm form, String errorMessage) {
        // 一覧を取得し直す
        BookManageForm initForm = service.initBooks();
        form.setBooks(initForm.getBooks());
        ModelAndView modelAndView = toTaskPages();
        modelAndView.addObject("form", form);
        modelAndView.addObject("message", errorMessage);
        return modelAndView;
    }

}
