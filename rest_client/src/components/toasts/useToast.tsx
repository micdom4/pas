import {createContext, useContext} from "react";
import type {ToastMessage} from "./ToastProvider.tsx";

interface ToastContextType {
    addToast: (title: string, message: string, variant?: ToastMessage['variant']) => void;
}

export const ToastContext = createContext<ToastContextType | undefined>(undefined);

const useToast = () => {
    const context = useContext(ToastContext);
    if (!context) {
        throw new Error('useToast must be used within a ToastProvider');
    }
    return context;
};

export default useToast;