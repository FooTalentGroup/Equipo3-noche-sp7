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
  handleOpenChange,
  onClose,
  cancelTitle = "Cancelar",
  acceptTitle = "Sí, confirmar",
  dialogTitle,
  title,
  dialogDescription,
  description,
  onAccept,
  onConfirm,
  onCancel = () => { },
  variant = "stokia",
  icon: Icon,
  showIcon = variant === "destructive",
}) => {
  const IconComponent = Icon || (showIcon ? Trash2 : null);
  const effectiveTitle =
    title ?? dialogTitle ?? "¿Desea confirmar los cambios?";
  const effectiveDescription =
    description ?? dialogDescription ??
    "La información del producto se actualizará en el inventario";
  const openChangeHandler =
    typeof handleOpenChange === "function"
      ? handleOpenChange
      : typeof onClose === "function"
        ? (open) => onClose(open)
        : () => { };

  const acceptHandler = () => {
    const cb = onConfirm || onAccept || (() => { });
    cb();
    try {
      openChangeHandler(false);
    } catch (e) { }
  };

  const cancelHandler = () => {
    try {
      onCancel();
    } catch (e) { }
    try {
      openChangeHandler(false);
    } catch (e) { }
  };

  return (
    <AlertDialog open={isOpen} onOpenChange={openChangeHandler}>
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle>{effectiveTitle}</AlertDialogTitle>
          <AlertDialogDescription>
            {effectiveDescription}
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter className="gap-6">
          <AlertDialogCancel onClick={onCancel}>
            {cancelTitle}
          </AlertDialogCancel>
          <AlertDialogAction
            onClick={acceptHandler}
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