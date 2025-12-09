import { useState, useMemo } from "react";

export const useCart = () => {
  const [items, setItems] = useState([]);

  const addItem = (productToAdd) => {
    const quantityToAdd = productToAdd.quantity || 1;

    setItems((prevItems) => {
      const existingItemIndex = prevItems.findIndex(
        (item) => item.id === productToAdd.id
      );

      if (existingItemIndex > -1) {
        return prevItems.map((item, index) =>
          index === existingItemIndex
            ? { ...item, quantity: item.quantity + quantityToAdd }
            : item
        );
      } else {
        return [
          ...prevItems,
          {
            ...productToAdd,
            quantity: quantityToAdd,
          },
        ];
      }
    });
  };

  const removeItem = (productId) => {
    setItems((prevItems) => prevItems.filter((item) => item.id !== productId));
  };

  const decrementItemQuantity = (productId) => {
    setItems((prevItems) => {
      return prevItems
        .map((item) => {
          if (item.id === productId) {
            if (item.quantity > 1) {
              return { ...item, quantity: item.quantity - 1 };
            } else {
              return null;
            }
          }
          return item;
        })
        .filter((item) => item !== null);
    });
  };

  const subtotal = useMemo(() => {
    return items.reduce((acc, item) => acc + item.price * item.quantity, 0);
  }, [items]);

  const total = subtotal;

  return {
    items,
    addItem,
    removeItem,
    decrementItemQuantity,
    subtotal,
    total,
  };
};
