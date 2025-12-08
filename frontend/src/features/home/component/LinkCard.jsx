import { Link } from "react-router";

function LinkCard({ image, title, icon: Icon, className, href }) {
  return (
    <Link to={href} className={`group relative max-h-[240px] border border-stokia-neutral-200 rounded-xl overflow-hidden transition-all cursor-pointer bg-stokia-neutral-50 ${className}`}>
      <img
        src={image}
        alt="Dashboard Link Card"
        className="w-full h-full object-cover transition-transform duration-300 group-hover:scale-105"
      />
      <div className="flex items-center w-full gap-3 p-5 bg-stokia-neutral-50 absolute bottom-0">
        <Icon className="text-stokia-neutral-700" size={20} />
        <p className="font-medium text-stokia-neutral-900 text-lg">{title}</p>
      </div>
    </Link>
  );
}

export default LinkCard;
