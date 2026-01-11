import React, {type ReactNode, useState} from 'react';
import {Toast, ToastContainer} from 'react-bootstrap';
import { ToastContext } from "./useToast";

export interface ToastMessage {
    id: number;
    title: string;
    message: string;
    variant: 'success' | 'danger' | 'warning' | 'info';
}

export const ToastProvider: React.FC<{ children: ReactNode }> = ({children}) => {
    const TIMEOUT_IN_MS = 5000;

    const [toasts, setToasts] = useState<ToastMessage[]>([]);

    const addToast = (title: string, message: string, variant: ToastMessage['variant'] = 'success') => {
        const id = Date.now();
        setToasts((prev) => [...prev, {id, title, message, variant}]);
    };

    const removeToast = (id: number) => {
        setToasts((prev) => prev.filter((t) => t.id !== id));
    };

    return (
        <ToastContext.Provider value={{addToast}}>
            {children}

            <ToastContainer position="top-end" className="p-3" style={{zIndex: 9999, position: 'fixed'}}>
                {toasts.map((toast) => (
                    <Toast
                        key={toast.id}
                        onClose={() => removeToast(toast.id)}
                        show={true}
                        delay={TIMEOUT_IN_MS}
                        autohide
                        bg={toast.variant}
                    >
                        <Toast.Header>
                            <strong className="me-auto">{toast.title}</strong>
                            <small>teraz</small>
                        </Toast.Header>
                        <Toast.Body className={toast.variant === 'danger' ? 'text-white' : ''}>
                            {toast.message}
                        </Toast.Body>
                    </Toast>
                ))}
            </ToastContainer>
        </ToastContext.Provider>
    );
};