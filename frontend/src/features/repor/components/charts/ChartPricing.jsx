import React from 'react';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js';
import { Line } from 'react-chartjs-2';

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

export const options = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      display: false,
    },
    tooltip: {
      backgroundColor: 'rgba(255, 255, 255, 0.9)',
      titleColor: '#1e293b',
      bodyColor: '#475569',
      borderColor: '#e2e8f0',
      borderWidth: 1,
      padding: 10,
      displayColors: true,
      callbacks: {
        label: function (context) {
          let label = context.dataset.label || '';
          if (label) {
            label += ': ';
          }
          if (context.parsed.y !== null) {
            label += new Intl.NumberFormat('es-AR', { style: 'currency', currency: 'ARS' }).format(context.parsed.y);
          }
          return label;
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
      min: 0,
      grid: {
        color: '#f1f5f9',
      },
      border: {
        display: false,
      }
    },
  },
  elements: {
    line: {
      tension: 0.4, // Smooth curve
    },
    point: {
      radius: 0, // Hide points by default
      hitRadius: 10, // Larger hit area for tooltip
      hoverRadius: 6,
    }
  }
};

const labels = ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'];

export default function ChartPricing({ dataPoints }) {
  const data = {
    labels,
    datasets: [
      {
        label: 'Costo',
        data: dataPoints || [38, 42, 45, 42, 38, 35, 30, 32, 38, 40, 41, 41],
        borderColor: '#5F7B99',
        backgroundColor: 'rgba(95, 123, 153, 0.5)',
        borderWidth: 2,
      },
    ],
  };

  return <Line options={options} data={data} />;
}
