## README.md

```markdown
# Blog App

A simple Spring Boot blog backend with:
- MySQL persistence
- JWT authentication
- signup & login
- protected post creation
- public homepage feed with optional body search

---

## Prerequisites

- Java 21
- MySQL 8.x
- Git (optional)
- Windows PowerShell or Command Prompt

This repo uses the Maven wrapper, so Maven is included via `.mvn/wrapper`.

---

## Recommended VS Code Extensions

Install these for the best Java + Spring Boot experience:

- `Extension Pack for Java` by Microsoft (`vscjava.vscode-java-pack`)
- `Spring Boot Extension Pack` by Pivotal (`pivotal.vscode-spring-boot`)
- `Java Test Runner` by Microsoft
- `Maven for Java` by Microsoft
- `Debugger for Java` by Microsoft

---

## Setup

1. Clone or open the project folder in VS Code.
2. Ensure MySQL is running.
3. Create a database for the project, for example:

```sql
CREATE DATABASE blogdb;
```

4. Configure environment variables.

### Required environment variables

| Name | Description | Example |
|------|-------------|---------|
| `SPRING_DATASOURCE_URL` | JDBC URL for MySQL | `jdbc:mysql://localhost:3306/blogdb?useSSL=false&serverTimezone=UTC` |
| `SPRING_DATASOURCE_USERNAME` | MySQL username | `root` |
| `SPRING_DATASOURCE_PASSWORD` | MySQL password | `yourpassword` |
| `BLOG_JWT_SECRET` | JWT signing secret | `replace-this-with-a-strong-secret` |
| `BLOG_JWT_EXPIRATION_MS` | JWT expiration in ms | `86400000` |

### Example for Windows PowerShell

```powershell
$env:SPRING_DATASOURCE_URL="jdbc:mysql://localhost:3306/blogdb?useSSL=false&serverTimezone=UTC"
$env:SPRING_DATASOURCE_USERNAME="root"
$env:SPRING_DATASOURCE_PASSWORD="root"
$env:BLOG_JWT_SECRET="change-me-in-production-change-me-in-production-32"
$env:BLOG_JWT_EXPIRATION_MS="86400000"
```

### Example for Command Prompt

```cmd
set SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/blogdb?useSSL=false&serverTimezone=UTC
set SPRING_DATASOURCE_USERNAME=root
set SPRING_DATASOURCE_PASSWORD=root
set BLOG_JWT_SECRET=change-me-in-production-change-me-in-production-32
set BLOG_JWT_EXPIRATION_MS=86400000
```

---

## Run the App

From the project root:

```powershell
.\mvnw.cmd spring-boot:run
```

Or build first:

```powershell
.\mvnw.cmd clean package
java -jar target\blog-0.0.1-SNAPSHOT.jar
```

The server starts on:

```text
http://localhost:8080
```

---

## APIs

### Signup

```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","password":"secret123"}'
```

Response includes:
- `userId`
- `token`
- `username`

### Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","password":"secret123"}'
```

### Create Post

Requires `Authorization: Bearer <token>` and `userId` in the body:

```bash
curl -X POST http://localhost:8080/api/posts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "userId": 1,
    "title": "First post",
    "body": "# Hello\n\nThis is markdown."
  }'
```

### List Posts

Public endpoint:

```bash
curl http://localhost:8080/api/posts
```

Optional body search:

```bash
curl "http://localhost:8080/api/posts?search=markdown"
```

---

## Notes

- The app uses MySQL by default.
- The `posts` table stores large markdown text in `LONGTEXT`.
- Login and signup responses now return `userId` with the JWT token and username.
- If the server fails to start because port `8080` is already in use, stop the other service or change the port.

---

## Tips for Newbies

- Use VS Code extensions to get Java syntax checking and Spring Boot support.
- Make sure MySQL is running and your env vars are correct.
- If you get JSON errors from curl, check your quoting carefully.
- If you change `BLOG_JWT_SECRET`, restart the server to apply it.
```

If you want, I can also give you a ready-made `.env` example and a sample `curl` workflow for signup, login, and creating a post.If you want, I can also give you a ready-made `.env` example and a sample `curl` workflow for signup, login, and creating a post.
