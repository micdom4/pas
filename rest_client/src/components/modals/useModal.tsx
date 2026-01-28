import {createContext, useContext} from "react";
import type {ConfirmationOptions} from "./ModalProvider.tsx";

interface ModalContextType {
    showConfirmation: (options: ConfirmationOptions) => void;
}

export const ModalContext = createContext<ModalContextType | undefined>(undefined);

const useModal = () => {
    const context = useContext(ModalContext);
    if (!context) {
        throw new Error('useModal must be used within a ModalProvider');
    }
    return context;
};

export default useModal;