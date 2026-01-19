# PasswordHacker

A client-server application demonstrating brute-force password cracking. The server generates login using dictionary and a random password and validates authentication attempts, while the client ("hacker") attempts to guess the password using a dictionary attack and brute force.

![Java](https://img.shields.io/badge/Java-17%2B-orange?logo=openjdk)
![Status](https://img.shields.io/badge/Status-Complete-brightgreen)
![Architecture](https://img.shields.io/badge/Architecture-OOP-blue)

## Project Architecture

```
├── src/main/
│   ├── java/hacker/              # Client side (attacker)
│   │   ├── Connection.java       # Server connection management
│   │   ├── FileManager.java      # Client file management
│   │   ├── Main.java             # Client entry point
│   │   ├── PassHacker.java       # Password cracking logic
│   │   └── RequestManager.java   # Outcoming request management
│   │
│   ├── java/model/              # Data models (shared)
│   │   ├── AuthRequest.java     # Authentication request model
│   │   └── AuthResponse.java    # Authentication response model
│   │
│   ├── java/server/             # Server side
│   │   ├── FileManager.java     # Server file management
│   │   ├── Main.java            # Server entry point
│   │   └── RequestManager.java  # Incoming request management
│   │
│   └── resources/               # Application resources
│       └── passwords.txt        # Password dictionary file
│       └── logins.txt           # Logins dictionary file
├── build.gradle                 # Gradle build configuration
├── settings.gradle              # Gradle project settings
├── gradlew                      # Gradle wrapper (Unix/Linux)
└── gradlew.bat                  # Gradle wrapper (Windows)
```

## Configuration

### Server Parameters
- Default server address: `http://localhost:23456`
- Request/Response format: `JSON`

## Usage

### Server Mode
The server starts on the specified port and:
1. Generates a login using dictionary and random password on startup
2. Listens for requests
3. Validates submitted passwords
4. Returns JSON response with validation result

### Client Mode (Hacker)
The client connects to the server and:
1. Loads logins dictionary from file
2. Generates combinations with different register of chars
2. Sequentially sends each login and password to the server
3. Analyzes server responses
4. Stops when login and password is successfully guessed
