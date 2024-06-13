package com.distribuida.service;

import com.distribuida.db.Book;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

@ApplicationScoped
public class BookServiceImpl implements BookService {

    @Inject
    private EntityManager entityManager;

    @Override
    public Book buscarPorId(Integer id) {
        return this.entityManager.find(Book.class, id);
    }

    @Override
    public List<Book> buscarTodos() {
        return this.entityManager.createQuery("SELECT b FROM Book b ORDER BY b.id ASC", Book.class).getResultList();
    }

    @Override
    public Book crear(Book book) {
        EntityTransaction transaction = this.entityManager.getTransaction();

        try {
            transaction.begin();
            this.entityManager.persist(book);
            transaction.commit();
            return book;
        } catch (Exception e) {
            transaction.rollback();
            return null;
        }
    }

    @Override
    public Book actualizar(Book book) {
        EntityTransaction transaction = this.entityManager.getTransaction();

        try {
            transaction.begin();
            this.entityManager.merge(book);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }

        return book;
    }

    @Override
    public void eliminar(Integer id) {
        EntityTransaction transaction = this.entityManager.getTransaction();

        try {
            transaction.begin();
            this.entityManager.remove(this.buscarPorId(id));
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }
}
