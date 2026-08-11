import { useEffect, useState } from "react";
import {
    CartesianGrid,
    Legend,
    Line,
    LineChart,
    ResponsiveContainer,
    Tooltip,
    XAxis,
    YAxis,
} from "recharts";

type SensorReading = {
    id: number;
    deviceId: string;
    temperature: number;
    humidity: number;
    status: string;
    timestamp: string;
};

type ChartReading = {
    time: string;
    temperature: number;
    humidity: number;
};

const API_URL = "http://localhost:8080/api/readings";

function SensorChart() {
    const [chartData, setChartData] = useState<ChartReading[]>([]);

    useEffect(() => {
        async function loadChartData() {
            try {
                const response = await fetch(API_URL);

                if (!response.ok) {
                    throw new Error("Unable to load chart data");
                }

                const readings: SensorReading[] = await response.json();

                const data = readings
                    .slice(0, 20)
                    .reverse()
                    .map((reading) => ({
                        time: new Date(reading.timestamp).toLocaleTimeString(),
                        temperature: reading.temperature,
                        humidity: reading.humidity,
                    }));

                setChartData(data);
            } catch {
                setChartData([]);
            }
        }

        loadChartData();

        const intervalId = window.setInterval(loadChartData, 5000);

        return () => {
            window.clearInterval(intervalId);
        };
    }, []);

    return (
        <section className="chart-panel">
            <div className="panel-header">
                <div>
                    <p className="eyebrow">Trends</p>
                    <h3>Temperature and humidity</h3>
                </div>

                <span className="panel-badge">Last 20 readings</span>
            </div>

            <div className="chart-container">
                {chartData.length === 0 ? (
                    <div className="chart-empty">
                        Waiting for enough sensor data...
                    </div>
                ) : (
                    <ResponsiveContainer width="100%" height={320}>
                        <LineChart
                            data={chartData}
                            margin={{
                                top: 12,
                                right: 12,
                                left: 0,
                                bottom: 4,
                            }}
                        >
                            <CartesianGrid
                                stroke="#1e293b"
                                strokeDasharray="4 4"
                            />

                            <XAxis
                                dataKey="time"
                                stroke="#64748b"
                                tick={{ fill: "#64748b", fontSize: 11 }}
                            />

                            <YAxis
                                stroke="#64748b"
                                tick={{ fill: "#64748b", fontSize: 11 }}
                            />

                            <Tooltip
                                contentStyle={{
                                    border: "1px solid #334155",
                                    borderRadius: "10px",
                                    backgroundColor: "#0f172a",
                                    color: "#e2e8f0",
                                }}
                            />

                            <Legend />

                            <Line
                                type="monotone"
                                dataKey="temperature"
                                name="Temperature °C"
                                stroke="#60a5fa"
                                strokeWidth={3}
                                dot={{ r: 3, fill: "#60a5fa" }}
                                activeDot={{ r: 6 }}
                            />

                            <Line
                                type="monotone"
                                dataKey="humidity"
                                name="Humidity %"
                                stroke="#34d399"
                                strokeWidth={3}
                                dot={{ r: 3, fill: "#34d399" }}
                                activeDot={{ r: 6 }}
                            />
                        </LineChart>
                    </ResponsiveContainer>
                )}
            </div>
        </section>
    );
}

export default SensorChart;