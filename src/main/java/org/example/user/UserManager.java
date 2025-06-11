package org.example.user;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public final class UserManager {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final String LOGIN_ENDPOINT = "login";
    private static final String REGISTER_ENDPOINT = "register";
    private static final String USER_FORM = """
            {
                "nick": "%s",
                "password": "%s"
            }""";

    private UserManager() {}

    public static int tryLogin(String username, String password, String host) throws IOException, InterruptedException {
        return sendRequest(username, password, host, LOGIN_ENDPOINT);
    }

    public static int tryRegister(String username, String password, String host) throws IOException, InterruptedException {
        return sendRequest(username, password, host, REGISTER_ENDPOINT);
    }

    private static int sendRequest(String username, String password, String host, String endpoint) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://%s:8000/%s".formatted(host, endpoint)))
                .POST(HttpRequest.BodyPublishers.ofString(USER_FORM.formatted(username, password)))
                .build();
        HttpResponse<String> resp = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        return resp.statusCode();
    }

}
