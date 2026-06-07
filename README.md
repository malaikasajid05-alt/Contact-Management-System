<div align="center">

<h1>📒 Contact Management System</h1>

<p>A full-stack contact management application built with <strong>Spring Boot</strong> and <strong>React</strong> — featuring JWT authentication, smart search, CSV import/export and a clean, responsive interface.</p>

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

<div align="center">
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
- Search by name, email or phone
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
</div>

---

## 🛠️ Tech Stack

<div align="center">

| Layer | Technologies |
|:---:|:---|
| **Backend** | Java 21, Spring Boot 3, Spring Security, Spring Data JPA, Hibernate |
| **Database** | Microsoft SQL Server |
| **Auth** | JWT (jjwt), BCrypt |
| **Frontend** | React 19, JavaScript ES6+, Axios, HTML5/CSS3 |
| **Testing** | JUnit 5, Mockito |
| **Code Quality** | SonarQube |
| **Build** | Maven, npm |

</div>

---


## 📸 Screenshots
 
<div align="center">

### 🔐 **Login Page**
<img width="958" height="436" alt="Screenshot 2026-06-07 011516" src="https://github.com/user-attachments/assets/9e34eef9-0d75-49e2-9c81-90c5b30573c2" />

### 📝 Register Page
<img width="956" height="433" alt="Screenshot 2026-06-07 011533" src="https://github.com/user-attachments/assets/853ec229-d767-4857-895b-065dba8d6750" />
 
### 📋 Contacts Dashboard
<img width="956" height="427" alt="Contacts Dashboard" src="https://github.com/user-attachments/assets/db239fdf-de87-4c07-a8c6-3ef0215496c1" />
<img width="957" height="437" alt="Contacts Dashboard 2" src="https://github.com/user-attachments/assets/be760723-3fd9-4ae4-b078-480a30085787" />

### 📇 Contacts

<img width="959" height="435" alt="Screenshot 2026-06-07 005240" src="https://github.com/user-attachments/assets/3462af8b-95ce-44f5-9a15-bd42bd7cf503" />
 
### ➕ Add Contact

<img width="959" height="429" alt="Screenshot 2026-06-07 005357" src="https://github.com/user-attachments/assets/4c392b2c-1114-410a-835f-86996b452f19" />
<img width="959" height="432" alt="Screenshot 2026-06-07 005416" src="https://github.com/user-attachments/assets/7ce9d80c-d626-4db2-9147-a26e026828dd" />

### ✏️ Edit Contact

<img width="955" height="434" alt="Screenshot 2026-06-07 005446" src="https://github.com/user-attachments/assets/e05a7fec-9b6e-4ea3-9bd3-7b238ec6edd9" />
<img width="959" height="421" alt="Screenshot 2026-06-07 005502" src="https://github.com/user-attachments/assets/1649d1b9-dca2-4937-9c96-6d968a057e65" />

### 🗑️ Delete Contact

<img width="655" height="343" alt="Screenshot 2026-06-07 005519" src="https://github.com/user-attachments/assets/fce4a46f-f700-4999-8221-e76a8c01809d" />

### 📤 Import / Export

<img width="955" height="422" alt="Screenshot 2026-06-07 011413" src="https://github.com/user-attachments/assets/ed632d48-6b21-4553-b261-bc2a59024014" />

### 👤 User Profile

<img width="959" height="431" alt="Screenshot 2026-06-07 005544" src="https://github.com/user-attachments/assets/21f0077f-e2e7-4c97-95a1-6ecf77cf8920" />

 
</div>

</div>

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
git clone https://github.com/malaikasajid05-alt/Contact-Management-System.git
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

<div align="center">

| Method | Endpoint | Description |
|:---:|:---|:---|
| `POST` | `/auth/register` | Register a new user |
| `POST` | `/auth/login` | Login and receive JWT |
| `PUT` | `/users/change-password` | Change user password |

</div>

### Contacts

<div align="center">

| Method | Endpoint | Description |
|:---:|:---|:---|
| `POST` | `/contacts` | Create a contact |
| `GET` | `/contacts` | Get all contacts (paginated) |
| `GET` | `/contacts/{id}` | Get a single contact |
| `PUT` | `/contacts/{id}` | Update a contact |
| `DELETE` | `/contacts/{id}` | Delete a contact |
| `GET` | `/contacts/export` | Export contacts as CSV |
| `POST` | `/contacts/import` | Import contacts from CSV |

</div>

---

## 🧪 Running Tests
 
```bash
cd backend
.\mvnw.cmd test
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

<div align="center">

**Malaika Sajid** — Software Engineering Student

[![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/malaikasajid05-alt)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/malaika-sajid-5710b8363)
[![Email](https://img.shields.io/badge/Email-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:malaikakh5@gmail.com)

</div>

---

<div align="center">
  <sub>If you found this project useful, consider giving it a ⭐ — it means a lot!</sub>
</div>
