package utils;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import model.Epic;
import model.SubTask;
import model.Task;
import taskengine.ManagerReadException;
import taskengine.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HttpTaskServer {
    private static HttpServer httpServer;
    private static TaskManager taskManager;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private static final Gson gson = new Gson();

    public HttpTaskServer(TaskManager taskManager) {
        try {
            this.httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
            this.taskManager = taskManager;

            httpServer.createContext("/tasks/task", new TaskHandler());
            httpServer.createContext("/tasks/subtask", new SubTaskHandler());
            httpServer.createContext("/tasks/epic", new EpicHandler());
            httpServer.createContext("/tasks/history", new HistoryHandler());
            httpServer.createContext("/tasks/", new PrioritizedTaskHandler());
            httpServer.start();
        } catch (IOException e) {
            throw new HttpTaskServerRunException("Не удалось запустить сервер");
        }
    }

    static class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            URI requestURI = httpExchange.getRequestURI();

            int responseCode;
            String response = "";

            switch (method) {
                case "GET":
                    if (requestURI.getQuery()  == null ) {
                        //GET /tasks/task/
                        response = gson.toJson(taskManager.getTasks());
                        responseCode = 200;
                    } else if (requestURI.getQuery().startsWith("id=")) {
                        //GET /tasks/task/?id=
                        int id = Integer.parseInt(requestURI.getQuery().substring(3));
                        if (taskManager.getTaskById(id) == null) {
                            responseCode = 404;
                        } else {
                            responseCode = 200;
                            response = gson.toJson(taskManager.getTaskById(id));
                        }
                    } else {
                        responseCode = 400;
                    }
                    break;
                case "POST":
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                    Task task = gson.fromJson(body, Task.class);
                    taskManager.createTask(task);
                    responseCode = 201;
                    break;
                case "DELETE":
                    if (requestURI.getQuery() == null) {
                        taskManager.deleteAllTasks();
                        responseCode = 200;
                    } else if (requestURI.getQuery().startsWith("id=")) {
                        int id = Integer.parseInt(requestURI.getQuery().substring(3));
                        taskManager.deleteTaskById(id);
                        responseCode = 200;
                    } else {
                        responseCode = 400;
                    }
                    break;
                default:
                    response = "Unsupported method";
                    responseCode = 405;
            }
            httpExchange.sendResponseHeaders(responseCode, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    static class SubTaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            URI requestURI = httpExchange.getRequestURI();

            int responseCode;
            String response = "";

            switch (method) {
                case "GET":
                    if (requestURI.getQuery()  == null ) {
                        //GET /tasks/subtask/
                        response = gson.toJson(taskManager.getSubTasks());
                        responseCode = 200;
                    } else if (requestURI.getQuery().startsWith("id=")) {
                        //GET /tasks/subtask/?id=
                        int id = Integer.parseInt(requestURI.getQuery().substring(3));
                        if (taskManager.getSubTaskById(id) == null) {
                            responseCode = 404;
                        } else {
                            responseCode = 200;
                            response = gson.toJson(taskManager.getSubTaskById(id));
                        }
                    } else {
                        responseCode = 400;
                    }
                    break;
                case "POST":
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                    SubTask subTask = gson.fromJson(body, SubTask.class);
                    taskManager.createSubTask(subTask);
                    responseCode = 201;
                    break;
                case "DELETE":
                    if (requestURI.getQuery() == null) {
                        taskManager.deleteAllSubTasks();
                        responseCode = 200;
                    } else if (requestURI.getQuery().startsWith("id=")) {
                        int id = Integer.parseInt(requestURI.getQuery().substring(3));
                        taskManager.deleteSubTaskById(id);
                        responseCode = 200;
                    } else {
                        responseCode = 400;
                    }
                    break;
                default:
                    response = "Unsupported method";
                    responseCode = 405;
            }
            httpExchange.sendResponseHeaders(responseCode, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }

        }
    }

    static class EpicHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            URI requestURI = httpExchange.getRequestURI();

            int responseCode;
            String response = "";

            switch (method) {
                case "GET":
                    if (requestURI.getQuery()  == null ) {
                        //GET /tasks/epic/
                        response = gson.toJson(taskManager.getEpics());
                        responseCode = 200;
                    } else if (requestURI.getQuery().startsWith("id=")) {
                        //GET /tasks/epic/?id=
                        int id = Integer.parseInt(requestURI.getQuery().substring(3));
                        if (taskManager.getEpicById(id) == null) {
                            responseCode = 404;
                        } else {
                            responseCode = 200;
                            response = gson.toJson(taskManager.getEpicById(id));
                        }
                    } else {
                        responseCode = 400;
                    }
                    break;
                case "POST":
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                    Epic epic = gson.fromJson(body, Epic.class);
                    taskManager.createEpic(epic);
                    responseCode = 201;
                    break;
                case "DELETE":
                    if (requestURI.getQuery() == null) {
                        taskManager.deleteAllEpics();
                        responseCode = 200;
                    } else if (requestURI.getQuery().startsWith("id=")) {
                        int id = Integer.parseInt(requestURI.getQuery().substring(3));
                        taskManager.deleteEpicById(id);
                        responseCode = 200;
                    } else {
                        responseCode = 400;
                    }
                    break;
                default:
                    response = "Unsupported method";
                    responseCode = 405;
            }
            httpExchange.sendResponseHeaders(responseCode, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }

        }
    }

    static class HistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            //System.out.println("HistoryHandler");
            String method = httpExchange.getRequestMethod();
            int responseCode;
            String response;

            if ("GET".equals(method)) {//GET /tasks/history
                responseCode = 200;
                response = gson.toJson(taskManager.history());
            } else {
                response = "Unsupported method";
                responseCode = 400;
            }

            httpExchange.sendResponseHeaders(responseCode, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    static class PrioritizedTaskHandler implements  HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            //System.out.println("PrioritizedTaskHandler(");
            String method = httpExchange.getRequestMethod();
            int responseCode;
            String response;

            if ("GET".equals(method)) {//GET /tasks
                responseCode = 200;
                response = gson.toJson(taskManager.getPrioritizedTasks());
            } else {
                response = "Unsupported method";
                responseCode = 400;
            }

            httpExchange.sendResponseHeaders(responseCode, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }

        }
    }
}
