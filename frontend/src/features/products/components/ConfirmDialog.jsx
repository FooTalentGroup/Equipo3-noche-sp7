import React from "react";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from "@/shared/components/ui/alert-dialog";
import { buttonVariants } from "@/shared/components/ui/button";
import { cn } from "@/lib/utils";
import { Trash2 } from "lucide-react";

export const ConfirmDialog = ({
  isOpen = false,
  handleOpenChange = () => {},
  cancelTitle = "Cancelar",
  acceptTitle = "Sí, confirmar",
  dialogTitle = "¿Desea confirmar los cambios?",
  dialogDescription = "La información del producto se actualizará en el inventario",
  onAccept = () => {},
  onCancel = () => {},
  variant = "stokia",
  icon: Icon,
  showIcon = variant === "destructive",
}) => {
  const IconComponent = Icon || (showIcon ? Trash2 : null);

  return (
    <AlertDialog open={isOpen} onOpenChange={handleOpenChange}>
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle>{dialogTitle}</AlertDialogTitle>
          <AlertDialogDescription>{dialogDescription}</AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter className="justify-start! gap-6">
          <AlertDialogCancel onClick={onCancel}>
            {cancelTitle}
          </AlertDialogCancel>
          <AlertDialogAction
            onClick={onAccept}
            className={cn(buttonVariants({ variant }))}
          >
            {IconComponent && <IconComponent className="h-4 w-4" />}
            {acceptTitle}
          </AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
};