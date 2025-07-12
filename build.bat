docker build -t player-service-backend:latest ./player-service-backend

docker build -t players-ui-react:latest ./players-ui-react

docker build -t ollama:latest -f Dockerfile.ollama-model .