import { useEffect, useState } from "react";

type AlertEvent = {
    id: number;
    sensorReadingId: number;
    deviceId: string;
    alertType: string;
    message: string;
    actualValue: number;
    thresholdValue: number;
    createdAt: string;
    acknowledged: boolean;
};

const ALERTS_API_URL = "http://localhost:8080/api/alerts/active";

function AlertPanel() {
    const [alerts, setAlerts] = useState<AlertEvent[]>([]);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        async function loadActiveAlerts() {
            try {
                const response = await fetch(ALERTS_API_URL);

                if (!response.ok) {
                    throw new Error(
                        `Alert API request failed with status ${response.status}`
                    );
                }

                const data: AlertEvent[] = await response.json();

                setAlerts(data);
                setError(null);
            } catch {
                setError("Unable to load active alerts.");
            }
        }

        loadActiveAlerts();

        const intervalId = window.setInterval(
            loadActiveAlerts,
            5000
        );

        return () => {
            window.clearInterval(intervalId);
        };
    }, []);

    async function acknowledgeAlert(alertId: number) {
        try {
            const response = await fetch(
                `http://localhost:8080/api/alerts/${alertId}/acknowledge`,
                {
                    method: "PATCH",
                }
            );

            if (!response.ok) {
                throw new Error("Unable to acknowledge alert");
            }

            setAlerts((currentAlerts) =>
                currentAlerts.filter((alert) => alert.id !== alertId)
            );
        } catch {
            setError("Unable to acknowledge alert.");
        }
    }


    return (
        <section
            className={`alert-panel ${
                alerts.length > 0
                    ? "alert-panel-warning"
                    : "alert-panel-normal"
            }`}
        >
            <div className="alert-summary">
                <div className="alert-panel-header">
                    <div>
                        <p className="eyebrow">
                            {alerts.length > 0
                                ? "Active alerts"
                                : "Alert status"}
                        </p>

                        <h3>
                            {alerts.length > 0
                                ? `${alerts.length} active alert${
                                    alerts.length === 1 ? "" : "s"
                                }`
                                : "All conditions normal"}
                        </h3>
                    </div>

                    <div className="alert-panel-icon">
                        {alerts.length > 0 ? "!" : "✓"}
                    </div>
                </div>

                {error && (
                    <p className="alert-api-error">
                        {error}
                    </p>
                )}

                {alerts.length === 0 && !error ? (
                    <p className="alert-panel-message">
                        No unacknowledged alerts are currently stored.
                    </p>
                ) : null}
            </div>

            {alerts.length > 0 ? (
                <div className="alert-list">
                    {alerts.map((alert) => (
                        <article className="alert-item" key={alert.id}>
                            <div className="alert-item-content">
                                <strong>{alert.alertType}</strong>
                                <p>{alert.message}</p>
                                <span>
                {alert.deviceId} ·{" "}
                                    {new Date(alert.createdAt).toLocaleTimeString()}
              </span>
                            </div>

                            <div className="alert-item-actions">
                                <div className="alert-values">
                                    <span>Actual: {alert.actualValue}</span>
                                    <span>Limit: {alert.thresholdValue}</span>
                                </div>

                                <button
                                    type="button"
                                    onClick={() => acknowledgeAlert(alert.id)}
                                >
                                    Acknowledge
                                </button>
                            </div>
                        </article>
                    ))}
                </div>
            ) : null}
        </section>
    );
}

export default AlertPanel;