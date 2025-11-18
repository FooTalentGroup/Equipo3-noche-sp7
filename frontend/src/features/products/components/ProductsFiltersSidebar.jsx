import { X, ChevronLeft, ChevronRight } from 'lucide-react';
import { Button } from '@/shared/components/ui/button';
import { SearchBar } from './filters/SearchBar';
import { SortSelector } from './filters/SortSelector';
import { CategoryFilter } from './filters/CategoryFilter';
import {
  Sheet,
  SheetContent,
  SheetHeader,
  SheetTitle
} from '@/shared/components/ui/sheet';

export function ProductsFiltersSidebar({
  isOpen,
  onClose,
  searchQuery,
  onSearchChange,
  sort,
  onSortChange,
  selectedCategories, // eslint-disable-line no-unused-vars
  onCategoriesChange, // eslint-disable-line no-unused-vars
  isCollapsed,
  onToggleCollapse
}) {
  const sidebarContent = (
    <div className='p-6 space-y-6'>
      <div>
        <h3 className='text-lg font-semibold mb-4'>Buscar</h3>
        <SearchBar value={searchQuery} onChange={onSearchChange} />
      </div>

      <SortSelector value={sort} onChange={onSortChange} />

      {/* Filtro de categorías temporalmente oculto - Backend no implementado aún */}
      {/* <CategoryFilter
        selectedCategories={selectedCategories}
        onChange={onCategoriesChange}
      /> */}
    </div>
  );

  return (
    <>
      <Sheet open={isOpen} onOpenChange={onClose}>
        <SheetContent
          side='left'
          className='w-80 p-0 lg:hidden shadow-[2px_0_12px_rgba(234,88,12,0.15)]'
        >
          <SheetHeader className='p-4 border-b'>
            <SheetTitle>Filtros</SheetTitle>
          </SheetHeader>
          {sidebarContent}
          <div className='pt-4 border-t px-6 pb-6'>
            <Button onClick={onClose} className='w-full' variant='outline'>
              Aplicar Filtros
            </Button>
          </div>
        </SheetContent>
      </Sheet>

      <aside
        className={`hidden lg:block bg-white border-r transition-all duration-300 ${
          isCollapsed
            ? 'w-12 shadow-sm'
            : 'w-80 shadow-[2px_0_12px_rgba(234,88,12,0.15)]'
        }`}
      >
        <div className='sticky top-[73px] h-[calc(100vh-73px)] flex flex-col'>
          <div
            className={`border-b ${
              isCollapsed
                ? 'flex items-center justify-center p-2'
                : 'flex items-center justify-between p-4'
            }`}
          >
            {!isCollapsed && <h2 className='text-lg font-semibold'>Filtros</h2>}
            <Button
              variant='ghost'
              size='icon'
              onClick={onToggleCollapse}
              className={isCollapsed ? '' : 'ml-auto'}
              aria-label={isCollapsed ? 'Expandir filtros' : 'Colapsar filtros'}
            >
              {isCollapsed ? (
                <ChevronRight className='h-4 w-4' />
              ) : (
                <ChevronLeft className='h-4 w-4' />
              )}
            </Button>
          </div>

          {!isCollapsed && (
            <div className='flex-1 overflow-y-auto'>{sidebarContent}</div>
          )}
        </div>
      </aside>
    </>
  );
}
