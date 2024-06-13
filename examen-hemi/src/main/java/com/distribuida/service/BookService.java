package com.distribuida.service;

import com.distribuida.db.Book;

import java.util.List;

public interface BookService {

    Book buscarPorId(Integer id);

    List<Book> buscarTodos();

    Book crear(Book book);

    Book actualizar(Book book);

    void eliminar(Integer id);
}

