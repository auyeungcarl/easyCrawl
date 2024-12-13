package com.github.kingschan1204.jdkhttp;

import com.github.kingschan1204.easycrawl.helper.regex.RegexHelper;
import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class JdkHttp {

    class MyCookieHandler extends CookieHandler {
        private Map<String, List<String>> cookieStore = new HashMap<>();

        @Override
        public Map<String, List<String>> get(URI uri, Map<String, List<String>> requestHeaders) throws IOException {
            String host = uri.getHost();
            List<String> cookies = cookieStore.get(host);
            if (cookies != null) {
                requestHeaders.put("Cookie", cookies);
            }
            return requestHeaders;
        }

        @Override
        public void put(URI uri, Map<String, List<String>> responseHeaders) throws IOException {
            String host = uri.getHost();
            List<String> setCookies = responseHeaders.get("Set-Cookie");
            if (setCookies != null) {
                cookieStore.put(host, setCookies);
            }
        }

        public Map<String, List<String>> getCookies() {
            return cookieStore;
        }
    }

    @Test
    public void get() throws Exception {
//        SSLContext sslContext = SSLContext.getInstance("TLSv1.3");
//        sslContext.init(null, null, null);
        MyCookieHandler cookieHandler = new MyCookieHandler();

        HttpClient httpClient = HttpClient.newBuilder()
//                .sslContext(sslContext)
                // 自定义代理
                //.proxy(ProxySelector.of(new InetSocketAddress("proxy.example.com", 8080)))
                //系统代理
                //.proxy(ProxySelector.getDefault())
                .connectTimeout(Duration.ofSeconds(10))
                .cookieHandler(cookieHandler)
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://xueqiu.com/about"))
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//        System.out.println(response.body());
        System.out.println("http code:" + response.statusCode());
        Map<String, List<String>> map = response.headers().map();
       /* System.out.println("http headers:" + map);
        List<String> cookies = map.get("Set-Cookie");
        cookies.forEach(System.out::println);*/
        Map<String,String> heads = new LinkedHashMap<>(map.size());

        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
           String key = entry.getKey();
           List<String> values = entry.getValue();
           if(key.equalsIgnoreCase("Set-Cookie")){
               List<String> list = new ArrayList<>();
               for(String cookie : values){
                   list.add(RegexHelper.findFirst(cookie,"([^;]*)(?=;)"));
               }
               heads.put(key,list.stream().collect(Collectors.joining(";")));
           }
           heads.put(key,values.stream().collect(Collectors.joining(",")));
        }
        System.out.println(heads);


    }

    public static void main(String[] args) {
        String text = """
                acw_tc=1a0c66d917340843125184867e0049e0a7fbb2e014ed788d1470c5d4108915;path=/;HttpOnly;Max-Age=1800
                xq_a_token=220b0abef0fac476d076c9f7a3938b7edac35f48; path=/; domain=.xueqiu.com; httponly
                xqat=220b0abef0fac476d076c9f7a3938b7edac35f48; path=/; domain=.xueqiu.com; httponly
                xq_r_token=a57f65f14670a8897031b7c4f10ea42a50894850; path=/; domain=.xueqiu.com; httponly
                xq_id_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1aWQiOi0xLCJpc3MiOiJ1YyIsImV4cCI6MTczNTY5Mjg4OSwiY3RtIjoxNzM0MDg0Mjg2NjIwLCJjaWQiOiJkOWQwbjRBWnVwIn0.WDQueVRBhskDhKbPvVJiWVO8OY2ubceib4mLPo2yfiJoJHBYoCFJra3yXzxmhQgaYfsJPkvEVCYHTMOdQ6Uw09XviDtCX42mh76cstEZ0169RoKWUHGRDC54CmIMQsSxuoF6PMK7r2GKkQFL9EEE1sgUh6aWYTIEPppajr4c-NGM-Q-TnQ2LTL7qb8_hs9MkRzAIYiglMcXiFMyK44jUrkh0XRxpHLA-gPx-7vvst8vvHxaQf1ZlqWbMB8fe5JoCTtTB_-8R_r55S3Tjegv4zFBvPG-g44hbRIQ0vI82qqglSfhSOEE0oLfmo_HHvQrOlcomjMvpEt3dfsdDbKhABw; path=/; domain=.xueqiu.com; httponly
                xq_is_login=; path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT; domain=.xueqiu.com; httponly
                remember=; path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT; domain=.xueqiu.com; httponly
                cookiesu=221734084312529; path=/; expires=Sun, 14 Dec 2025 10:05:12 GMT; domain=.xueqiu.com
                u=221734084312529; path=/; domain=.xueqiu.com
                """;
       String[] lines = text.split("\n");
       for(String line : lines){
           System.out.println(line);
           System.out.println(RegexHelper.findFirst(line,"([^;]*)(?=;)"));
           System.out.println();
       }
    }
}
