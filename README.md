# Product Management Application

This is a Spring Boot and React project for managing products, including product types and colors. The backend uses Java 21, Spring Boot, PostgreSQL, and Lombok, while the frontend is built with React and Axios.

## Features

- Add, view, update, and delete products with name, product type, and colors.
- Manage product types and colors dynamically.
- Uses Docker for easy deployment.
- Backend is a REST API powered by Spring Boot.
- Frontend is a React-based UI with Axios for API calls.
- Includes unit tests for the backend.

## Prerequisites

Before running the project, ensure you have the following installed:

- **Docker** (Required for running with Docker)
- **Docker Compose** (Required for running with Docker Compose)
- **Node.js & npm** (If running the frontend manually)
- **Java 21** (If running the backend manually)
- **Maven** (If running the backend manually)
- **PostgreSQL** (If running manually without Docker)

## How to Download and Set Up the Project

### Clone the Repository

```sh
git clone https://github.com/amitakay28/product-management-app.git
cd product-management-app
```

### Run the Project with Docker (Recommended)

The simplest way to run everything (Backend, Frontend, Database) is using Docker:

```sh
docker-compose up --build
```

- The backend will run at `http://localhost:8080`
- The frontend will run at `http://localhost:3000`
- PostgreSQL will be set up automatically.
- **Database data will persist even if the container is removed manually.**

To stop the containers:

```sh
docker-compose down
```

### Running Manually (Without Docker)

#### Setup PostgreSQL Database

Ensure PostgreSQL is installed and running. Then create a database:

```sh
psql -U postgres
CREATE DATABASE productdb;
```

Make sure the credentials match those in `application.properties`.

#### Run the Backend

```sh
cd backend
mvn clean install
mvn spring-boot:run
```

The API will be available at `http://localhost:8080/api/products`.

#### Run the Frontend

```sh
cd frontend
npm install
npm start
```

The UI will be available at `http://localhost:3000`.

## API Testing with Postman

For easy API testing, use the provided **Postman Collection**:

1. Open **Postman**.
2. Import the `postman_collection.json` file (provided in the repository).
3. Run the available API requests for **Products, Colours, and Product Types**.

## API Endpoints

### **Products**

- **Get All Products**: `GET /api/products`
- **Get Product by ID**: `GET /api/products/{id}`
- **Add Product**: `POST /api/products`
- **Update Product**: `PUT /api/products/{id}`
- **Delete Product**: `DELETE /api/products/{id}`

### **Colours**

- **Get All Colours**: `GET /api/colours`
- **Add Colour**: `POST /api/colours`
- **Update Colour**: `PUT /api/colours/{id}`
- **Delete Colour**: `DELETE /api/colours/{id}`

### **Product Types**

- **Get All Product Types**: `GET /api/product-types`
- **Add Product Type**: `POST /api/product-types`
- **Update Product Type**: `PUT /api/product-types/{id}`
- **Delete Product Type**: `DELETE /api/product-types/{id}`

## Running Tests

Run backend unit tests:

```sh
cd backend
mvn test
```

## Project Structure

```
├── backend (Spring Boot)
│   ├── src/main/java/com/example/productapp
│   ├── src/test/java/com/example/productapp (Unit Tests)
│   ├── application.properties
│   ├── pom.xml (Maven Dependencies)
│   ├── Dockerfile (Backend Docker Setup)
│
├── frontend (React)
│   ├── src/components (React Components)
│   ├── src/services (API Calls with Axios)
│   ├── styles.css (Basic Styling)
│   ├── package.json (Frontend Dependencies)
│   ├── Dockerfile (Frontend Docker Setup)
│
├── docker-compose.yml (Docker Setup for Backend, Frontend, and DB)
├── postman_collection.json (Postman API Testing Collection)
├── README.md (This File)
```

## Contributing

Feel free to fork, submit PRs, or suggest improvements.

