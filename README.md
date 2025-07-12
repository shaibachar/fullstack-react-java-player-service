# ⚾ Player Service App
<p align="center">
Player Service is a lightweight fullstack app built using Spring Boot for the backend and React for the frontend app. Player service backend app serves as a simple way to access player information stored in a `Player.csv` file and makes this data available through a RESTful APIs. These APIs are used by Player Service UI app to show player information.
</p>

# ✨ Introduction

Player Service App

<img width="1349" alt="Screenshot 2024-10-29 at 11 00 29 PM" src="https://github.com/user-attachments/assets/b1cadc82-0484-4328-8170-018eedfac327">


# 👨‍💻 Tech stack

## Backend
- Java 17
- Maven
- Spring Boot 3.3.4 (with Spring Web MVC, Spring Data JPA)
- H2 Database
- [Docker](https://www.docker.com/) or [Podman](https://podman.io/)
- [Ollama4j SDK](https://ollama4j.github.io/ollama4j/intro)

## Frontend
- React
- NPM
- Node.js (version 18.20.2)
    - Download and install from [nodejs.org](https://nodejs.org/)
    - Verify installation: `node --version`
- Css

# 🔨 Installation
1. Clone this repository or Download the code as zip

2. Set up backend app
    - Refer to [player-service-backend README](player-service-backend/README.md) for backend app setup instructions.

3. Set up frontend app
    - Refer to [players-ui-react README](players-ui-react/README.md) for frontend setup instructions.

## Helm & Kubernetes Deployment

### Install Helm (Windows)
Download and install from: https://helm.sh/docs/intro/install/
Or use Chocolatey:
```
choco install kubernetes-helm
```

### Package the Helm chart
```
cd player-service-chart
helm package .
```

### Deploy to local Kubernetes (Docker Desktop)
```
# Make sure Docker Desktop Kubernetes is enabled
# No need to run minikube commands

# Install the chart (replace version as needed)
helm install player-service ./player-service-chart-0.1.0.tgz
```

### Check status
```
kubectl get pods
kubectl get services
```

### Uninstall
```
helm uninstall player-service
```

### Build Docker images for Docker Desktop Kubernetes

1. Build your images (from project root):
```
docker build -t player-service-backend:latest ./player-service-backend

docker build -t players-ui-react:latest ./players-ui-react

docker build -t players-ollama:latest -f Dockerfile.ollama-model .
```

2. No need to load images into Kubernetes—Docker Desktop shares images automatically.

3. Deploy the Helm chart as described above.

If you update your images, repeat these steps before redeploying the chart.

### Expose services with port-forward

To access the frontend and backend from your browser and allow the frontend to contact the backend, run these commands after deploying:

```
# Port-forward frontend service to localhost:3000
kubectl port-forward service/frontend 3000:80

# Port-forward backend service to localhost:8080
kubectl port-forward service/backend 8080:8080
```

- Open http://localhost:3000 in your browser for the frontend UI.
- The frontend will be able to contact the backend at http://localhost:8080.

Leave these terminals running while you use the app.


