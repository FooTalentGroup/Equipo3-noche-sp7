import React from 'react';
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend,
} from 'chart.js';
import { Bar } from 'react-chartjs-2';

ChartJS.register(
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend
);

export const options = {
    responsive: true,
    maintainAspectRatio: false,
    interaction: {
        mode: 'index',
        intersect: false,
    },
    plugins: {
        legend: {
            display: false,
        },
        tooltip: {
            enabled: true,
            mode: 'index',
            intersect: false,
            backgroundColor: 'rgba(255, 255, 255, 0.95)',
            titleColor: '#1e293b',
            bodyColor: '#475569',
            borderColor: '#e2e8f0',
            borderWidth: 1,
            padding: 12,
            displayColors: true,
            callbacks: {
                label: function (context) {
                    return `Stock actual ${context.parsed.y}`;
                }
            }
        },
    },
    scales: {
        x: {
            grid: {
                display: false,
            },
        },
        y: {
            beginAtZero: true,
            grid: {
                color: '#f1f5f9',
            },
            border: {
                display: false,
            }
        },
    },
};

export default function ChartStock({ dataPoints, labels }) {
    const validDataPoints = dataPoints && dataPoints.length > 0 ? dataPoints : [];
    const validLabels = labels && labels.length > 0 ? labels : [];

    const data = {
        labels: validLabels,
        datasets: [
            {
                label: 'Stock actual',
                data: validDataPoints,
                backgroundColor: '#5F7B99',
                borderColor: '#5F7B99',
                borderWidth: 1,
            },
        ],
    };

    return <Bar key={JSON.stringify(validDataPoints)} options={options} data={data} />;
}
