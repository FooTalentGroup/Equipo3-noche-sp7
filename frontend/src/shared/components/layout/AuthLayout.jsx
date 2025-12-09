import logoImg from "@/assets/logo.png";
import Stockia from "@/assets/stockia2.svg";

export function AuthLayout({ children }) {
  return (
    <div className="min-h-screen w-full grid grid-cols-1 md:grid-cols-2">
       <div className="hidden md:block w-full h-full relative"
         style={{ 
                  backgroundImage: `url(${logoImg})`,
                  backgroundSize: "cover",
                  backgroundPosition: "center",
                }}
        >
        <img
          src={Stockia}
          alt="Logo Stockia"
          className="absolute z-10"
          style={{
            width: "204.5px",
            height: "44px",
            bottom: "20px",
            left: "5%",
            opacity: 1,
          }}
        />
      </div>

      <div className="flex flex-col items-center justify-center min-h-screen p-4">
        {children}
      </div>
    </div>
  );
}