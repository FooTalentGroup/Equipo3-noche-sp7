import React from 'react';
import { Doughnut } from 'react-chartjs-2';
import {
    Chart as ChartJS,
    ArcElement,
    Tooltip,
    Legend
} from 'chart.js';

ChartJS.register(ArcElement, Tooltip, Legend);

const PAYMENT_METHOD_LABELS = {
    'CASH': 'Efectivo',
    'CREDIT_CARD': 'Tarjeta de crédito/débito',
    'DEBIT_CARD': 'Tarjeta de crédito/débito',
    'VIRTUAL_WALLET': 'Billetera virtual'
};

const PAYMENT_METHOD_COLORS = {
    'CASH': '#1F2937',
    'CREDIT_CARD': '#6B7280',
    'DEBIT_CARD': '#6B7280',
    'VIRTUAL_WALLET': '#3B82F6'
};

const ChartPaymentMethods = ({ paymentMethods = [], totalAmount = 0 }) => {
    const labels = paymentMethods.map(item =>
        PAYMENT_METHOD_LABELS[item.paymentMethod] || item.paymentMethod
    );

    const values = paymentMethods.map(item => item.totalAmount);

    const colors = paymentMethods.map(item =>
        PAYMENT_METHOD_COLORS[item.paymentMethod] || '#9CA3AF'
    );

    const data = {
        labels,
        datasets: [
            {
                data: values,
                backgroundColor: colors,
                borderWidth: 0,
                hoverOffset: 4
            }
        ]
    };

    const options = {
        responsive: true,
        maintainAspectRatio: false,
        cutout: '70%',
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
                callbacks: {
                    label: function (context) {
                        const value = context.raw;
                        return new Intl.NumberFormat('es-ES', {
                            style: 'currency',
                            currency: 'USD',
                            maximumFractionDigits: 0
                        }).format(value);
                    }
                }
            }
        }
    };

    const formatTotal = (amount) => {
        if (amount >= 1000000) {
            return `$${(amount / 1000000).toFixed(1)}M`;
        } else if (amount >= 1000) {
            return `$${(amount / 1000).toFixed(0)} K`;
        }
        return `$${amount}`;
    };

    if (paymentMethods.length === 0) {
        return (
            <div className="w-full h-full flex flex-col items-center justify-center">
                <div className="relative w-48 h-48">
                    <Doughnut
                        data={{
                            labels: ['Sin datos'],
                            datasets: [{
                                data: [1],
                                backgroundColor: ['#E5E7EB'],
                                borderWidth: 0
                            }]
                        }}
                        options={options}
                    />
                    <div className="absolute inset-0 flex flex-col items-center justify-center">
                        <span className="text-2xl font-bold text-stokia-neutral-900">$0</span>
                        <span className="text-sm text-stokia-neutral-500">Total</span>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="w-full h-full flex flex-col">
            <div className="flex items-center gap-2 mb-4">
                <div className="w-3 h-3 bg-stokia-primary-600 rounded-sm"></div>
                <span className="text-sm text-stokia-neutral-700">Métodos de pago</span>
            </div>

            <div className="flex-1 flex items-center justify-center">
                <div className="relative w-48 h-48">
                    <Doughnut data={data} options={options} />
                    <div className="absolute inset-0 flex flex-col items-center justify-center pointer-events-none">
                        <span className="text-2xl font-bold text-stokia-neutral-900 whitespace-nowrap">{formatTotal(totalAmount)}</span>
                        <span className="text-sm text-stokia-neutral-500 mt-1">Total</span>
                    </div>
                </div>
            </div>

            <div className="mt-4 space-y-2">
                {paymentMethods.map((item, index) => (
                    <div key={index} className="flex items-center gap-2">
                        <div
                            className="w-3 h-3 rounded-sm"
                            style={{ backgroundColor: colors[index] }}
                        ></div>
                        <span className="text-sm text-stokia-neutral-700">
                            {PAYMENT_METHOD_LABELS[item.paymentMethod] || item.paymentMethod}
                        </span>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default ChartPaymentMethods;
