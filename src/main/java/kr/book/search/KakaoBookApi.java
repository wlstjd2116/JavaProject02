package kr.book.search;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KakaoBookApi {
    private static final String API_KEY = "2f76237a640bf75c42e84f414f7a1dc3";
    private static final String API_BASE_URL = "https://dapi.kakao.com/v3/search/book";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    // 책검색 메서드
    public static List<Book> searchBooks(String title) throws IOException {
        //url build
        HttpUrl.Builder urlBuilder = HttpUrl.parse(API_BASE_URL).newBuilder();
        urlBuilder.addQueryParameter("query", title);

        // Request(요청) => url + header builder
        //
        Request request = new Request.Builder().url(urlBuilder.build())
                .addHeader("Authorization", "KakaoAK " + API_KEY)
                .build();

        // client newCall().execute -> 접속 요청(try)
        try(Response response = client.newCall(request).execute()) {
            if(!response.isSuccessful()) throw new IOException("Request failed " + response);

            // response에 들어온 값을 charStream으로 받아온 후 , 이를 gson을 이용하여  sonObject로 변경하는 과정
            JsonObject jsonResponse = gson.fromJson(response.body().charStream(), JsonObject.class);
            JsonArray documents = jsonResponse.getAsJsonArray("documents");

            List<Book> books = new ArrayList<>();
            for(JsonElement document : documents){
                JsonObject bookJson = document.getAsJsonObject();
                Book book = new Book(
                        bookJson.get("title").getAsString(),
                        bookJson.get("authors").getAsJsonArray().toString(),
                        bookJson.get("publisher").getAsString(),
                        bookJson.get("thumbnail").getAsString()
                );
                books.add(book);
            }
            return books;
        }
    }
}
