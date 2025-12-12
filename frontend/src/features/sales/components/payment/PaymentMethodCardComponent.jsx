function PaymentMethodCard({ onClick, name, description, icon: Icon, selected }) {
  const selectedClasses = selected
    ? "border-stokia-neutral-600 bg-stokia-neutral-50 ring-2 ring-stokia-neutral-600"
    : "border-stokia-neutral-300 hover:bg-stokia-neutral-100 bg-white";

  return (
    <div
      role="button"
      onClick={onClick}
      className={`flex flex-row items-center gap-6 p-6 rounded-lg border-2 cursor-pointer transition-colors ${selectedClasses}`}
    >
      <Icon className="h-9 w-9 text-stokia-neutral-950" />
      <div className="flex flex-col gap-2">
        <p className="font-medium text-2xl">{name}</p>
        <p className="text-xl text-stokia-neutral-500">{description}</p>
      </div>
    </div>
  )
}

export default PaymentMethodCard;
