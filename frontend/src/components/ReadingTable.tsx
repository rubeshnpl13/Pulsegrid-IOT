import { useEffect, useState } from "react";

type SensorReading = {
    id: number;
    deviceId: string;
    temperature: number;
    humidity: number;
    status: string;
    timestamp: string;
};

const API_URL = "http://localhost:8080/api/readings";

function ReadingTable() {
    const [readings, setReadings] = useState<SensorReading[]>([]);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        async function loadReadings() {
            try {
                const response = await fetch(API_URL);

                if (!response.ok) {
                    throw new Error(`API request failed with status ${response.status}`);
                }

                const data: SensorReading[] = await response.json();

                setReadings(data.slice(0, 10));
                setError(null);
            } catch {
                setError("Unable to load recent readings.");
            }
        }

        loadReadings();

        const intervalId = window.setInterval(loadReadings, 5000);

        return () => {
            window.clearInterval(intervalId);
        };
    }, []);

    return (
        <section className="table-panel">
            <div className="panel-header">
                <div>
                    <p className="eyebrow">History</p>
                    <h3>Recent sensor readings</h3>
                </div>

                <span className="panel-badge">
          {readings.length} records
        </span>
            </div>

            {error && <p className="table-error">{error}</p>}

            {!error && readings.length === 0 ? (
                <div className="table-empty">
                    No sensor readings available yet.
                </div>
            ) : (
                <div className="table-wrapper">
                    <table>
                        <thead>
                        <tr>
                            <th>Device</th>
                            <th>Temperature</th>
                            <th>Humidity</th>
                            <th>Status</th>
                            <th>Time</th>
                        </tr>
                        </thead>

                        <tbody>
                        {readings.map((reading) => (
                            <tr key={reading.id}>
                                <td>{reading.deviceId}</td>
                                <td>{reading.temperature} °C</td>
                                <td>{reading.humidity} %</td>
                                <td>
                    <span className="table-status">
                      <span className="status-dot"></span>
                        {reading.status}
                    </span>
                                </td>
                                <td>
                                    {new Date(reading.timestamp).toLocaleTimeString()}
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}
        </section>
    );
}

export default ReadingTable;