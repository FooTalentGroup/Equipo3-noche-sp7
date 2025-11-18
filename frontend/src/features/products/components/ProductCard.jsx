import { Pencil, Trash2 } from 'lucide-react';
import { Button } from '@/shared/components/ui/button';

export const ProductCard = ({
  id,
  name,
  price,
  description,
  category,
  image,
  onEdit,
  onDelete
}) => {
  return (
    <div className='bg-white border rounded-2xl shadow-md p-4 hover:shadow-lg transition relative flex flex-col h-full'>
      <div className='absolute top-4 right-4 flex gap-2 z-10'>
        <Button
          variant='ghost'
          size='icon'
          className='h-8 w-8'
          onClick={() =>
            onEdit?.({ id, name, price, description, category, image })
          }
          aria-label='Editar producto'
        >
          <Pencil className='h-4 w-4' />
        </Button>
        <Button
          variant='ghost'
          size='icon'
          className='h-8 w-8 text-red-600 hover:text-red-700 hover:bg-red-50'
          onClick={() => onDelete?.(id, name)}
          aria-label='Eliminar producto'
        >
          <Trash2 className='h-4 w-4' />
        </Button>
      </div>

      {image && (
        <img
          src={image}
          alt={name}
          className='h-40 mx-auto object-contain mb-4'
        />
      )}
      <div className='mb-2'>
        {category && (
          <span className='text-xs text-gray-500 bg-gray-100 px-2 py-1 rounded'>
            {category}
          </span>
        )}
      </div>
      <h2 className='font-semibold text-lg mb-2 line-clamp-2 pr-16'>{name}</h2>
      <div className='h-16 overflow-y-auto mb-2'>
        <p className='text-gray-600 text-sm'>{description}</p>
      </div>
      <p className='font-bold text-blue-600 mt-auto'>${price}</p>
    </div>
  );
};
