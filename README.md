<div align = "center">
<a>
<img src="logo.png" alt="Logo" width="80" height="80">
</a>
<br>
<p>ShortURL é um encurtador de URLs eficiente e seguro, desenvolvido em Spring Boot, que permite converter links longos em URLs curtas e fáceis de compartilhar. O sistema suporta autenticação JWT, cache com Redis, persistência no PostgreSQL e documentação via Swagger ou Postman.
</p>
</div>

> **[Read this Documentation in English](README-EN.md)**

## 📖 **Índice**

- [Visão Geral](#-vis%C3%A3o-geral)
- [Pré-requisitos](#-pr%C3%A9-requisitos)
- [Tecnologias](#-tecnologias)
- [Swagger](#-swagger)
- [Postman](#-postman)
- [Variáveis do Ambiente](#%EF%B8%8F-variaveis-do-ambiente)
- [Testes](#-testes)
- [Licença](#-licen%C3%A7a)
- [Referências](#references)
## 🔭 **Visão Geral**

ShortURL permite aos usuários encurtar URLs e acompanhar estatísticas sobre os acessos. Com suporte a tokens JWT para autenticação, cache via Redis e banco de dados PostgreSQL, ele é escalável e seguro. O sistema também inclui **Swagger** para explorar as APIs e **Flyway** para controle de migração do banco.

**Principais Funcionalidades:**  
- Encurtamento de URLs.
- Autenticação e autorização do `/actuator` via JWT.
- Suporte a Redis para melhorar o desempenho.
- Integração com PostgreSQL para persistência.
- API documentada via **Swagger** ou **Postman**.
- Configurações flexíveis via **variáveis de ambiente**.

---
## <img src="https://static-00.iconduck.com/assets.00/toolbox-emoji-512x505-gpgwist1.png" width="20" height="20" alt="Toolbox"> **Pré-requisitos**

- **JDK** (versão **21** ou superior)
- **Maven** (versão **3.8** ou superior)
- **Spring Boot** (versão **3.4.2** ou superior)
- **PostgreSQL** (versão **16** ou superior)
- **Redis** (Opcional, para cache)

---
## 💻 **Tecnologias**

- **Spring Boot** (Framework principal)
- **Spring Security** (Autenticação e autorização via JWT)
- **Spring Data JPA** (Persistência no banco relacional)
- **Spring Data Redis** (Cache para encurtamento de URLs)
- **MapStruct** (Conversão entre DTOs e entidades)
- **JWT (JJWT)** (Autenticação e segurança)
- **Flyway** (Migração de banco de dados)
- **Swagger (SpringDoc OpenAPI)** (Documentação da API)
- **PostgreSQL** (Banco de dados principal)
- **H2 Database** (Banco em memória para testes)
- **Redis** (Opcional, usado para cache e performance)
- **Prometheus** (Opcional, usado para monitoramento)

---
## 📜**Swagger**

O projeto disponibiliza documentação para a API via Swagger. Para acessar, reative o swagger no `application.yml` e no `SecurityConfig` inicie o sistema e vá até:

🔗 **`http://localhost:8000/swagger-ui.html`**

---
## 🔗**Postman**

Coleção de testes para **Postman** disponível:

[Postman Collection](https://www.postman.com/sam-goldman11/programs-of-mapple/collection/r2yhoqi/url-shortener)

---
## <img src="https://img.icons8.com/plasticine/100/java-coffee-cup-logo.png" alt="java-coffee-cup-logo" width="50" height="50" style="position: relative; top: 10px;">**Instalação do Programa**

Configure os arquivos disponíveis com as variáveis de ambiente detalhadas no arquivo `config.args` para deploy. Caso esteja realizando testes, utilize `testes.args` para configurar os parâmetros adequados para um ambiente de teste ou definir os atributos diretamente na IDE na versão mais recente.

Para inicializar o programa de forma automática com as variáveis de ambiente configuradas, utilize um dos seguintes scripts disponíveis:

- **`run.bat`** (Para Windows)
- **`run.ps1`** (Para Windows PowerShell)

Esses scripts garantem que todas as variáveis de ambiente sejam carregadas corretamente antes da execução do programa Java.

---
## <img><img src="https://user-images.githubusercontent.com/11943860/46922575-7017cf80-cfe1-11e8-845a-0cd198fb546c.png" alt="java-coffee-cup-logo" width="30" height="30" style="position: relative; top: 5px;"> **Eclipse IDE** 

O projeto inclui arquivos de configuração para o **Eclipse IDE**, que podem ser importados diretamente na **Run Configuration**. Isso facilita o carregamento automático das variáveis de ambiente, alternar entre os diferentes tipos de implementação, como:

- **Ambiente de Desenvolvimento**
- **Ambiente de Testes**
- **Deploy em Produção**

---
## ⚙️ **Variaveis do Ambiente**

| **Description**                                          | **Parameter**          | **Default values** |
| -------------------------------------------------------- | ---------------------- | ------------------ |
| `Porta do servidor`                                      | `SERVER_PORT`          | `8000`             |
| `Chave Secreta para JWT autenticar o /actuator`          | `SECRET-KEY`           | `NONE`             |
| `Senha para acessar o /actuator`                         | `SECURITY_PASSWORD`    | `NONE`             |
| `URL do banco de dados`                                  | `DB_HOST`              | `LOCALHOST`        |
| `Porta do banco de dados`                                | `DB_PORT`              | `5432`             |
| `Nome da Database do banco de dados`                     | `DB_DATABASE`          | `example`          |
| `Usuário do banco de dados`                              | `DB_USER`              | `NONE`             |
| `Senha do banco de dados`                                | `DB_PASSWORD`          | `NONE`             |
| `Tempo máximo de vida da conexão`                        | `DB_MAXLIFETIME`       | `300000`           |
| `Tempo limite para conexões inativas`                    | `DB_IDLE-TIMEOUT`<br>  | `180000`<br>       |
| `Clean expired URL's`                                    | `EXPIRED_DATA`         | `1200000`          |
| `Usuário do Redis`                                       | `REDIS_USERNAME`       | `NONE`             |
| `Host do Redis`                                          | `REDIS_HOST`           | `NONE`             |
| `Senha do Redis`                                         | `REDIS_PASSWORD`       | `NONE`             |
| `Porta do Redis`                                         | `REDIS_PORT`           | `NONE`             |
| `SSL do Redis`                                           | `REDIS_SSL`            | `NONE`             |
| `Tempo para verificar conexão Redis`                     | `REDIS_CONNECTION`<br> | `300000`<br>       |
| `Sincronização de dados Redis -> Banco`                  | `REDIS_SYNC`           | `300000`           |
| `Prometheus logging com docker ou no aws` - Experimental | `PROMETHEUS_REGION`    | `NONE`             |
| `Prometheus logging com docker ou no aws` - Experimental | `REMOTE_WRITE_URL`     | `NONE`             |
| `Habilitar ou desabilitar o Swagger`                     | `SWAGGER_ENABLE`       | `false`            |
| `Url do Swagger`                                         | `SWAGGER_URL`          | `/docs`            |

---
## **Testes**
Para executar os testes, rode com os parametros especificos para ambiente de testes e utilize o seguinte comando:
>🚨 cheque os [requisitos](#-pr%C3%A9-requisitos)

```
mvn test
```

---
## **Variaveis do Ambiente para Testes**

| **Description**                              | **Parameter**         | **Default values** |
| -------------------------------------------- | --------------------- | ------------------ |
| `Porta do servidor`                          | `SERVER_PORT`         | `8000`             |
| `Chave secreta de Autenticação do /actuator` | `SECRET-KEY`          | `NONE`             |
| `Senha para acessar o /actuator`             | `SECURITY_PASSWORD`   | `NONE`             |
| `Usuário do Redis`                           | `REDIS_USERNAME_TEST` | `NONE`             |
| `Host do Redis`                              | `REDIS_HOST_TEST`     | `NONE`             |
| `Senha do Redis`                             | `REDIS_PASSWORD_TEST` | `NONE`             |
| `Porta do Redis`                             | `REDIS_PORT_TEST`     | `NONE`             |
| `SSL do Redis`                               | `REDIS_SSL_TEST`      | `NONE`             |

---
## 📄 **Licença**

Este projeto está licenciado sob a **BSD 2-Clause License**. Para mais detalhes, consulte o arquivo [LICENSE](LICENSE).

>🔗 **[BSD 2-Clause License](https://opensource.org/license/bsd-2-clause)**

---
## 📌**Referencias**

>Baseado no projeto original de **[Zeeshaan Ahmad](https://github.com/zeeshaanahmad/url-shortener)**.