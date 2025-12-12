import React from 'react';
import { Bar } from 'react-chartjs-2';
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend
} from 'chart.js';

ChartJS.register(
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend
);

const ChartSalesDaily = ({ dailySales = [] }) => {
    const labels = dailySales.map(item => {
        const date = new Date(item.date);
        return date.toLocaleDateString('es-ES', { day: '2-digit', month: 'short' });
    });

    const values = dailySales.map(item => item.totalAmount);

    const data = {
        labels,
        datasets: [
            {
                label: 'Monto',
                data: values,
                backgroundColor: '#3B82F6',
                borderRadius: 4,
                barThickness: 30,
                hoverBackgroundColor: '#2563EB',
            }
        ]
    };

    const options = {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: {
                display: false
            },
            tooltip: {
                enabled: true,
                backgroundColor: '#1F2937',
                titleColor: '#F9FAFB',
                bodyColor: '#F9FAFB',
                padding: 12,
                cornerRadius: 8,
                displayColors: false,
                callbacks: {
                    label: function (context) {
                        const value = context.raw;
                        return `Monto ${new Intl.NumberFormat('es-ES', {
                            style: 'currency',
                            currency: 'USD',
                            maximumFractionDigits: 0
                        }).format(value)}`;
                    }
                }
            }
        },
        scales: {
            x: {
                grid: {
                    display: false
                },
                ticks: {
                    color: '#6B7280',
                    font: {
                        size: 11
                    }
                }
            },
            y: {
                grid: {
                    color: '#E5E7EB',
                    drawBorder: false
                },
                ticks: {
                    color: '#6B7280',
                    font: {
                        size: 11
                    },
                    callback: function (value) {
                        return new Intl.NumberFormat('es-ES', {
                            notation: 'compact',
                            compactDisplay: 'short'
                        }).format(value);
                    }
                }
            }
        }
    };

    if (dailySales.length === 0) {
        return (
            <div className="w-full h-full flex items-center justify-center text-stokia-neutral-500">
                No hay datos de ventas diarias
            </div>
        );
    }

    return <Bar data={data} options={options} />;
};

export default ChartSalesDaily;
