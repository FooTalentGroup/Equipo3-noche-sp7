import { Link } from "react-router";

function LinkCard({ title, icon: Icon, className, href, description }) {
  return (
    <Link to={href} className={`flex flex-col group items-start gap-4 max-h-60 border border-stokia-neutral-200 rounded-xl overflow-hidden transition-all cursor-pointer bg-stokia-neutral-50 shadow-lg p-6 hover:bg-stokia-neutral-300/20 ${className}`}>
      <span className="p-3 bg-stokia-primary-200 rounded-xl group-hover:transform group-hover:shadow-md transition-shadow">
        <Icon className="text-stokia-primary-600" size={22} />
      </span>
      <div className="flex flex-col w-full gap-3 bottom-0">
        <p className="font-medium text-stokia-neutral-900 text-xl">{title}</p>
        <p className="text-stokia-neutral-950 text-base">{description ?? "Ver más"}</p>
      </div>
    </Link>
  );
}

export default LinkCard;
