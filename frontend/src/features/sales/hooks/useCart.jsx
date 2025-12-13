import { useState, useMemo } from "react";

export const useCart = () => {
  const [items, setItems] = useState([]);
  const [discount, setDiscount] = useState(null);

  const addItem = (productToAdd) => {
    const quantityToAdd = Number(productToAdd.quantity) || 1;
    setItems((prevItems) => {
      const existingItemIndex = prevItems.findIndex(
        (item) => item.id === productToAdd.id
      );
      if (existingItemIndex > -1) {
        return prevItems.map((item, index) =>
          index === existingItemIndex
            ? { ...item, quantity: Number(item.quantity) + quantityToAdd }
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

  const updateItemQuantity = (productId, newQuantity) => {
    if (newQuantity <= 0) {
      removeItem(productId);
      return;
    }
    setItems((prevItems) =>
      prevItems.map((item) =>
        item.id === productId ? { ...item, quantity: newQuantity } : item
      )
    );
  };
  const clearCart = () => {
    setItems([]);
    setDiscount(null);
  };
  const applyDiscount = (discountData) => {
    setDiscount(discountData);
  };
  const removeDiscount = () => {
    setDiscount(null);
  };


  const subtotal = useMemo(() => {
    return items.reduce((acc, item) => acc + item.price * item.quantity, 0);
  }, [items]);

  const discountAmount = useMemo(() => {
    if (!discount || subtotal === 0) return 0;

    let amount = 0;
    if (discount.type === "percentage") {
      amount = (subtotal * discount.value) / 100;
    } else if (discount.type === "fixed") {
      amount = discount.value;
    }

    return Math.min(amount, subtotal);
  }, [discount, subtotal]);

  const total = useMemo(() => {
    return subtotal - discountAmount;
  }, [subtotal, discountAmount]);

  const loadFromOrder = (order) => {
    setItems(order.items || []);

    if (order.discountAmount && order.discountAmount > 0) {
      setDiscount({
        type: "AMOUNT",
        value: order.discountAmount
      });
    } else {
      setDiscount(null);
    }
  };



  return {
    items,
    discount,
    addItem,
    removeItem,
    decrementItemQuantity,
    updateItemQuantity,
    clearCart,
    applyDiscount,
    removeDiscount,
    loadFromOrder,
    subtotal,
    discountAmount,
    total,
  };
};
