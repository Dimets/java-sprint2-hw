package test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskengine.HTTPTaskManager;
import taskengine.TaskManager;
import utils.KVServer;
import utils.Managers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class HTTPTaskManagerTest extends TaskManagerTest<HTTPTaskManager> {
    private KVServer kvServer;
    private String API_TOKEN;
    private Gson gson;

    @BeforeEach
    void setUp() throws IOException {
        gson = new Gson();
        kvServer = new KVServer();
        kvServer.start();
        taskManager = (HTTPTaskManager) Managers.getDefault();

        // создаём экземпляр URI, содержащий адрес нужного ресурса

        try {
            URI uri = URI.create("http://localhost:8078/register");
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
            HttpRequest httpRequest = requestBuilder
                    .GET()
                    .uri(uri)
                    .build();
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse.BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, bodyHandler);

            if (httpResponse.statusCode() == 200) {
                API_TOKEN = httpResponse.body();
            } else {
                System.out.println("Что-то пошло не так...");
                System.out.println(httpResponse);
                System.out.println(httpResponse.body());
            }

            System.out.println("Test HttpClient has registered with API_TOKEN = " + API_TOKEN);
        } catch  (InterruptedException | IOException e) {
            System.out.println("Во время выполнения запроса возникла ошибка." +
                    " Проверьте, пожалуйста, URL-адрес и повторите попытку");
        }

    }

    @AfterEach
    void upSet() {
        kvServer.stop();
    }

    @Test
    void shouldSave() throws IOException, InterruptedException {

        taskManager.createTask(new Task("Task First Name","Task First Description",111, TaskStatus.NEW, LocalDateTime.now(), Duration.ofDays(1)));
        //taskManager.createTask(new Task("Task Second Name","Task Second Description",222, TaskStatus.NEW, LocalDateTime.now().plusDays(5), Duration.ofDays(1)));

        URI uri = URI.create("http://localhost:8078/load/tasks?API_TOKEN=" + API_TOKEN);
        // создаём объект, описывающий HTTP-запрос
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .GET()    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос

        // HTTP-клиент с настройками по умолчанию
        HttpClient client = HttpClient.newHttpClient();

        // получаем стандартный обработчик тела запроса с конвертацией содержимого в строку
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        // отправляем запрос и получаем ответ от сервера
        HttpResponse<String> response = client.send(request, handler);

        Assertions.assertEquals(200, response.statusCode(), "Неверный код ответа");

        String taskString = gson.toJson(taskManager.getTaskById(111));

        JsonElement jsonElement = JsonParser.parseString(response.body()).getAsJsonObject().get("111");

        Assertions.assertEquals(taskString,gson.toJson(jsonElement),"Сохраненное значение неверно");

    }

    @Test
    void shouldLoad() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8078/save/tasks?API_TOKEN=" + API_TOKEN);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        Task task = new Task("Task Name",
                "Task Description",111, TaskStatus.NEW, LocalDateTime.now(),
                Duration.ofDays(1));
        Map<Integer, Task> tasksMap = new HashMap<>();
        tasksMap.put(111,task);
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(gson.toJson(tasksMap));
        HttpRequest httpRequest = requestBuilder
                .POST(bodyPublisher)
                .uri(uri)
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, bodyHandler);

        TaskManager taskManager = HTTPTaskManager.load();

        Assertions.assertEquals(task, taskManager.getTaskById(111), "Восттановленное значение некорректно");

    }

}
