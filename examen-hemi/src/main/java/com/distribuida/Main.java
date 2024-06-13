package com.distribuida;

import com.distribuida.db.Book;
import com.distribuida.service.BookService;
import com.google.gson.Gson;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;
import jakarta.enterprise.inject.spi.CDI;
import org.apache.webbeans.config.WebBeansContext;
import org.apache.webbeans.spi.ContainerLifecycle;

public class Main {

    static BookService service;
    private static ContainerLifecycle lifecycle = null;
    static Gson gson = new Gson();

    public static void apagarContenedorCDI() {
        lifecycle.stopApplication(null);
    }

    static void unoLibro(ServerRequest rq, ServerResponse res) {
        res.send(gson.toJson(service.buscarPorId(
                Integer.valueOf(rq.path().pathParameters().get("id"))
        )));
    }

    static void todosLibros(ServerRequest rq, ServerResponse res) {
        res.send(gson.toJson(service.buscarTodos()));
    }

    static void crearLibro(ServerRequest rq, ServerResponse res) {
        res.send(gson.toJson(
                service.crear(
                        gson.fromJson(
                                rq.content().as(String.class), Book.class
                        )
                )
        ));
    }

    static void actualizarLibro(ServerRequest rq, ServerResponse res) {
        var book = gson.fromJson(
                rq.content().as(String.class), Book.class
        );

        book.setId(Integer.valueOf(rq.path().pathParameters().get("id")));

        res.send(gson.toJson(
                service.actualizar(book)
        ));
    }

    static void eliminarLibro(ServerRequest rq, ServerResponse res) {
        service.eliminar(Integer.valueOf(rq.path().pathParameters().get("id")));
        res.send("");
    }

    private static void iniciarContenedorCDI() {
        lifecycle = WebBeansContext.currentInstance().getService(ContainerLifecycle.class);
        lifecycle.startApplication(null);
    }

    private static BookService obtenerServicioBook() {
        return CDI.current().select(BookService.class).get();
    }

    private static void iniciarServidorWeb() {
        WebServer server = WebServer.builder()
                .port(8080)
                .routing(builder -> builder
                        .get("/book/{id}", Main::unoLibro)
                        .get("/book", Main::todosLibros)
                        .post("/book", Main::crearLibro)
                        .put("/book/{id}", Main::actualizarLibro)
                        .delete("/book/{id}", Main::eliminarLibro)
                )
                .build();
        server.start();
    }

    private static void imprimirTodosLosLibros() {
        service.buscarTodos().stream().forEach(System.out::println);
    }

    public static void main(String[] args) {
        iniciarContenedorCDI();

        service = obtenerServicioBook();

        iniciarServidorWeb();

        imprimirTodosLosLibros();

        apagarContenedorCDI();
    }

}