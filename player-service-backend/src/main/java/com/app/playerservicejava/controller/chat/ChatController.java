package com.app.playerservicejava.controller.chat;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.playerservicejava.service.chat.ChatClientService;

import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.models.Model;

@RestController
@RequestMapping(value = "${api.version}/chat", produces = { MediaType.APPLICATION_JSON_VALUE })
public class ChatController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatController.class);
    @Autowired
    private ChatClientService chatClientService;


    @PostMapping
    public @ResponseBody String chat() throws OllamaBaseException, IOException, InterruptedException {
        return chatClientService.chat();
    }

    @PostMapping("/players-csv")
    public @ResponseBody String chatPlayersCsv(@RequestBody String question) throws OllamaBaseException, IOException, InterruptedException {
        return chatClientService.chatPlayersCsv(question);
    }

    @GetMapping("/list-models")
    public ResponseEntity<List<Model>> listModels() throws OllamaBaseException, IOException, URISyntaxException, InterruptedException {
        List<Model> models = chatClientService.listModels();
        return ResponseEntity.ok(models);
    }
}
