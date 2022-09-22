package com.taf.testautomation.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.FileWriter;
import java.io.Writer;

public class RestServices {

    public static JsonElement getJson(String url, String userName, String password, String saveAsPath) throws Exception {
        WebClient webClient = getWebClient(url);
        String responseString = webClient.get().headers(header -> header.setBasicAuth(userName, password)).accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(String.class).block();

        JsonParser jsonParser = new JsonParser();
        JsonElement response = jsonParser.parse(responseString);

        saveJSON(saveAsPath, response);

        return response;
    }

    public static WebClient getWebClient(String url) {
        return WebClient.builder().baseUrl(url).build();
    }

    public static void saveJSON(String path, JsonElement response) throws Exception {
        try (Writer writer = new FileWriter(path)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
            gson.toJson(response, writer);
        }
    }

    public static JsonElement postJson(String url, JsonElement body, String appKeyHeader, String appKey, String saveAsPath) {
        Gson gson = new Gson();
        String requestJson = gson.toJson(body);
        return httpPost(url, appKeyHeader, appKey, requestJson);
    }

    public static JsonElement httpPost(String url, String appKeyHeader, String appKey, String requestJson) {
        WebClient webClient=WebClient.create();
        String postString = webClient.post().uri(url).contentType(MediaType.APPLICATION_JSON).header(appKeyHeader,appKey).accept(MediaType.APPLICATION_JSON).body(BodyInserters.fromPublisher(Mono.just(requestJson), String.class)).retrieve().bodyToMono(String.class).block();

        JsonParser jsonParser = new JsonParser();
        JsonElement response = jsonParser.parse(postString);

        return response;
    }

}
