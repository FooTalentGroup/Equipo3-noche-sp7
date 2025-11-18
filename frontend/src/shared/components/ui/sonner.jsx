import { Toaster as Sonner } from 'sonner';

const Toaster = ({ ...props }) => {
  return (
    <>
      <style>{`
        [data-sonner-toast][data-type="success"] [data-icon] {
          color: rgb(234 88 12) !important;
        }
        [data-sonner-toast][data-type="loading"] [data-icon] {
          color: rgb(234 88 12) !important;
        }
        [data-sonner-toast][data-type="success"] svg {
          color: rgb(234 88 12) !important;
        }
        [data-sonner-toast][data-type="loading"] svg {
          color: rgb(234 88 12) !important;
        }
      `}</style>
      <Sonner
        theme='light'
        className='toaster group'
        toastOptions={{
          classNames: {
            toast:
              'group toast group-[.toaster]:bg-white group-[.toaster]:text-slate-950 group-[.toaster]:border-slate-200 group-[.toaster]:shadow-lg',
            success:
              'group-[.toaster]:border-l-4 group-[.toaster]:border-l-orange-600 group-[.toaster]:bg-white',
            error:
              'group-[.toaster]:border-l-4 group-[.toaster]:border-l-red-600 group-[.toaster]:bg-white',
            info: 'group-[.toaster]:border-l-4 group-[.toaster]:border-l-blue-600 group-[.toaster]:bg-white',
            warning:
              'group-[.toaster]:border-l-4 group-[.toaster]:border-l-yellow-600 group-[.toaster]:bg-white',
            loading:
              'group-[.toaster]:border-l-4 group-[.toaster]:border-l-orange-600 group-[.toaster]:bg-white',
            description: 'group-[.toast]:text-slate-600',
            actionButton:
              'group-[.toast]:bg-orange-600 group-[.toast]:text-white group-[.toast]:hover:bg-orange-700',
            cancelButton:
              'group-[.toast]:bg-slate-100 group-[.toast]:text-slate-700 group-[.toast]:hover:bg-slate-200'
          }
        }}
        {...props}
      />
    </>
  );
};

export { Toaster };
