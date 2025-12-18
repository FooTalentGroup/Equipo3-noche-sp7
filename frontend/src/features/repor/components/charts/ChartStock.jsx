import React, { useRef, useEffect, useState, useMemo } from 'react';
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

const baseOptions = {
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

export default function ChartStock({ dataPoints, labels, forceHourly = false, fitToContainer = false, showPlaceholder = true }) {
    const containerRef = useRef(null);
    const rangeRef = useRef(null);
    const [maxIndex, setMaxIndex] = useState(0);
    const [visibleSlots, setVisibleSlots] = useState(0);
    const [leftPercent, setLeftPercent] = useState(0);

    const validDataPoints = dataPoints && dataPoints.length > 0 ? dataPoints : [];
    const validLabels = labels && labels.length > 0 ? labels : [];

    const hourLabels = Array.from({ length: 11 }, (_, i) => `${String(8 + i).padStart(2, '0')}:00`);

    const effectiveLabels = forceHourly ? hourLabels : validLabels;
    let effectiveData = validDataPoints;
    if (forceHourly) {
        if (Array.isArray(dataPoints) && dataPoints.length === hourLabels.length) {
            effectiveData = dataPoints;
        } else {
            effectiveData = Array(hourLabels.length).fill(0);
        }
    }

    const slotWidth = 56;

    const minWidth = useMemo(() => {
        if (fitToContainer) return '100%';
        return Math.max((forceHourly ? hourLabels.length : (validLabels.length || 0)) * slotWidth, 600);
    }, [validLabels.length, slotWidth, forceHourly, fitToContainer]);

    const numericData = (effectiveData || []).map(v => Number(v) || 0);
    const plotData = numericData.map(v => Math.abs(v));
    const isPlaceholder = showPlaceholder && ((!validDataPoints || validDataPoints.length === 0) || numericData.every(v => v === 0));
    const placeholderSample = [61, 55, 49, 45, 41, 38, 34, 30, 26, 22, 18];
    const placeholderData = (effectiveLabels && effectiveLabels.length <= placeholderSample.length)
        ? placeholderSample.slice(0, effectiveLabels.length)
        : Array.from({ length: effectiveLabels.length }, (_, i) => placeholderSample[i % placeholderSample.length] || 10);

    const displayData = isPlaceholder ? placeholderData : numericData;

    const data = {
        labels: effectiveLabels,
        datasets: [
            {
                label: 'Stock actual',
                data: isPlaceholder ? displayData : plotData,
                backgroundColor: isPlaceholder ? '#CBD5E1' : '#436086',
                borderColor: isPlaceholder ? '#CBD5E1' : '#436086',
                borderWidth: 1,
            },
        ],
    };

    const actualMax = (validDataPoints && validDataPoints.length) ? Math.max(...validDataPoints.map(v => Math.abs(Number(v) || 0))) : 0;
    const placeholderMax = placeholderData.length ? Math.max(...placeholderData) : 75;
    const maxVal = isPlaceholder ? placeholderMax : actualMax;
    const yMax = (maxVal <= 1) ? 75 : Math.ceil(maxVal * 1.2);
    const stepSize = Math.ceil(yMax / 6);

    const localOptions = {
        ...baseOptions,
        plugins: {
            ...baseOptions.plugins,
            tooltip: {
                ...baseOptions.plugins.tooltip,
                enabled: !isPlaceholder,
            }
        },
        scales: {
            x: {
                ...baseOptions.scales.x,
                ticks: {
                    maxRotation: 0,
                    minRotation: 0,
                }
            },
            y: {
                ...baseOptions.scales.y,
                beginAtZero: true,
                max: yMax,
                ticks: {
                    stepSize: stepSize,
                    callback: function (value) {
                        if (Number.isInteger(value)) return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.');
                        return value;
                    }
                }
            }
        }
    };

    useEffect(() => {
        const el = containerRef.current;
        if (!el) return;

        const update = () => {
            if (fitToContainer) {
                setMaxIndex(0);
                setVisibleSlots(validLabels.length || 0);
                if (rangeRef.current) {
                    rangeRef.current.max = '0';
                    rangeRef.current.value = '0';
                    setLeftPercent(0);
                }
                return;
            }
            const slots = Math.max(1, Math.floor(el.clientWidth / slotWidth));
            setVisibleSlots(slots);
            const maxIdx = Math.max(0, (validLabels.length || 0) - slots);
            setMaxIndex(maxIdx);
            const currIdx = Math.round((el.scrollLeft || 0) / slotWidth) || 0;
            if (rangeRef.current) {
                rangeRef.current.max = String(maxIdx);
                rangeRef.current.value = String(currIdx);
            }
            const left = maxIdx > 0 ? (currIdx / Math.max(1, maxIdx)) * 100 : 0;
            setLeftPercent(left);
        };

        update();
        const onScroll = () => {
            const currIdx = Math.round((el.scrollLeft || 0) / slotWidth) || 0;
            if (rangeRef.current) rangeRef.current.value = String(currIdx);
            const left = maxIndex > 0 ? (currIdx / Math.max(1, maxIndex)) * 100 : 0;
            setLeftPercent(left);
        };
        el.addEventListener('scroll', onScroll);
        window.addEventListener('resize', update);
        return () => {
            el.removeEventListener('scroll', onScroll);
            window.removeEventListener('resize', update);
        };
    }, [validLabels.length, slotWidth]);

    return (
        <div>
            <div style={{ overflow: 'hidden', width: '100%' }}>
                <div ref={containerRef} style={{ overflowX: 'auto', overflowY: 'hidden', width: '100%' }}>
                    <div style={{ minWidth: typeof minWidth === 'number' ? `${minWidth}px` : minWidth, height: 340, padding: 8 }}>
                        <Bar key={JSON.stringify({ d: validDataPoints, l: validLabels })} options={localOptions} data={data} />
                    </div>
                </div>
            </div>

            {/* Barra de scroll inferior sincronizada */}
            <div style={{ marginTop: 8 }}>
                {maxIndex > 0 ? (
                    <div style={{ position: 'relative', height: 18, display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
                        <div style={{ width: '60%', height: 8, background: '#E6EEF6', borderRadius: 6, position: 'relative' }}>
                            {/* viewport indicator */}
                            <div style={{ position: 'absolute', left: `${leftPercent}%`, transform: 'translateX(-50%)', width: `${Math.max((visibleSlots / Math.max(1, validLabels.length)) * 100, 6)}%`, height: 8, background: '#93C5FD', borderRadius: 6 }} />
                        </div>
                        <input
                            ref={rangeRef}
                            style={{ position: 'absolute', width: '60%', height: 18, opacity: 0, cursor: 'pointer' }}
                            type="range"
                            min={0}
                            max={maxIndex}
                            step={1}
                            defaultValue={0}
                            onChange={(e) => {
                                const idx = Number(e.target.value);
                                if (containerRef.current) containerRef.current.scrollLeft = idx * slotWidth;
                                const left = maxIndex > 0 ? (idx / Math.max(1, maxIndex)) * 100 : 0;
                                setLeftPercent(left);
                            }}
                        />
                    </div>
                ) : (
                    <div style={{ height: 8 }} />
                )}
            </div>
        </div>
    );
}
