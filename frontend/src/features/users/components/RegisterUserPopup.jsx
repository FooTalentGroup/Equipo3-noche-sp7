import React, { useEffect, useState } from 'react';
import { Button } from '@/shared/components/ui/button.jsx';
import { Input } from '@/shared/components/ui/input.jsx';
import { Select } from '@/shared/components/ui/select.jsx';
import { SuccessModal } from '@/shared/components/ui/SuccessModal.jsx';
import { LoadingModal } from '@/shared/components/ui/LoadingModal';

const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

export default function RegisterUserPopup({ open, onClose, onSave, initialData = null }) {
  const isEditMode = !!initialData?.id;
  const [form, setForm] = useState({ nombre: '', email: '', password: '', repeatPassword: '', role: '', accountStatus: 'ACTIVE' });
  const [errors, setErrors] = useState({});
  const [showSuccess, setShowSuccess] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const [changePassword, setChangePassword] = useState(false);

  useEffect(() => {
    if (open) {
      if (initialData) {
        setForm({
          nombre: initialData.nombre || '',
          email: initialData.email || '',
          password: '',
          repeatPassword: '',
          role: initialData.role || '',
          accountStatus: initialData.accountStatus || 'ACTIVE'
        });
        setChangePassword(false);
      } else {
        setForm({ nombre: '', email: '', password: '', repeatPassword: '', role: '', accountStatus: 'ACTIVE' });
      }
      setErrors({});
      setShowSuccess(false);
    }
  }, [initialData, open]);

  if (!open) return null;

  function validate() {
    const e = {};
    // When creating, enforce required fields.
    if (!isEditMode) {
      if (!form.nombre || form.nombre.trim().length < 3) {
        e.nombre = 'El nombre debe tener al menos 3 caracteres';
      }
      if (!form.email || !emailRegex.test(form.email)) {
        e.email = 'Correo electrónico inválido';
      }
      if (!form.role) {
        e.role = 'Selecciona un rol';
      }
      if (!form.accountStatus) {
        e.accountStatus = 'Selecciona un estado de cuenta';
      }
    } else {
      // Edit mode: fields are optional. Validate only if user provided values.
      if (form.nombre && form.nombre.trim().length < 3) {
        e.nombre = 'El nombre debe tener al menos 3 caracteres';
      }
      if (form.email && !emailRegex.test(form.email)) {
        e.email = 'Correo electrónico inválido';
      }
      // role and accountStatus are optional in edit mode; no required validation.
    }

    // Password rules: required only when creating; when editing validate only if changePassword is true.
    if (!isEditMode || changePassword) {
      if (!form.password || form.password.length < 8) {
        e.password = 'La contraseña debe tener al menos 8 caracteres';
      }
      if (form.password !== form.repeatPassword) {
        e.repeatPassword = 'Las contraseñas no coinciden';
      }
    }
    setErrors(e);
    return Object.keys(e).length === 0;
  }

  async function submit(e) {
    e?.preventDefault();
    if (!validate()) return;
    setIsSubmitting(true);
    try {
      // Build payload: include password only if provided; include other fields as present
      const payload = { id: initialData?.id };
      if (form.nombre) payload.nombre = form.nombre.trim();
      if (form.email) payload.email = form.email.trim();
      if (form.role) payload.role = form.role;
      if (form.accountStatus) payload.accountStatus = form.accountStatus;
      // include password only if creating or user opted to change it
      if ((!isEditMode && form.password) || (isEditMode && changePassword && form.password)) payload.password = form.password;

      await onSave?.(payload);

      setIsSubmitting(false);

      if (!isEditMode) {
        setShowSuccess(true);
      } else {
        handleClose();
      }
    } catch (error) {
      console.error('Error saving user:', error);
      setIsSubmitting(false);
      setErrors(prev => ({ ...prev, email: error.response?.data?.message || 'Error al guardar usuario' }));
    }
  }

  const handleClose = () => {
    setForm({ nombre: '', email: '', password: '', repeatPassword: '', role: '', accountStatus: 'ACTIVE' });
    setErrors({});
    setShowSuccess(false);
    setIsSubmitting(false);
    setChangePassword(false);
    onClose?.();
  };

  const handleRegisterAnother = () => {
    setShowSuccess(false);
    setForm({ nombre: '', email: '', password: '', repeatPassword: '', role: '', accountStatus: 'ACTIVE' });
    setErrors({});
  };

  return (
    <div className="fixed inset-0 z-1000 flex items-center justify-center">
      <div className="absolute inset-0 bg-black/40" onClick={!isSubmitting && !showSuccess ? handleClose : undefined} />
      <div className="relative w-[760px] max-w-full bg-[#F4F5F7] rounded-2xl shadow-[-3px_2px_4px_0px_rgba(0,0,0,0.25)] p-8 z-10">
        {!isSubmitting && !showSuccess && (
          <form onSubmit={submit} className="flex flex-col">
            <div className="mb-4">
              <h3 className="text-3xl font-semibold text-[#202326]">{isEditMode ? 'Editar usuario' : 'Registrar nuevo usuario'}</h3>
              <p className="text-xs text-[#627078] mt-1">{isEditMode ? 'Actualiza la información del usuario' : 'Completa este formulario para registrar un nuevo usuario'}</p>
            </div>

            <div className="border-t border-Stockia-Neutral-200---Pressed---Stroke mb-6" />

            <div className="space-y-4">
              <div>
                <label className="text-[1rem] font-medium text-[#202326] flex items-center gap-1">Nombre completo <span className="text-[#C93939]">*</span></label>
                  <Input className="mt-2 bg-white" value={form.nombre} onChange={(e) => setForm(f => ({ ...f, nombre: e.target.value }))} />
                {errors.nombre && <p className="text-xs text-Stockia-Red-Error-600---Activo mt-1">{errors.nombre}</p>}
              </div>

              <div>
                <label className="text-[1rem] font-medium text-[#202326] flex items-center gap-1">Correo electrónico <span className="text-[#C93939]">*</span></label>
                <Input type="email" className="mt-2 bg-white" value={form.email} onChange={(e) => setForm(f => ({ ...f, email: e.target.value }))} />
                {errors.email && <p className="text-xs text-Stockia-Red-Error-600---Activo mt-1">{errors.email}</p>}
              </div>

              {/* Password fields: only show when creating OR when user opts to change password in edit mode */}
              {(!isEditMode || changePassword) && (
                <div>
                  <label className="text-[1rem] font-medium text-[#202326] flex items-center gap-1">Contraseña <span className="text-[#C93939]">*</span></label>
                  <div className="relative mt-2">
                    <Input type={showPassword ? 'text' : 'password'} className="bg-white pr-10" value={form.password} onChange={(e) => setForm(f => ({ ...f, password: e.target.value }))} />
                    <button type="button" onClick={() => setShowPassword(s => !s)} className="absolute right-2 top-1/2 -translate-y-1/2 p-1">
                      {showPassword ? (
                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 20 20" fill="none"><path d="M10 4.58398C11.5678 4.58399 13.1004 5.04984 14.4033 5.92188C15.6247 6.73941 16.5934 7.87935 17.2021 9.21289L17.3184 9.48242L17.3281 9.50684C17.4461 9.82484 17.4462 10.1752 17.3281 10.4932C17.325 10.5015 17.3217 10.5104 17.3184 10.5186C16.7206 11.9677 15.7061 13.2072 14.4033 14.0791C13.1004 14.9511 11.5678 15.416 10 15.416C8.43224 15.416 6.89958 14.9511 5.59668 14.0791C4.29393 13.2072 3.27944 11.9677 2.68164 10.5186C2.67827 10.5104 2.67496 10.5015 2.67188 10.4932C2.55383 10.1752 2.55389 9.82484 2.67188 9.50684C2.67491 9.49869 2.67832 9.49046 2.68164 9.48242C3.27936 8.03311 4.29387 6.79391 5.59668 5.92188C6.89962 5.04984 8.43217 4.58399 10 4.58398ZM10 6.08398C8.72923 6.08399 7.48673 6.46116 6.43066 7.16797C5.39102 7.86387 4.57805 8.84864 4.09082 10C4.57802 11.1514 5.39102 12.1361 6.43066 12.832C7.4867 13.5388 8.72928 13.916 10 13.916C11.2707 13.916 12.5133 13.5388 13.5693 12.832C14.6089 12.1362 15.421 11.1513 15.9082 10C15.421 8.8488 14.6089 7.8638 13.5693 7.16797C12.5133 6.46116 11.2708 6.08399 10 6.08398ZM10 7.25C11.5188 7.25 12.75 8.48122 12.75 10C12.75 11.5188 11.5188 12.75 10 12.75C8.48122 12.75 7.25 11.5188 7.25 10C7.25 8.48122 8.48122 7.25 10 7.25ZM10 8.75C9.30964 8.75 8.75 9.30964 8.75 10C8.75 10.6904 9.30964 11.25 10 11.25C10.6904 11.25 11.25 10.6904 11.25 10C11.25 9.30964 10.6904 8.75 10 8.75ZM2.00977 2.00977H2V2H2.00977V2.00977Z" fill="#737373"/></svg>
                      ) : (
                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 20 20" fill="none"><path d="M15.9619 7.07617C16.1039 6.68731 16.5349 6.48721 16.9238 6.62891C17.3125 6.77102 17.5127 7.20102 17.3711 7.58984C16.9294 8.80019 16.1977 9.87801 15.2471 10.7334L15.9072 11.5176C16.1736 11.8344 16.1331 12.3076 15.8164 12.5742C15.4996 12.8407 15.0265 12.7999 14.7598 12.4834L14.0381 11.627C13.5221 11.9367 12.9743 12.185 12.4053 12.3682L12.7324 13.8369C12.8223 14.2413 12.5665 14.6416 12.1621 14.7314C11.7579 14.821 11.3574 14.5663 11.2676 14.1621L10.9395 12.6895C10.6289 12.7269 10.3151 12.7471 10 12.7471C9.68445 12.7471 9.37055 12.727 9.05957 12.6895L8.73242 14.1621C8.64259 14.5665 8.24127 14.8213 7.83691 14.7314C7.43289 14.6414 7.17779 14.2411 7.26758 13.8369L7.59375 12.3682C7.02482 12.1848 6.47686 11.9367 5.96094 11.627L5.24023 12.4834C4.97353 12.7999 4.50036 12.8405 4.18359 12.5742C3.86694 12.3074 3.82712 11.8344 4.09375 11.5176L4.75293 10.7334C3.80243 9.87807 3.07053 8.80007 2.62891 7.58984C2.48727 7.20092 2.68734 6.77091 3.07617 6.62891C3.46513 6.48702 3.89498 6.68734 4.03711 7.07617C4.48322 8.29862 5.29514 9.35452 6.36133 10.1006C7.42773 10.8466 8.69855 11.247 10 11.2471C11.3014 11.247 12.5713 10.8466 13.6377 10.1006C14.7041 9.35452 15.5157 8.29879 15.9619 7.07617ZM2.00977 2.00977H2V2H2.00977V2.00977Z" fill="#737373"/></svg>
                      )}
                    </button>
                  </div>
                  <div className="mt-3">
                    <label className="text-[1rem] font-medium text-[#202326] flex items-center gap-1">Repetir contraseña <span className="text-[#C93939]">*</span></label>
                    <div className="relative mt-2">
                      <Input type={showPassword ? 'text' : 'password'} className="bg-white pr-10" value={form.repeatPassword} onChange={(e) => setForm(f => ({ ...f, repeatPassword: e.target.value }))} />
                      <button type="button" onClick={() => setShowPassword(s => !s)} className="absolute right-2 top-1/2 -translate-y-1/2 p-1">
                        {showPassword ? (
                           <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 20 20" fill="none"><path d="M10 4.58398C11.5678 4.58399 13.1004 5.04984 14.4033 5.92188C15.6247 6.73941 16.5934 7.87935 17.2021 9.21289L17.3184 9.48242L17.3281 9.50684C17.4461 9.82484 17.4462 10.1752 17.3281 10.4932C17.325 10.5015 17.3217 10.5104 17.3184 10.5186C16.7206 11.9677 15.7061 13.2072 14.4033 14.0791C13.1004 14.9511 11.5678 15.416 10 15.416C8.43224 15.416 6.89958 14.9511 5.59668 14.0791C4.29393 13.2072 3.27944 11.9677 2.68164 10.5186C2.67827 10.5104 2.67496 10.5015 2.67188 10.4932C2.55383 10.1752 2.55389 9.82484 2.67188 9.50684C2.67491 9.49869 2.67832 9.49046 2.68164 9.48242C3.27936 8.03311 4.29387 6.79391 5.59668 5.92188C6.89962 5.04984 8.43217 4.58399 10 4.58398ZM10 6.08398C8.72923 6.08399 7.48673 6.46116 6.43066 7.16797C5.39102 7.86387 4.57805 8.84864 4.09082 10C4.57802 11.1514 5.39102 12.1361 6.43066 12.832C7.4867 13.5388 8.72928 13.916 10 13.916C11.2707 13.916 12.5133 13.5388 13.5693 12.832C14.6089 12.1362 15.421 11.1513 15.9082 10C15.421 8.8488 14.6089 7.8638 13.5693 7.16797C12.5133 6.46116 11.2708 6.08399 10 6.08398ZM10 7.25C11.5188 7.25 12.75 8.48122 12.75 10C12.75 11.5188 11.5188 12.75 10 12.75C8.48122 12.75 7.25 11.5188 7.25 10C7.25 8.48122 8.48122 7.25 10 7.25ZM10 8.75C9.30964 8.75 8.75 9.30964 8.75 10C8.75 10.6904 9.30964 11.25 10 11.25C10.6904 11.25 11.25 10.6904 11.25 10C11.25 9.30964 10.6904 8.75 10 8.75ZM2.00977 2.00977H2V2H2.00977V2.00977Z" fill="#737373"/></svg>
                        ) : (
                          <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 20 20" fill="none"><path d="M15.9619 7.07617C16.1039 6.68731 16.5349 6.48721 16.9238 6.62891C17.3125 6.77102 17.5127 7.20102 17.3711 7.58984C16.9294 8.80019 16.1977 9.87801 15.2471 10.7334L15.9072 11.5176C16.1736 11.8344 16.1331 12.3076 15.8164 12.5742C15.4996 12.8407 15.0265 12.7999 14.7598 12.4834L14.0381 11.627C13.5221 11.9367 12.9743 12.185 12.4053 12.3682L12.7324 13.8369C12.8223 14.2413 12.5665 14.6416 12.1621 14.7314C11.7579 14.821 11.3574 14.5663 11.2676 14.1621L10.9395 12.6895C10.6289 12.7269 10.3151 12.7471 10 12.7471C9.68445 12.7471 9.37055 12.727 9.05957 12.6895L8.73242 14.1621C8.64259 14.5665 8.24127 14.8213 7.83691 14.7314C7.43289 14.6414 7.17779 14.2411 7.26758 13.8369L7.59375 12.3682C7.02482 12.1848 6.47686 11.9367 5.96094 11.627L5.24023 12.4834C4.97353 12.7999 4.50036 12.8405 4.18359 12.5742C3.86694 12.3074 3.82712 11.8344 4.09375 11.5176L4.75293 10.7334C3.80243 9.87807 3.07053 8.80007 2.62891 7.58984C2.48727 7.20092 2.68734 6.77091 3.07617 6.62891C3.46513 6.48702 3.89498 6.68734 4.03711 7.07617C4.48322 8.29862 5.29514 9.35452 6.36133 10.1006C7.42773 10.8466 8.69855 11.247 10 11.2471C11.3014 11.247 12.5713 10.8466 13.6377 10.1006C14.7041 9.35452 15.5157 8.29879 15.9619 7.07617ZM2.00977 2.00977H2V2H2.00977V2.00977Z" fill="#737373"/></svg>
                        )}
                      </button>
                    </div>
                    {errors.repeatPassword && <p className="text-xs text-Stockia-Red-Error-600---Activo mt-1">{errors.repeatPassword}</p>}
                  </div>

                  </div>
                )}

              {/* When editing, allow user to opt-in to change password */}
              {isEditMode && !changePassword && (
                <div className="mt-2">
                  <button type="button" onClick={() => setChangePassword(true)} className="text-sm text-[#436086] underline">Cambiar contraseña</button>
                </div>
              )}

              <div>
                <label className="text-sm font-medium text-Stockia-Neutral-950 flex items-center gap-1">Rol asignado <span className="text-Stockia-Red-Error-600---Activo">*</span></label>
                <Select className="mt-2 bg-white" value={form.role} onChange={(e) => setForm(f => ({ ...f, role: e.target.value }))}>
                  <option value="">Seleccionar rol asignado</option>
                  <option value="ADMIN">ADMINISTRADOR</option>
                  <option value="MANAGER">ENCARGADO</option>
                  <option value="USER">USUARIO</option>
                </Select>
                {errors.role && <p className="text-xs text-Stockia-Red-Error-600---Activo mt-1">{errors.role}</p>}
              </div>

              <div>
                <label className="text-sm font-medium text-Stockia-Neutral-950 flex items-center gap-1">Estado de la cuenta <span className="text-Stockia-Red-Error-600---Activo">*</span></label>
                <Select className="mt-2 bg-white" value={form.accountStatus} onChange={(e) => setForm(f => ({ ...f, accountStatus: e.target.value }))}>
                  <option value="ACTIVE">Activo</option>
                  <option value="INACTIVE">Inactivo</option>
                </Select>
              </div>
            </div>

            <div className="mt-6 flex justify-end gap-4">
              <Button type="button" variant="ghost" className="w-36 h-11 text-[#404040] border-gray-300 hover:bg-gray-200" onClick={handleClose}>Cancelar</Button>
              <Button type="submit" className="w-44 h-11 bg-[#436086] hover:bg-[#364d6e] text-white font-medium">{isEditMode ? 'Guardar cambios' : 'Registrar usuario'}</Button>
            </div>
          </form>
        )}

        {isSubmitting && (
          <LoadingModal
            title="Guardando usuario..."
            description="Estamos guardando el usuario, por favor espera..."
            className="absolute top-0 left-0 w-full h-full bg-stokia-neutral-50"
          />
        )}

        {showSuccess && (
          <SuccessModal
            className="absolute top-0 left-0 w-full h-full bg-card"
            title={isEditMode ? '¡Usuario actualizado!' : '¡Listo!'}
            description={isEditMode ? 'Los cambios han sido guardados.' : 'Usuario registrado correctamente.'}
            primaryButtonText={isEditMode ? 'Seguir editando' : 'Registrar usuario'}
            secondaryButtonText="Volver"
            onPrimaryClick={isEditMode ? undefined : handleRegisterAnother}
            onSecondaryClick={handleClose}
            showButtons={true}
          />
        )}
      </div>
    </div>
  );
}
