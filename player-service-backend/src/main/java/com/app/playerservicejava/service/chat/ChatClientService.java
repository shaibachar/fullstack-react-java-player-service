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
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class ChatClientService {
    /**
     * Checks if the Ollama server is available by attempting to list models.
     * Returns true if available, false otherwise.
     */
    public boolean isOllamaAvailable() {
        try {
            ollamaAPI.listModels();
            return true;
        } catch (Exception e) {
            LOGGER.warn("Ollama server not available: {}", e.getMessage());
            return false;
        }
    }

    // Cache for CSV chunks by filename
    private final java.util.Map<String, List<String>> csvChunkCache = new java.util.concurrent.ConcurrentHashMap<>();

    // Split CSV into chunks of maxChars (approx. 2000 tokens)
    // Split CSV into chunks by rows (default 100 rows per chunk)
    public List<String> splitCsvIntoChunks(String csv, int rowsPerChunk) {
        List<String> chunks = new java.util.ArrayList<>();
        String[] lines = csv.split("\r?\n");
        int headerRows = 1;
        String header = lines.length > 0 ? lines[0] : "";
        for (int i = headerRows; i < lines.length; i += rowsPerChunk) {
            StringBuilder chunk = new StringBuilder();
            chunk.append(header).append("\n");
            for (int j = i; j < Math.min(i + rowsPerChunk, lines.length); j++) {
                chunk.append(lines[j]).append("\n");
            }
            chunks.add(chunk.toString());
        }
        return chunks;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatClientService.class);

    private OllamaAPI ollamaAPI;

    @Autowired
    public ChatClientService() {
        String ollamaHost = System.getenv("OLLAMA_HOST");
        if (ollamaHost == null || ollamaHost.isEmpty()) {
            // ollamaHost = "http://host.docker.internal:11434"; // default for
            // docker local development
            ollamaHost = "http://ollama:11434"; // default for docker-compose
        }
        this.ollamaAPI = new OllamaAPI(ollamaHost);
        
        //TODO: remove the timeout setting in production
        // This is just for development purposes to avoid timeout issues
        this.ollamaAPI.setRequestTimeoutSeconds(120); // Set a reasonable timeout
    }

    public String loadCsvByFileName(String csvFileName) {
        String csvData = "";
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(csvFileName)) {
            if (is != null) {
                csvData = new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
            } else {
                LOGGER.error("Failed to read {}: not found in classpath", csvFileName);
                csvData = "(CSV file could not be loaded)";
            }
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

    public String chatPlayersCsv(String question, String csvFileName)
            throws OllamaBaseException, IOException, InterruptedException {
        String model = OllamaModelType.TINYLLAMA;
        // Use cache if available
        List<String> chunks = csvChunkCache.get(csvFileName);
        if (chunks == null) {
            String csvData = loadCsvByFileName(csvFileName);
            // Default: 10 rows per chunk
            chunks = splitCsvIntoChunks(csvData, 10);
            csvChunkCache.put(csvFileName, chunks);
        }
        StringBuilder combinedResult = new StringBuilder();
        boolean raw = false;
        for (String chunk : chunks) {
            if (chunk == null || chunk.isEmpty()) {
                LOGGER.warn("Skipping empty chunk for file: {}", csvFileName);
                continue;
            } else if (chunk.split("\r?\n").length > 11) {
                LOGGER.warn("Skipping chunk too large: {}", chunk);
                continue;
            }
            // Build prompt with chunk and question
            // Use PromptBuilder to create a structured prompt
            PromptBuilder promptBuilder = new PromptBuilder()
                    .addLine("You are an expert on baseball player statistics.")
                    .addLine("Here is a portion of " + csvFileName + ":")
                    .addSeparator()
                    .addLine(chunk)
                    .addSeparator()
                    .addLine("Answer the following question using only this data.")
                    .addLine("Question: " + question);
            OllamaResult response = ollamaAPI.generate(model, promptBuilder.build(), raw, new OptionsBuilder().build());
            combinedResult.append(response.getResponse()).append("\n");

        }
        return combinedResult.toString();
    }

    public List<Model> listModels() throws OllamaBaseException, IOException, URISyntaxException, InterruptedException {
        List<Model> models = ollamaAPI.listModels();
        return models;
    }

    public String chat() throws OllamaBaseException, IOException, InterruptedException {
        String model = OllamaModelType.TINYLLAMA;

        PromptBuilder promptBuilder = new PromptBuilder()
                .addLine("Recite a haiku about recursion.");

        /**
         * Sample prompt
         *
         * PromptBuilder promptBuilder =
         * new PromptBuilder()
         * .addLine("You are an expert coder and understand different programming
         * languages.")
         * .addLine("Given a question, answer ONLY with code.")
         * .addLine("Produce clean, formatted and indented code in markdown format.")
         * .addLine(
         * "DO NOT include ANY extra text apart from code. Follow this instruction very
         * strictly!")
         * .addLine("If there's any additional information you want to add, use comments
         * within code.")
         * .addLine("Answer only in the programming language that has been asked for.")
         * .addSeparator()
         * .addLine("Example: Sum 2 numbers in Python")
         * .addLine("Answer:")
         * .addLine("```python")
         * .addLine("def sum(num1: int, num2: int) -> int:")
         * .addLine(" return num1 + num2")
         * .addLine("```")
         * .addSeparator()
         * .add("How do I read a file in Go and print its contents to stdout?");
         **/

        boolean raw = false;
        OllamaResult response = ollamaAPI.generate(model, promptBuilder.build(), raw, new OptionsBuilder().build());
        return response.getResponse();
    }

}
