package ru.netology;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        List<Post> catsPosts;
        String catsPostsUrl = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";
        String body = readBody(catsPostsUrl);

        catsPosts = readAnswer(body);
        List<Post> upvoteNotNull = catsPosts.stream()
                .filter(post -> post.getUpvotes() > 0)
                .collect(Collectors.toList());
        upvoteNotNull.forEach(System.out::println);
    }
    public static String readBody(String url) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();
        HttpGet request = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(request);

//        Arrays.stream(response.getAllHeaders()).forEach(System.out::println);

        String body = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
        httpClient.close();
        return body;
    }
    public static List<Post> readAnswer(String body) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Post> posts = mapper.readValue(body, new TypeReference<List<Post>>() {});
//        posts.forEach(System.out::println);
        return posts;
    }
}