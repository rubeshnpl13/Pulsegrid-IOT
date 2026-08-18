PulseGrid
A real-time IoT sensor data pipeline. Simulated sensors publish JSON readings over MQTT, a Java Spring Boot backend subscribes to the broker, deserializes the messages, and persists them to PostgreSQL — with a REST API on top for querying the stored data.

Architecture



Tech Stack
Component	Technology
Sensor simulator	Python (paho-mqtt)
Message broker	Eclipse Mosquitto (MQTT)
Backend	Java 19, Spring Boot
Persistence	Spring Data JPA / Hibernate
Database	PostgreSQL 17 (Docker)
Containerization	Docker
Features
End-to-end streaming pipeline from simulated sensors to persistent storage

Decoupled, publish/subscribe messaging via MQTT

Automatic JSON deserialization into typed sensor reading models

REST API with full CRUD operations for sensor readings

Containerized PostgreSQL for a reproducible local setup

Prerequisites
Java 19+

Docker

Python 3.x with paho-mqtt (pip install paho-mqtt)

Getting Started
Clone the repository

bash
git clone https://github.com/<your-username>/pulsegrid.git
cd pulsegrid
Start PostgreSQL in Docker

bash
docker run --name pulsegrid-db \
  -e POSTGRES_DB=pulsegrid \
  -e POSTGRES_USER=pulsegrid \
  -e POSTGRES_PASSWORD=secret \
  -p 5432:5432 -d postgres:17
Start the Mosquitto broker

bash
docker run -it --name pulsegrid-broker -p 1883:1883 eclipse-mosquitto
Run the Spring Boot backend

bash
./mvnw spring-boot:run
Run the sensor simulator

bash
python simulator/sensor_simulator.py
Readings should now flow from the simulator through MQTT into the database.

Configuration
Setting	Default	Description
MQTT_BROKER_URL	tcp://localhost:1883	Mosquitto broker address
MQTT_TOPIC	pulsegrid/sensors	Topic the backend subscribes to
DB_URL	jdbc:postgresql://localhost:5432/pulsegrid	JDBC connection string
DB_USER / DB_PASSWORD	pulsegrid / secret	Database credentials
MQTT Message Format
The simulator publishes JSON payloads like this:

json
{
  "sensorId": "sensor-001",
  "type": "temperature",
  "value": 23.5,
  "unit": "°C",
  "timestamp": "2026-08-16T02:30:00Z"
}
REST API
Method	Endpoint	Description
GET	/api/readings	List all sensor readings
GET	/api/readings/{id}	Get a single reading
GET	/api/readings/sensor/{sensorId}	Readings for one sensor
DELETE	/api/readings/{id}	Delete a reading
Project Structure
text
pulsegrid/
├── simulator/            # Python sensor simulator
│   └── sensor_simulator.py
├── src/main/java/        # Spring Boot backend
│   └── .../controller/   # REST controllers
│   └── .../service/      # SensorReadingService
│   └── .../model/        # JPA entities
│   └── .../mqtt/         # MQTT subscriber / config
├── src/main/resources/
│   └── application.properties
└── pom.xml

Roadmap
Docker Compose setup to run the whole stack with one command

Live dashboard for visualizing readings

Support for additional sensor types and alerting thresholds

Cloud deployment
