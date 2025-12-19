
export function InfoBanner() {
  return (
    <div
      className="relative flex flex-col text-center px-7 py-11 shadow-sm rounded-xl text-stokia-neutral-50 bg-cover bg-center overflow-hidden"
      style={{
        backgroundImage: "url('/home_banner.png')",
      }}
    >
      <div id="overlay" className="absolute inset-0 bg-stokia-primary-950 opacity-75" />
      <div className="relative z-10">
        <h2 className="text-3xl font-semibold">
          Próximamente: IA Predictiva
        </h2>
        <p>
          Prepárate para desbloquear una nueva y potente funcionalidad que llegará muy pronto a nuestra plataforma. ¡Mantente atento!
        </p>
      </div>
    </div>
  );
}

