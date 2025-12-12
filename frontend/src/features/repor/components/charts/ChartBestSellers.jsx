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
  indexAxis: 'y',
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      display: false,
    },
    tooltip: {
      callbacks: {
        label: function (context) {
          return ` Vendidos: ${context.parsed.x}`;
        }
      }
    }
  },
  scales: {
    x: {
      beginAtZero: true,
      grid: {
        display: true,
        drawBorder: false,
        color: 'rgba(0, 0, 0, 0.1)',
      },
      ticks: {
        stepSize: 10
      }
    },
    y: {
      grid: {
        display: false,
        drawBorder: false,
      },
    }
  },
  ticks: {
    color: "#475569",     // slate-600
    font: {
      size: 14,
      weight: "500",
    }
  }
};

const colors = [
  '#436086',
  '#567AA1',
  '#7797B9',
  '#A7BBD2',
  '#D0DAE7',
];

export default function BestSellersChart({ products = [], showPlaceholder = true }) {
  const isEmpty = showPlaceholder && (!products || products.length === 0);

  const displayProducts = isEmpty
    ? [
      { productName: '-', quantitySold: 61 },
      { productName: '-', quantitySold: 55 },
      { productName: '-', quantitySold: 49 },
      { productName: '-', quantitySold: 45 },
      { productName: '-', quantitySold: 41 },
    ]
    : [...products]
      .sort((a, b) => b.quantitySold - a.quantitySold)
      .slice(0, 5);

  const labels = displayProducts.map(product => product.productName);
  const soldQuantities = displayProducts.map(product => product.quantitySold);

  const maxValue = Math.max(...soldQuantities, 0);
  const dynamicMax = Math.ceil(maxValue * 1.2);

    const data = {
    labels,
    datasets: [
      {
        label: 'Productos vendidos',
        data: soldQuantities,
        backgroundColor: isEmpty ? Array(5).fill('#CBD5E1') : Array(displayProducts.length).fill('#436086'),
        borderColor: 'rgba(0, 0, 0, 0)',
        borderWidth: 1,
        borderRadius: 4,
      },
    ],
  };

  const chartOptions = {
    ...options,
    plugins: {
      ...options.plugins,
      tooltip: {
        ...options.plugins.tooltip,
        enabled: !isEmpty, // Disable tooltip for empty state
      }
    },
    scales: {
      ...options.scales,
      x: {
        ...options.scales.x,
        max: dynamicMax
      }
    }
  };

  return <Bar options={chartOptions} data={data} />;
}
