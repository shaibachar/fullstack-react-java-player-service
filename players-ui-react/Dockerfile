# Dockerfile for React frontend (players-ui-react)
# Build stage
FROM node:20-alpine AS build
WORKDIR /app
COPY package.json package-lock.json ./
# Set backend API URL for build
ENV REACT_APP_API_URL=http://localhost:30001
ENV REACT_APP_API_VERSION=v1
RUN npm install --legacy-peer-deps
COPY . .
RUN npm run build

# Production stage
FROM nginx:alpine
WORKDIR /usr/share/nginx/html
COPY --from=build /app/build .
# Copy custom nginx config if needed
# COPY nginx.conf /etc/nginx/nginx.conf
EXPOSE 3000
CMD ["nginx", "-g", "daemon off;"]
