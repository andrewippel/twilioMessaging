# TwilioMessaging Application
A RESTful API developed in Java 21 using Spring Boot for sending SMS messages via Twilio and managing message records with MySQL database.

## Features
- Integration with Twilio API to send SMS
- Save the messages in a MySQL database, tracking their status: SENT, DELIVERED, FAILED
- Logical deletion of messages
- Search messages by recipient number and send date
- Swagger documentation
- Unitary tests

## Requirements
- JDK 21
- Maven 3.8
- MySQL Server
- Twilio account (for real SMS sending)

## Setup Instructions
1. **Clone the repository:**

```bash
  git clone https://github.com/andrewippel/twilioMessaging.git
```

2. **Create a MySQL database:**

```bash
  CREATE DATABASE twilio_messaging_db;
```

3. **Edit the application.properties with your Twilio accountSID and authToken:**

```bash
  twilio.accountSid=your_twilio_account_sid
  twilio.authToken=your_twilio_auth_token
```

4. **Install dependencies and build:**

```bash
  mvn clean install
```

5. **Run the application (the application will start on: http://localhost:8080):**

```bash
  mvn spring-boot:run
```

6. **Access Swagger UI (API documentation):**

```bash
  http://localhost:8080/swagger-ui/index.html
```
