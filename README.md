# Virtual Power Plant (VPP) Application

This project is a Spring Boot application designed to manage batteries in a virtual power plant. It provides RESTful APIs to save battery data and retrieve battery statistics based on various filters.

## API Endpoints
### Save Battery Data
- **POST** `/vpp/api/batteries`
  - Request Body: JSON array of battery data
  - Example:
    ```json
    [
        {
            "name": "Battery1",
            "postcode": "12345",
            "capacity": 100
        },
        {
            "name": "Battery2",
            "postcode": "67890",
            "capacity": 200
        }
    ]
    ```
  - Response: 201 Created

### Get Battery Statistics
- **GET** `/vpp/api/batteries`
  - Query Parameters:
    - `startPostcode` and `endPostcode`: Range of postcodes (inclusive).
    - `minCapacity`: Minimum capacity
    - `maxCapacity`: Maximum capacity
  - Response: JSON object with statistics
  - Example:
    ```json
    { 
      "batteryNames": ["Battery1", "Battery2"],
      "totalCapacity": 150,
      "averageCapacity": 75
    }
    ```

## Prerequisites
- Java 17
- Maven 3
- MySQL 8.x
- Docker (for running Testcontainers)

## Setup Instructions

### 1. Repository URL
```bash
https://github.com/smmohiuddin/vpp.git
```

### 2. Change database configuration in application.properties
Edit the `src/main/resources/application.properties` file to set your MySQL database connection details:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/vpp
spring.datasource.username=your_username
spring.datasource.password=your_password
```
### 3. Test the Application
You can run the integration tests using:
```bash
mvn test
```

### 3. Build the Project
```bash
mvn clean install
```

### 5. Run the Application
```bash
mvn spring-boot:run
```

## Important Design Decisions
- **Testcontainers**: Used for integration testing to ensure the application works with a real MySQL database. Additionally, integration tests are implemented for both MySQL and PostgreSQL databases.
- **Batch Processing**: The application supports batch processing of battery data to improve performance when saving large datasets. Used database connection pooling to increase the throughput.
- **Common Exception handler**: A common exception handler is implemented called ``ControllerExceptionAdvice.java`` to handle exceptions in a consistent manner.
- **Jcoco**: Used for code coverage analysis. The project is configured to generate a code coverage report using JaCoCo. Achieved 92% code coverage. Report location ``/target/site/jacoco/index.html`` 
