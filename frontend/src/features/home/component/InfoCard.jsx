import { Link } from "react-router";

function InfoCard({ title, href, badgeIcon: Icon, quantity, description, className }) {
  return (
    <Link to={href} className={`group max-h-48 flex flex-col gap-4 p-6 border border-stokia-neutral-200 rounded-xl bg-stokia-neutral-50 shadow-lg hover:bg-stokia-neutral-300/20 transition-all ${className}`}>
      <div className="flex flex-col gap-2">
        <div className=" flex justify-between text-xl items-center gap-2">
          {title}
          <span className="p-2.5 bg-stokia-primary-200 rounded-xl group-hover:shadow-sm transition-shadow">
            <Icon className="text-stokia-primary-600" />
          </span>
        </div>
        <p className="text-5xl font-bold text-stokia-neutral-950">{quantity}</p>
      </div>
      <p className="font-medium text-stokia-neutral-950">{description}</p>
    </Link>
  )
}

export default InfoCard;
