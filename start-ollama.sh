#!/bin/sh

# Pull the required model (cache in image)
ollama pull tinyllama

# Start Ollama server in background
ollama serve &
# Wait for Ollama to be healthy
until curl -sf http://localhost:11434/; do
  echo "Waiting for Ollama to be healthy..."
  sleep 2
done
# Wait for server process to finish
wait
