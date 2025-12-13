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
          if (context.parsed.y !== null) {
            return `Precio $${context.parsed.y.toFixed(2)}`;
          }
          return '';
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
      tension: 0.4,
    },
    point: {
      radius: 0,
      hitRadius: 15,
      hoverRadius: 6,
      hoverBackgroundColor: '#5F7B99',
    }
  }
};

const labels = ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'];

export default function ChartPricing({ dataPoints, showPlaceholder = true }) {
  const validDataPoints = dataPoints && dataPoints.length > 0 ? dataPoints : Array(12).fill(0);

  const isPlaceholder = showPlaceholder && (validDataPoints.length === 0 || validDataPoints.every(v => Number(v) === 0));
  const placeholderSample = Array(12).fill(0);
  const displayData = isPlaceholder ? placeholderSample : validDataPoints;

  const localOptions = {
    ...options,
    plugins: {
      ...options.plugins,
      tooltip: {
        ...options.plugins.tooltip,
        enabled: !isPlaceholder,
      }
    }
  };

  const data = {
    labels,
    datasets: [
      {
        label: 'Precio',
        data: displayData,
        borderColor: '#436086',
        backgroundColor: 'rgba(67,96,134,0.08)',
        borderWidth: 2,
      },
    ],
  };

  return <Line key={JSON.stringify(displayData)} options={localOptions} data={data} />;
}
