export function formatDates(startDate, endDate) {
  const formattedStart = startDate
    ? startDate.toISOString().slice(0, 10)
    : null;

  const formattedEnd = endDate
    ? endDate.toISOString().slice(0, 10)
    : null;

  return { formattedStart, formattedEnd };
}
