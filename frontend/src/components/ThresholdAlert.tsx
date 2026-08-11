type SensorReading = {
    temperature: number;
    humidity: number;
};

type ThresholdAlertProps = {
    reading: SensorReading | null;
};

const TEMPERATURE_LIMIT = 28;
const HUMIDITY_LIMIT = 65;

function ThresholdAlert({ reading }: ThresholdAlertProps) {
    if (!reading) {
        return null;
    }

    const alerts: string[] = [];

    if (reading.temperature > TEMPERATURE_LIMIT) {
        alerts.push(
            `Temperature is high: ${reading.temperature} °C`
        );
    }

    if (reading.humidity > HUMIDITY_LIMIT) {
        alerts.push(
            `Humidity is high: ${reading.humidity} %`
        );
    }

    const hasAlert = alerts.length > 0;

    return (
        <section
            className={`alert-panel ${hasAlert ? "alert-warning" : "alert-normal"}`}
        >
            <div className="alert-icon">
                {hasAlert ? "!" : "✓"}
            </div>

            <div className="alert-content">
                <p className="eyebrow">
                    {hasAlert ? "Threshold alert" : "Environment normal"}
                </p>

                {hasAlert ? (
                    <div className="alert-messages">
                        {alerts.map((alert) => (
                            <p key={alert}>{alert}</p>
                        ))}
                    </div>
                ) : (
                    <p className="alert-message">
                        All sensor values are currently within safe limits.
                    </p>
                )}
            </div>

            <div className="alert-limits">
                <span>Temp ≤ {TEMPERATURE_LIMIT} °C</span>
                <span>Humidity ≤ {HUMIDITY_LIMIT} %</span>
            </div>
        </section>
    );
}

export default ThresholdAlert;