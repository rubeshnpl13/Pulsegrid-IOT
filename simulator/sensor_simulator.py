import json
import random
from datetime import datetime, timezone

import paho.mqtt.client as mqtt


BROKER_HOST = "localhost"
BROKER_PORT = 1883
MQTT_TOPIC = "sensors/temperature"
DEVICE_ID = "sensor-01"


temperature = round(random.uniform(20.0, 30.0), 1)
humidity = round(random.uniform(40.0, 70.0), 1)

reading = {
    "deviceId": DEVICE_ID,
    "temperature": temperature,
    "humidity": humidity,
    "status": "ONLINE",
    "timestamp": datetime.now(timezone.utc).isoformat()
}

payload = json.dumps(reading)

client = mqtt.Client(
    mqtt.CallbackAPIVersion.VERSION2,
    client_id="pulsegrid-simulator"
)

client.connect(BROKER_HOST, BROKER_PORT, keepalive=60)
client.loop_start()

publish_result = client.publish(
    MQTT_TOPIC,
    payload,
    qos=1
)

publish_result.wait_for_publish()

print(f"Published: {payload}")

client.loop_stop()
client.disconnect()