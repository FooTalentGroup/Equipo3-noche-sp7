import * as React from 'react';
import { ChevronLeft, ChevronRight } from 'lucide-react';
import { Button } from './button.jsx';
import { cn } from '@/lib/utils';

export function Pagination({
  currentPage,
  totalPages,
  onPageChange,
  className,
  showInfo = true,
  totalElements,
  pageSize,
}) {
  const handlePrevious = () => {
    if (currentPage > 0) {
      onPageChange(currentPage - 1);
    }
  };

  const handleNext = () => {
    if (currentPage < totalPages - 1) {
      onPageChange(currentPage + 1);
    }
  };

  const handlePageClick = (page) => {
    if (page >= 0 && page < totalPages) {
      onPageChange(page);
    }
  };

  const getPageNumbers = () => {
    const pages = [];
    const maxVisible = 5;

    if (totalPages <= maxVisible) {
      for (let i = 0; i < totalPages; i++) {
        pages.push(i);
      }
    } else {
      if (currentPage < 3) {
        for (let i = 0; i < 4; i++) {
          pages.push(i);
        }
        pages.push('ellipsis');
        pages.push(totalPages - 1);
      } else if (currentPage > totalPages - 4) {
        pages.push(0);
        pages.push('ellipsis');
        for (let i = totalPages - 4; i < totalPages; i++) {
          pages.push(i);
        }
      } else {
        pages.push(0);
        pages.push('ellipsis');
        for (let i = currentPage - 1; i <= currentPage + 1; i++) {
          pages.push(i);
        }
        pages.push('ellipsis');
        pages.push(totalPages - 1);
      }
    }

    return pages;
  };

  if (totalPages <= 1) {
    return null;
  }

  const pageNumbers = getPageNumbers();
  const startItem = currentPage * pageSize + 1;
  const endItem = Math.min((currentPage + 1) * pageSize, totalElements);

  return (
    <div className={cn('flex flex-col gap-4', className)}>
      {showInfo && totalElements > 0 && (
        <div className="text-sm text-stokia-neutral-400 text-center">
          Mostrando {startItem}-{endItem} de {totalElements} resultados
        </div>
      )}
      <div className="flex items-center justify-center gap-2">
        <Button
          variant="outline"
          size="sm"
          onClick={handlePrevious}
          disabled={currentPage === 0}
          aria-label="Página anterior"

        >
          <ChevronLeft className="h-4 w-4" />
          <span className="sr-only md:not-sr-only">Anterior</span>
        </Button>

        <div className="flex items-center gap-1">
          {pageNumbers.map((page, index) => {
            if (page === 'ellipsis') {
              return (
                <span
                  key={`ellipsis-${index}`}
                  className="px-2 text-stokia-neutral-400"
                >
                  ...
                </span>
              );
            }

            const isCurrentPage = page === currentPage;

            return (
              <Button
                key={page}
                variant={isCurrentPage ? 'stokia' : 'outline'}
                size="sm"
                onClick={() => handlePageClick(page)}
                aria-label={`Ir a página ${page + 1}`}
                aria-current={isCurrentPage ? 'page' : undefined}
              >
                {page + 1}
              </Button>
            );
          })}
        </div>

        <Button
          variant="outline"
          size="sm"
          onClick={handleNext}
          disabled={currentPage >= totalPages - 1}
          aria-label="Página siguiente"
        >
          <span className="sr-only md:not-sr-only">Siguiente</span>
          <ChevronRight className="h-4 w-4" />
        </Button>
      </div>
    </div>
  );
}

