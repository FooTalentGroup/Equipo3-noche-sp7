import { describe, test, expect, vi, beforeEach } from 'vitest'
import { render, screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { ProductCard } from './ProductCard.jsx'

describe('ProductCard', () => {
  const mockOnEdit = vi.fn()
  const mockOnDelete = vi.fn()

  const defaultProps = {
    id: 1,
    name: 'Hamburguesa Clásica',
    price: 12.99,
    description: 'Deliciosa hamburguesa con carne, lechuga y tomate',
    category: 'Platos Fuertes',
    image: 'https://example.com/burger.jpg',
    onEdit: mockOnEdit,
    onDelete: mockOnDelete,
  }

  beforeEach(() => {
    vi.clearAllMocks()
  })

  test('debe renderizar todos los datos del producto', () => {
    render(<ProductCard {...defaultProps} />)

    expect(screen.getByText('Hamburguesa Clásica')).toBeInTheDocument()
    expect(screen.getByText('$12.99')).toBeInTheDocument()
    expect(screen.getByText(/deliciosa hamburguesa/i)).toBeInTheDocument()
    expect(screen.getByText('Platos Fuertes')).toBeInTheDocument()
    expect(screen.getByAltText('Hamburguesa Clásica')).toBeInTheDocument()
  })

  test('debe renderizar sin imagen cuando image es null', () => {
    render(<ProductCard {...defaultProps} image={null} />)

    expect(screen.queryByAltText('Hamburguesa Clásica')).not.toBeInTheDocument()
    expect(screen.getByText('Hamburguesa Clásica')).toBeInTheDocument()
  })

  test('debe renderizar sin imagen cuando image es undefined', () => {
    render(<ProductCard {...defaultProps} image={undefined} />)

    expect(screen.queryByAltText('Hamburguesa Clásica')).not.toBeInTheDocument()
    expect(screen.getByText('Hamburguesa Clásica')).toBeInTheDocument()
  })

  test('debe renderizar sin categoría cuando category es null', () => {
    render(<ProductCard {...defaultProps} category={null} />)

    expect(screen.queryByText('Platos Fuertes')).not.toBeInTheDocument()
    expect(screen.getByText('Hamburguesa Clásica')).toBeInTheDocument()
  })

  test('debe llamar a onEdit con datos correctos al hacer click en botón editar', async () => {
    const user = userEvent.setup()
    render(<ProductCard {...defaultProps} />)

    const editButton = screen.getByLabelText('Editar producto')
    await user.click(editButton)

    expect(mockOnEdit).toHaveBeenCalledWith({
      id: 1,
      name: 'Hamburguesa Clásica',
      price: 12.99,
      description: 'Deliciosa hamburguesa con carne, lechuga y tomate',
      category: 'Platos Fuertes',
      image: 'https://example.com/burger.jpg',
    })
  })

  test('debe llamar a onDelete con id y name al hacer click en botón eliminar', async () => {
    const user = userEvent.setup()
    render(<ProductCard {...defaultProps} />)

    const deleteButton = screen.getByLabelText('Eliminar producto')
    await user.click(deleteButton)

    expect(mockOnDelete).toHaveBeenCalledWith(1, 'Hamburguesa Clásica')
  })

  test('debe funcionar correctamente cuando onEdit no está definido', async () => {
    const user = userEvent.setup()
    render(<ProductCard {...defaultProps} onEdit={undefined} />)

    const editButton = screen.getByLabelText('Editar producto')
    await user.click(editButton)

    expect(mockOnEdit).not.toHaveBeenCalled()
  })

  test('debe funcionar correctamente cuando onDelete no está definido', async () => {
    const user = userEvent.setup()
    render(<ProductCard {...defaultProps} onDelete={undefined} />)

    const deleteButton = screen.getByLabelText('Eliminar producto')
    await user.click(deleteButton)

    expect(mockOnDelete).not.toHaveBeenCalled()
  })

  test('debe renderizar precio con formato correcto', () => {
    render(<ProductCard {...defaultProps} price={9.99} />)
    expect(screen.getByText('$9.99')).toBeInTheDocument()
  })

  test('debe renderizar precio con decimales largos', () => {
    render(<ProductCard {...defaultProps} price={12.999} />)
    expect(screen.getByText('$12.999')).toBeInTheDocument()
  })

  test('debe renderizar descripción larga correctamente', () => {
    const longDescription = 'Esta es una descripción muy larga que debería ser renderizada correctamente en el componente ProductCard sin causar problemas de layout o visualización'
    render(<ProductCard {...defaultProps} description={longDescription} />)

    expect(screen.getByText(longDescription)).toBeInTheDocument()
  })
})

