import { useEffect, useState } from "react";
import "./App.css";
import ReadingTable from "./components/ReadingTable";

type SensorReading = {
  id: number;
  deviceId: string;
  temperature: number;
  humidity: number;
  status: string;
  timestamp: string;
};

const API_URL = "http://localhost:8080/api/readings/latest";

function App() {
  const [reading, setReading] = useState<SensorReading | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function loadLatestReading() {
      try {
        const response = await fetch(API_URL);

        if (!response.ok) {
          throw new Error(`API request failed with status ${response.status}`);
        }

        const data: SensorReading = await response.json();

        setReading(data);
        setError(null);
      } catch {
        setError("Unable to connect to the Spring Boot API.");
      } finally {
        setLoading(false);
      }
    }

    loadLatestReading();

    const intervalId = window.setInterval(
        loadLatestReading,
        5000
    );

    return () => {
      window.clearInterval(intervalId);
    };
  }, []);

  const formattedTime = reading
      ? new Date(reading.timestamp).toLocaleTimeString()
      : "Waiting for data";

  return (
      <main className="dashboard">
        <header className="topbar">
          <div>
            <p className="eyebrow">IoT monitoring platform</p>
            <h1>PulseGrid</h1>
          </div>

          <div className="connection-status">
            <span className="status-dot"></span>
            {error ? "API disconnected" : "MQTT connected"}
          </div>
        </header>

        <section className="dashboard-heading">
          <div>
            <p className="eyebrow">Overview</p>
            <h2>Sensor dashboard</h2>
            <p className="subtitle">
              Monitor connected devices and recent environmental readings.
            </p>
          </div>

          <div className="last-updated">
            <span>Last updated</span>
            <strong>{formattedTime}</strong>
          </div>
        </section>

        {error && (
            <div className="error-banner">
              {error}
            </div>
        )}

        <section className="metric-grid">
          <article className="metric-card">
            <span className="metric-label">Temperature</span>
            <strong className="metric-value">
              {loading || !reading ? "--" : `${reading.temperature} °C`}
            </strong>
            <span className="metric-caption">Latest reading</span>
          </article>

          <article className="metric-card">
            <span className="metric-label">Humidity</span>
            <strong className="metric-value">
              {loading || !reading ? "--" : `${reading.humidity} %`}
            </strong>
            <span className="metric-caption">Latest reading</span>
          </article>

          <article className="metric-card">
            <span className="metric-label">Device status</span>
            <strong className="metric-value status-text">
              {loading || !reading ? "--" : reading.status}
            </strong>
            <span className="metric-caption">
            {reading?.deviceId ?? "No device"}
          </span>
          </article>

          <article className="metric-card">
            <span className="metric-label">Reading ID</span>
            <strong className="metric-value">
              {loading || !reading ? "--" : reading.id}
            </strong>
            <span className="metric-caption">Latest database record</span>
          </article>
        </section>

        <section className="content-grid">
          <article className="panel">
            <div className="panel-header">
              <div>
                <p className="eyebrow">Live data</p>
                <h3>Latest sensor reading</h3>
              </div>

              <span className="panel-badge">
              {reading ? "API connected" : "API pending"}
            </span>
            </div>

            {reading ? (
                <div className="reading-details">
                  <div className="reading-row">
                    <span>Device ID</span>
                    <strong>{reading.deviceId}</strong>
                  </div>

                  <div className="reading-row">
                    <span>Temperature</span>
                    <strong>{reading.temperature} °C</strong>
                  </div>

                  <div className="reading-row">
                    <span>Humidity</span>
                    <strong>{reading.humidity} %</strong>
                  </div>

                  <div className="reading-row">
                    <span>Timestamp</span>
                    <strong>{formattedTime}</strong>
                  </div>
                </div>
            ) : (
                <div className="empty-state">
                  <div className="empty-icon">⌁</div>
                  <h4>Waiting for sensor data</h4>
                  <p>
                    The dashboard is trying to load the latest reading from the
                    Spring Boot API.
                  </p>
                </div>
            )}
          </article>

          <article className="panel">
            <div className="panel-header">
              <div>
                <p className="eyebrow">System</p>
                <h3>Platform status</h3>
              </div>
            </div>

            <div className="system-list">
              <div className="system-row">
                <span>MQTT broker</span>
                <span className="healthy">Healthy</span>
              </div>

              <div className="system-row">
                <span>Spring Boot API</span>
                <span className={error ? "pending" : "healthy"}>
                {error ? "Unavailable" : "Healthy"}
              </span>
              </div>

              <div className="system-row">
                <span>PostgreSQL</span>
                <span className="healthy">Healthy</span>
              </div>

              <div className="system-row">
                <span>Frontend API connection</span>
                <span className={reading ? "healthy" : "pending"}>
                {reading ? "Connected" : "Pending"}
              </span>
              </div>
            </div>
          </article>
        </section>
        <ReadingTable />
      </main>
  );
}

export default App;