import { Badge } from "@/shared/components/ui/badge";

function InfoCard({ title, badgeIcon: Icon, quantity, description, className }) {
  return (
    <div className={`max-h-44 flex flex-col gap-4 p-6 border border-stokia-neutral-200 rounded-xl bg-stokia-neutral-50 ${className}`}>
      <div className="flex flex-col gap-2.5">
        <Badge variant={"secondary"}>
          <Icon />
          {title}
        </Badge>
        <p className="text-5xl font-bold text-stokia-neutral-950">{quantity}</p>
      </div>
      <p className="font-medium text-stokia-neutral-950">{description}</p>
    </div>
  )
}

export default InfoCard;
