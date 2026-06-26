### BlogSphere Backend

This project is the Spring Boot backend for **BlogSphere**.

---

# Prerequisites

Make sure the following are installed:

- Docker Desktop
- ngrok
- Java 21 (if running without Docker)
- Jenkins (for CI/CD)

---

# Running the Backend Locally

## 1. Start Docker Desktop

Ensure Docker Desktop is running before executing any Docker commands.

---

## 2. Build the Docker Image

```bash
docker build -t blogsphere .
```

> **Note**
>
> The local Docker image is named **blogsphere**.
>
> The Docker Hub image is named **blog-app**.
>
> The `blogsphere` image is only intended for local development.

---

## 3. Run the Container

```bash
docker run -p 8080:8080 blogsphere
```

This maps:

```
Host Port      ->     Container Port
8080           ->     8080
```

The backend should now be available at:

```
http://localhost:8080
```

---

# Expose the Backend Using ngrok

Run:

```bash
ngrok http 8080
```

ngrok will generate a public URL similar to:

```
https://xxxxxxxx.ngrok-free.dev
```

---

# Update the Frontend

The frontend is deployed on **Lovable**.

Open:

```
src/api.ts
```

Update the `BASE_URL` constant with the latest ngrok URL.

Example:

```ts
const BASE_URL = "https://wincing-erratic-canary.ngrok-free.dev";
```

> **Important**
>
> Every time ngrok is restarted, a new URL may be generated. Update the frontend accordingly.

---

# CI/CD Pipeline

## 1. Expose Jenkins

Run:

```bash
ngrok http 8081
```

This exposes the local Jenkins instance to GitHub webhooks.

---

## 2. Push Code

Commit and push your changes to GitHub.

---

## 3. Verify Pipeline Trigger

Open Jenkins.

Navigate to:

```
pipe
```

pipeline.

A new build should automatically start.

If it does **not** trigger automatically, click:

```
Build Now
```

> Automatic triggering may occasionally fail due to slow network connectivity or webhook delivery delays.

---

## 4. Monitor the Build

Open the running build.

Click:

```
Console Output
```

Verify that all pipeline stages complete successfully.

---

## 5. Verify Docker Hub

Once the pipeline completes successfully, Jenkins pushes the latest Docker image to Docker Hub.

You should see a newly published image with the tag:

```
latest
```

under the repository:

```
blog-app
```

---

# Workflow Summary

1. Start Docker Desktop
2. Build the Docker image

   ```bash
   docker build -t blogsphere .
   ```

3. Run the container

   ```bash
   docker run -p 8080:8080 blogsphere
   ```

4. Start ngrok for the backend

   ```bash
   ngrok http 8080
   ```

5. Copy the generated ngrok URL.

6. Update:

   ```ts
   const BASE_URL = "<your-ngrok-url>";
   ```

   in the frontend `api.ts`.

7. Start ngrok for Jenkins

   ```bash
   ngrok http 8081
   ```

8. Push changes to GitHub.

9. Verify the Jenkins **pipe** pipeline starts.

10. If required, click **Build Now**.

11. Check **Console Output** for successful execution.

12. Verify the updated `latest` image has been pushed to Docker Hub.
