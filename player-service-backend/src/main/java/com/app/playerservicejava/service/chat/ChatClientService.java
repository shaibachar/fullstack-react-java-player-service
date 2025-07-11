package com.app.playerservicejava.service.chat;

import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.models.Model;
import io.github.ollama4j.models.OllamaResult;
import io.github.ollama4j.types.OllamaModelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.github.ollama4j.utils.OptionsBuilder;
import io.github.ollama4j.utils.PromptBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class ChatClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatClientService.class);

    private OllamaAPI ollamaAPI;

    @Autowired
    public ChatClientService() {
        String ollamaHost = System.getenv("OLLAMA_HOST");
        if (ollamaHost == null || ollamaHost.isEmpty()) {
            ollamaHost = "http://ollama:11434"; // default for docker-compose
        }
        this.ollamaAPI = new OllamaAPI(ollamaHost);
    }

    public String loadCsvByFileName(String csvFileName) {
        String csvData = "";
        java.nio.file.Path csvPath = java.nio.file.Paths.get("player-service-backend", csvFileName);
        try {
            csvData = java.nio.file.Files.readString(csvPath);
        } catch (IOException e) {
            LOGGER.error("Failed to read {}: {}", csvFileName, e.toString());
            csvData = "(CSV file could not be loaded)";
        }
        return csvData;
    }

    public String loadCsv(java.io.File file) {
        String csvData = "";
        try {
            csvData = java.nio.file.Files.readString(file.toPath());
        } catch (IOException e) {
            LOGGER.error("Failed to read file {}: {}", file.getAbsolutePath(), e.toString());
            csvData = "(CSV file could not be loaded)";
        }
        return csvData;
    }

    public String chatPlayersCsv(String question) throws OllamaBaseException, IOException, InterruptedException {
        return chatPlayersCsv(question, "Player.csv");
    }

    public String chatPlayersCsv(String question, String csvFileName) throws OllamaBaseException, IOException, InterruptedException {
        String model = OllamaModelType.TINYLLAMA;
        String csvData = loadCsvByFileName(csvFileName);
        PromptBuilder promptBuilder = new PromptBuilder()
                .addLine("You are an expert on baseball player statistics.")
                .addLine("Here is the contents of " + csvFileName + ":")
                .addSeparator()
                .addLine(csvData)
                .addSeparator()
                .addLine("Answer the following question using the data in " + csvFileName + ".")
                .addLine("Question: " + question);
        boolean raw = false;
        OllamaResult response = ollamaAPI.generate(model, promptBuilder.build(), raw, new OptionsBuilder().build());
        return response.getResponse();
    }

    public List<Model> listModels() throws OllamaBaseException, IOException, URISyntaxException, InterruptedException {
        List<Model> models = ollamaAPI.listModels();
        return models;
    }

    public String chat() throws OllamaBaseException, IOException, InterruptedException {
        String model = OllamaModelType.TINYLLAMA;

        PromptBuilder promptBuilder =
                new PromptBuilder()
                        .addLine("Recite a haiku about recursion.");

        /** Sample prompt
         *
        PromptBuilder promptBuilder =
                new PromptBuilder()
                        .addLine("You are an expert coder and understand different programming languages.")
                        .addLine("Given a question, answer ONLY with code.")
                        .addLine("Produce clean, formatted and indented code in markdown format.")
                        .addLine(
                                "DO NOT include ANY extra text apart from code. Follow this instruction very strictly!")
                        .addLine("If there's any additional information you want to add, use comments within code.")
                        .addLine("Answer only in the programming language that has been asked for.")
                        .addSeparator()
                        .addLine("Example: Sum 2 numbers in Python")
                        .addLine("Answer:")
                        .addLine("```python")
                        .addLine("def sum(num1: int, num2: int) -> int:")
                        .addLine("    return num1 + num2")
                        .addLine("```")
                        .addSeparator()
                        .add("How do I read a file in Go and print its contents to stdout?");
         **/

        boolean raw = false;
        OllamaResult response = ollamaAPI.generate(model, promptBuilder.build(), raw, new OptionsBuilder().build());
        return response.getResponse();
    }

}
