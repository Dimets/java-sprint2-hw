package taskengine;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String url;
    private String API_TOKEN = "";

    public KVTaskClient(String url) {
        this.url = url;

        try {
            URI uri = URI.create(url + "/register");
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
            HttpRequest httpRequest = requestBuilder
                    .GET()
                    .uri(uri)
                    .build();
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse.BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, bodyHandler);

            if (httpResponse.statusCode() == 200) {
                this.API_TOKEN = httpResponse.body();
            } else {
                System.out.println("Что-то пошло не так...");
                System.out.println(httpResponse);
                System.out.println(httpResponse.body());
            }

            System.out.println("KVTaskClient has registered with API_TOKEN = " + API_TOKEN);
        } catch  (InterruptedException | IOException e) {
            System.out.println("Во время выполнения запроса возникла ошибка." +
                    " Проверьте, пожалуйста, URL-адрес и повторите попытку");
        }
    }

    public void put(String key, String json)  {
        //POST /save/<key>?API_TOKEN=
        try {
            URI uri = URI.create(url + "/save/" + key + "?API_TOKEN=" + API_TOKEN);
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
            HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(json);
            HttpRequest httpRequest = requestBuilder
                    .POST(bodyPublisher)
                    .uri(uri)
                    .build();
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse.BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, bodyHandler);
            System.out.println(httpResponse);
        } catch (InterruptedException | IOException e) {
            System.out.println("Во время выполнения запроса возникла ошибка." +
                    " Проверьте, пожалуйста, URL-адрес и повторите попытку");
        }
    }

    public String load(String key) throws IOException, InterruptedException {
        //GET /load/<key>?API_TOKEN=
        URI uri = URI.create(url + "/load/" + key + "?API_TOKEN=" + API_TOKEN);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest httpRequest = requestBuilder
                .GET()
                .uri(uri)
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, bodyHandler);

        return httpResponse.body();
    }
}
