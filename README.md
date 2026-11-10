# Mini Shop — Java + Maven + Spring Boot + Docker

A beginner-friendly e-commerce demo. It serves a browser UI and JSON API from one Spring Boot application.

## Features
- Product catalogue
- Add products to cart; increase/decrease quantity
- Cart subtotal and item count
- Demo checkout confirmation
- One-command Docker image build

## Requirements
- Java 21 (JDK) and Maven 3.9+ for the manual host build
- Docker for the container exercise

## 1. Run manually first
From the project root:

```bash
java -version
mvn -version
mvn clean package
java -jar target/mini-ecommerce.jar
```

Open `http://localhost:8080` in your browser.

Stop the app with `Ctrl+C`.

## 2. Try the API
With the application running, open another terminal:

```bash
curl http://localhost:8080/api/products
curl http://localhost:8080/api/cart
curl -X POST http://localhost:8080/api/cart/1
curl http://localhost:8080/api/cart
curl -X POST http://localhost:8080/api/checkout
```

## 3. Build and run with Docker
Build:

```bash
docker build -t mini-ecommerce:1.0 .
```

Run:

```bash
docker run --rm --name mini-shop -p 8080:8080 mini-ecommerce:1.0
```

Open `http://localhost:8080`.

Stop with `Ctrl+C`. If you used `-d` instead, stop with `docker stop mini-shop`.

## 4. If port 8080 is already in use
Run on host port 8085:

```bash
docker run --rm --name mini-shop -p 8085:8080 mini-ecommerce:1.0
```

Open `http://localhost:8085`.

## Important learning notes
- `mvn clean package` compiles, tests and creates the executable Spring Boot JAR.
- The Dockerfile has two stages: Maven/JDK to build; JRE to run.
- The browser files are in `src/main/resources/static`; Spring Boot serves them automatically.
- The frontend calls `/api/...` endpoints. The Java backend handles those requests.
- This demo keeps the cart in memory and has no database, login, payment gateway, stock control or real order persistence. It is for learning, not production.
- Product photos are loaded from Unsplash, so an internet connection is needed for those images. The app itself can still run if images do not load.
