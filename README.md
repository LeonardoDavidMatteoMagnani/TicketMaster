# Ticketmaster Setup Guide

**Note**: This project was developed as part of a university Database exam.

This guide outlines the steps required to configure and start the **Ticketmaster** program.

## Prerequisites

- **MySQL**: MySQL Installer for Windows, MySQL Workbench, and MySQL Server.
- **Java Development Kit (JDK)**: Recommended version `OpenJDK 17.0.6`.
- **Development Environment**: Recommended [Visual Studio Code](https://code.visualstudio.com/) with the Java extension.

## Installation and Configuration Steps

### 1. Install MySQL
- Download and install MySQL Installer for Windows.
- During installation, ensure you include **MySQL Workbench** and **MySQL Server**.

### 2. Configure MySQL Server
- Accept the default base configurations.
- Set the password: `RootPassword`.

### 3. Configure MySQL Workbench
1. Launch MySQL Workbench.
2. Create a new connection with the following settings:
    - **Connection Name**: `ticketmaster`
    - **Username**: `root`
    - **Hostname**: `localhost`
    - **Port**: `3306`
3. Within the newly created connection, create a new schema named `ticketmaster`.

### 4. Verify and Start the MySQL Service
1. Ensure the `MySQL80` service is running:
   - Open the **Task Manager** (Control Panel).
   - Locate the `MySQL80` service in the services window.
   - If its status is "Stopped," right-click and select `Start`.
   
### 5. Install the Java Development Kit (JDK)
- Verify JDK installation by opening the terminal and typing:
    ```bash
    java -version
    ```
- If not installed, download and install `OpenJDK 17.0.6`.

### 6. Start the Program
- Use any development environment of your choice. It has been successfully tested with **Visual Studio Code** and the Java extension.
- Once the program is running, open a browser and visit: [http://localhost:8080/](http://localhost:8080/).

## Final Notes

- Make sure to follow all steps carefully to avoid errors.
- For issues or questions, refer to the official documentation for MySQL or Java.

---
