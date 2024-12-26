package com.ticket.waiting.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.Optional;

@Controller
public class ViewController {

    private final WebClient webClient;

    public ViewController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://127.0.0.1:9010").build();
    }

    @GetMapping("/")
    public Mono<String> index(@RequestParam(name = "queue", defaultValue = "default") String queue,
                              @RequestParam(name = "user_id") Long userId,
                              HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String cookieName = "user-queue-%s-token".formatted(queue);
        String token;
        if (cookies != null) {
            token = Arrays.stream(cookies)
                    .filter(i -> i.getName().equalsIgnoreCase(cookieName))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse("");
        } else {
            token = "";
        }

        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/queue/allowed")
                        .queryParam("queue", queue)
                        .queryParam("user_id", userId)
                        .queryParam("token", token)
                        .build())
                .retrieve()
                .bodyToMono(AllowedUserResponse.class)
                .map(response -> {
                    if (response.allowed() == null || !response.allowed()) {
                        // 대기 웹페이지로 리다이렉트
                        return "redirect:http://127.0.0.1:9010/waiting-room?user_id=%d&redirect_url=%s".formatted(
                                userId, "http://127.0.0.1:9000?user_id=%d".formatted(userId)
                        );
                    }
                    // 허용 상태이면 해당 페이지를 진입
                    return "index";
                });
    }

    public record AllowedUserResponse(Boolean allowed){

    }

}
