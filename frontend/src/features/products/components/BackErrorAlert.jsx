import { Alert, AlertTitle } from "@/shared/components/ui/alert";
import { AlertCircleIcon } from "lucide-react";

export function BackErrorAlert({
  variant = "destructive",
  alertTitle = "Error inesperado."
}) {
  return (
    <Alert variant={variant}>
      <AlertCircleIcon />
      <AlertTitle>{alertTitle}</AlertTitle>
    </Alert>
  );
}
