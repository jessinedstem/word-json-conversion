package com.edstem.kakfa.wordjsonkafka.controller;

import com.edstem.kakfa.wordjsonkafka.service.WordJsonProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/v1/parse")
public class WordJsonMessageController {
    private final WordJsonProducerService wordJsonProducerService;
@Autowired
    public WordJsonMessageController(WordJsonProducerService wordJsonProducerService) {
        this.wordJsonProducerService = wordJsonProducerService;
    }

    @PostMapping("/conversion")
    public ResponseEntity<String> convertWordToJson(@RequestParam("file") MultipartFile file) throws IOException {
        String json = wordJsonProducerService.convertWordToJson(file);
        return ResponseEntity.ok(json);
    }
}
