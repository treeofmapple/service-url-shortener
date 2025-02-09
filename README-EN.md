<div align="center"> <a> <img src="logo.png" alt="Logo" width="80" height="80"> </a> <br> <p>ShortURL is an efficient and secure URL shortener developed in Spring Boot. It converts long links into short, easy-to-share URLs. The system supports JWT authentication, Redis caching, PostgreSQL persistence, and documentation via Swagger or Postman.</p> </div>

> **[Leia esta documentação em português](README.md)**

## 📖 **Index**

- [Overview](#-overview)
- [Prerequisites](#-prerequisites)
- [Technologies](#-technologies)
- [Swagger](#-swagger)
- [Postman](#-postman)
- [Environment Variables](#%EF%B8%8F-environment-variables)
- [Testing](#-testing)
- [License](#-license)
- [References](#references)

## 🔭 **Overview**

ShortURL allows users to shorten URLs and track access statistics. With JWT token authentication, Redis caching, and PostgreSQL as a database, the system is scalable and secure. It also includes **Swagger** for API exploration and **Flyway** for database migration management.

### **Key Features:**

- URL shortening
- Authentication and authorization for `/actuator` using JWT
- Redis support for performance improvement
- PostgreSQL integration for persistence
- API documentation via **Swagger** or **Postman**
- Flexible configuration via **environment variables**

## <img src="https://static-00.iconduck.com/assets.00/toolbox-emoji-512x505-gpgwist1.png" width="20" height="20" alt="Toolbox"> **Prerequisites**

- **JDK** (version **21** or higher)
- **Maven** (version **3.8** or higher)
- **Spring Boot** (version **3.4.2** or higher)
- **PostgreSQL** (version **16** or higher)
- **Redis** (Optional, for caching)

## 💻 **Technologies**

- **Spring Boot** (Main framework)
- **Spring Security** (Authentication and authorization via JWT)
- **Spring Data JPA** (Relational database persistence)
- **Spring Data Redis** (Cache for URL shortening)
- **MapStruct** (DTO-to-entity conversion)
- **JWT (JJWT)** (Authentication and security)
- **Flyway** (Database migration)
- **Swagger (SpringDoc OpenAPI)** (API documentation)
- **PostgreSQL** (Primary database)
- **H2 Database** (In-memory database for testing)
- **Redis** (Optional, used for caching and performance)

## 📜 **Swagger**

API documentation is available via Swagger. To access it, enable Swagger in `application.yml` and `SecurityConfig`, start the system, and visit:

🔗 **`http://localhost:8000/swagger-ui.html`**


## 🔗 **Postman**

A Postman test collection is available:

[Postman Collection](https://www.postman.com/sam-goldman11/programs-of-mapple/collection/r2yhoqi/url-shortener)


## ⚙️ **Environment Variables**

|**Description**|**Parameter**|**Default Value**|
|---|---|---|
|Server port|`SERVER_PORT`|`8000`|
|JWT secret key|`SECRET-KEY`|`NONE`|
|Password for accessing `/actuator`|`SECURITY_PASSWORD`|`NONE`|
|Database URL|`DATASOURCE_URL`|`NONE`|
|Database user|`DATASOURCE_USER`|`NONE`|
|Database password|`DATASOURCE_PASSWORD`|`NONE`|
|Max connection lifetime|`DATASOURCE_MAXLIFETIME`|`300000`|
|Idle timeout for connections|`DATASOURCE_IDLE-TIMEOUT`|`180000`|
|Clean expired URLs|`EXPIRED_DATA`|`1200000`|
|Redis username|`REDIS_USERNAME`|`NONE`|
|Redis host|`REDIS_HOST`|`NONE`|
|Redis password|`REDIS_PASSWORD`|`NONE`|
|Redis port|`REDIS_PORT`|`NONE`|
|Redis SSL|`REDIS_SSL`|`NONE`|
|Redis connection check interval|`REDIS_CONNECTION`|`300000`|
|Sync Redis data with database|`REDIS_SYNC`|`300000`|


## 🧪 **Testing**

To run tests, ensure the **requirements** are met and execute:

> Check the [requirements](#-prerequisites)

```
mvn test
```

## 🔬 **Test Environment Variables**

|**Description**|**Parameter**|**Default Value**|
|---|---|---|
|Server port|`SERVER_PORT`|`8000`|
|JWT secret key|`SECRET-KEY`|`NONE`|
|Password for `/actuator`|`SECURITY_PASSWORD`|`NONE`|
|Expired URL cleanup time|`EXPIRED_DATA`|`1200000`|
|Redis test username|`REDIS_USERNAME_TEST`|`NONE`|
|Redis test host|`REDIS_HOST_TEST`|`NONE`|
|Redis test password|`REDIS_PASSWORD_TEST`|`NONE`|
|Redis test port|`REDIS_PORT_TEST`|`NONE`|
|Redis test SSL|`REDIS_SSL_TEST`|`NONE`|
|Redis connection check interval|`REDIS_CONNECTION`|`300000`|
|Sync Redis data with database|`REDIS_SYNC`|`300000`|

---

## 📄 **License**

This project is licensed under the **BSD 2-Clause License**. For more details, see the [LICENSE](LICENSE) file.

🔗 **[BSD 2-Clause License](https://opensource.org/license/bsd-2-clause)**

---

## 📌 **References**

> Based on the original project by **[Zeeshaan Ahmad](https://github.com/zeeshaanahmad/url-shortener)**.