import React, { useState, useEffect } from "react";
import { Button } from "@/shared/components/ui/button";
import { Textarea } from "@headlessui/react";

export const OrderNoteModal = ({ isOpen, onClose, onSave, initialNote = "" }) => {
  const [note, setNote] = useState(initialNote);

  useEffect(() => {
    setNote(initialNote);
  }, [initialNote]);

  if (!isOpen) return null;

  const handleSave = () => {
    onSave(note);
    onClose();
  };

  const handleCancel = () => {
    setNote(initialNote);
    onClose();
  };

  return (
    <div className="fixed inset-0 bg-black/20 bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-xl p-6 w-full max-w-[546px]">
        <h2 className="text-xl font-semibold text-foreground mb-4">
          Agregar nota
        </h2>

        <Textarea
          value={note}
          onChange={(e) => setNote(e.target.value)}
          placeholder="Escribir nota"
          className="w-full h-32 px-3 py-2 border border-border rounded-lg"
        />

        <div className="flex justify-end space-x-3 mt-6">
          <Button
            onClick={handleCancel}
            className="px-6 py-2 text-foreground bg-stokia-neutral-50 hover:bg-tokia-neutral/90 rounded-lg"
          >
            Cancelar
          </Button>
          <Button
            onClick={handleSave}
            className="px-6 py-2 text-white bg-btn-primary hover:bg-btn-primary/90 rounded-lg"
          >
            Guardar nota
          </Button>
        </div>
      </div>
    </div>
  );
};