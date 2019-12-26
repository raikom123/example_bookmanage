package com.example.bookmanage.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bookmanage.domain.Book;
import com.example.bookmanage.exception.BookNotFoundException;
import com.example.bookmanage.form.BookManageForm;
import com.example.bookmanage.repository.BookRepository;

@Service
public class BookManageService {

    private BookRepository bookRepository;

    /**
     * コンストラクタ
     * 
     * @param bookRepository 
     */
    @Autowired
    public BookManageService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public BookManageForm initBooks() {
        List<Book> books = bookRepository.findAll();
        BookManageForm form = new BookManageForm(true, books);
        return form;
    }

    @Transactional(readOnly = true)
    public BookManageForm readOneBook(long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));

        List<Book> books = bookRepository.findAll();
        BookManageForm form = new BookManageForm(false, books);

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(book, form);

        return form;
    }

    @Transactional(readOnly = false)
    public void updateBook(long id, BookManageForm form) {
        // Entityを取得
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));

        // 楽観排他
        if (book.getVersion() != form.getVersion()) {
            throw new ObjectOptimisticLockingFailureException(Book.class, id);
        }

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(form, book);
        bookRepository.save(book);
    }

    @Transactional(readOnly = false)
    public void createBook(BookManageForm form) {
        ModelMapper modelMapper = new ModelMapper();
        Book book = modelMapper.map(form, Book.class);
        bookRepository.save(book);
    }

    @Transactional(readOnly = false)
    public void deleteBook(long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
        } else {
            throw new BookNotFoundException(id);
        }
    }

}
