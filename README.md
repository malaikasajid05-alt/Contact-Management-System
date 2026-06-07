<div align="center"

<h1>📒 Contact Management System</h1>

<p>A full-stack contact management application built with <strong>Spring Boot</strong> and <strong>React</strong> — featuring JWT authentication, smart search, CSV import/export, and a clean, responsive interface.</p>

<br/>

![Java](https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![React](https://img.shields.io/badge/React_19-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)
![SQL Server](https://img.shields.io/badge/SQL_Server-CC2927?style=for-the-badge&logo=microsoft-sql-server&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
![SonarQube](https://img.shields.io/badge/SonarQube-4E9BCD?style=for-the-badge&logo=sonarqube&logoColor=white)

</div>

---

## ✨ Features

<table>
<tr>
<td width="50%">

**🔐 Authentication**
- User Registration & Login
- JWT-based stateless authentication
- BCrypt password encryption
- Change password
- Remember Me support

**📇 Contact Management**
- Add, View, Edit, Delete contacts
- Search by name, email, or phone
- Sort A → Z / Z → A
- Pagination support
- Delete confirmation modal

</td>
<td width="50%">

**📞 Rich Contact Details**
- Multiple emails (Personal / Work)
- Multiple phone numbers (Mobile / Home / Work)
- Job title & category support

**📤 Import / Export**
- Export contacts to CSV
- Import contacts from CSV

**👤 User Profile**
- View profile information
- Change password from profile page

</td>
</tr>
</table>

---

## 🛠️ Tech Stack

| Layer | Technologies |
|---|---|
| **Backend** | Java 21, Spring Boot 3, Spring Security, Spring Data JPA, Hibernate |
| **Database** | Microsoft SQL Server |
| **Auth** | JWT (jjwt), BCrypt |
| **Frontend** | React 19, JavaScript ES6+, Axios, HTML5/CSS3 |
| **Testing** | JUnit 5, Mockito |
| **Code Quality** | SonarQube |
| **Build** | Maven, npm |

---

## 📁 Project Structure

<details>
<summary><strong>🖥️ Backend</strong></summary>

```
backend/
└── src/main/java/com/contactmanager/
    ├── controller/        # REST API endpoints
    ├── service/           # Business logic
    ├── repository/        # Database access (JPA)
    ├── entity/            # Database models
    ├── dto/               # Data Transfer Objects
    ├── mapper/            # Entity ↔ DTO mapping
    ├── security/          # JWT & Spring Security config
    └── exception/         # Global exception handling

└── src/main/resources/
    └── application.properties
```

</details>

<details>
<summary><strong>💻 Frontend</strong></summary>

```
frontend/
└── src/
    ├── api/                    # Axios API calls
    ├── assets/                 # Images & static files
    ├── components/
    │    ├── ChangePasswordModal/
    │    ├── ContactCard/
    │    ├── ContactModal/
    │    ├── DeleteModal/
    │    ├── Modal/
    │    ├── Navbar/
    │    ├── Pagination/
    │    └── SearchBar/
    ├── context/                # Auth context (React Context API)
    ├── hooks/                  # Custom React hooks
    ├── layouts/                # Page layout wrappers
    ├── pages/                  # Route-level page components
    ├── routes/                 # Routing configuration
    ├── styles/                 # Global & component CSS
    ├── utils/                  # Helper functions
    ├── App.jsx
    ├── index.jsx
    └── index.html
```

</details>

---

## 🚀 Getting Started

### Prerequisites

Before running the project, ensure you have:

- Java 21+
- Maven 3.8+
- Node.js 18+ & npm
- Microsoft SQL Server

---

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/contact-management-system.git
cd contact-management-system
```

---

### 2. Backend Setup

#### Create the Database

```sql
CREATE DATABASE contact_management_system;
```

#### Configure `application.properties`

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=contact_management_system;encrypt=true;trustServerCertificate=true
spring.datasource.username=springuser
spring.datasource.password=Spring@12345
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

spring.jpa.database-platform=org.hibernate.dialect.SQLServerDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=mysecretkeymysecretkeymysecretkey12
jwt.expiration=86400000
```

#### Run the Backend

```bash
cd backend
mvn spring-boot:run
```

> Backend runs at `http://localhost:8080`

---

### 3. Frontend Setup

```bash
cd frontend
npm install
npm run dev
```

> Frontend runs at `http://localhost:5173`

---

## 🔗 API Reference

### Authentication

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/auth/register` | Register a new user |
| `POST` | `/auth/login` | Login and receive JWT |
| `PUT` | `/users/change-password` | Change user password |

### Contacts

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/contacts` | Create a contact |
| `GET` | `/contacts` | Get all contacts (paginated) |
| `GET` | `/contacts/{id}` | Get a single contact |
| `PUT` | `/contacts/{id}` | Update a contact |
| `DELETE` | `/contacts/{id}` | Delete a contact |
| `GET` | `/contacts/export` | Export contacts as CSV |
| `POST` | `/contacts/import` | Import contacts from CSV |

---

## 🧪 Running Tests

```bash
cd backend
mvn test
```

---

## 📊 SonarQube Analysis

```bash
mvn sonar:sonar \
  -Dsonar.projectKey=contact-management-system \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=your_token
```

---

## 👩‍💻 Author

**Malaika Sajid** — Software Engineering Student

[![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/your-username)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://linkedin.com/in/your-profile)
[![Email](https://img.shields.io/badge/Email-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:malaikakh5@gmail.com)

---

<div align="center">
  <sub>If you found this project useful, consider giving it a ⭐ — it means a lot!</sub>
</div>
