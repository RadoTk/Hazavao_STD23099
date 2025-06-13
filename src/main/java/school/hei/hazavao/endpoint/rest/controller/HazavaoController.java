package school.hei.hazavao.endpoint.rest.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/hazavao")
@AllArgsConstructor
public class HazavaoController {

    private final RestTemplate restTemplate;

    // 🔑 Injection de la clé OpenAI depuis application.properties
    @Value("${openai.api.key}")
    private String apiKey;

    @GetMapping
    public String getDefinition(@RequestParam String teny) {
        String apiUrl = "https://api.openai.com/v1/chat/completions";

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "user", "content", "Hazavao amin'ny teny malagasy ilay teny: " + teny)
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            return (String) message.get("content");
        } catch (Exception e) {
            return "Tsy nety ny fangatahana: " + e.getMessage();
        }
    }
}
