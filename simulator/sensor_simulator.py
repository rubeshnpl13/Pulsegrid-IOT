import json
import random
import time
from datetime import datetime, timezone

import paho.mqtt.client as mqtt


BROKER_HOST = "localhost"
BROKER_PORT = 1883
MQTT_TOPIC = "sensors/temperature"
DEVICE_ID = "sensor-01"
PUBLISH_INTERVAL_SECONDS = 5


def create_sensor_reading():
    temperature = round(random.uniform(20.0, 30.0), 1)
    humidity = round(random.uniform(40.0, 70.0), 1)

    return {
        "deviceId": DEVICE_ID,
        "temperature": temperature,
        "humidity": humidity,
        "status": "ONLINE",
        "timestamp": datetime.now(timezone.utc).isoformat()
    }


def main():
    client = mqtt.Client(
        mqtt.CallbackAPIVersion.VERSION2,
        client_id="pulsegrid-simulator"
    )

    client.connect(BROKER_HOST, BROKER_PORT, keepalive=60)
    client.loop_start()

    print("Sensor simulator started.")
    print(f"Publishing to {MQTT_TOPIC} every {PUBLISH_INTERVAL_SECONDS} seconds.")
    print("Press Ctrl+C to stop.")

    try:
        while True:
            reading = create_sensor_reading()
            payload = json.dumps(reading)

            publish_result = client.publish(
                MQTT_TOPIC,
                payload,
                qos=1
            )

            publish_result.wait_for_publish()

            print(f"Published: {payload}")

            time.sleep(PUBLISH_INTERVAL_SECONDS)

    except KeyboardInterrupt:
        print("\nStopping sensor simulator...")

    finally:
        client.loop_stop()
        client.disconnect()
        print("Sensor simulator stopped.")


if __name__ == "__main__":
    main()