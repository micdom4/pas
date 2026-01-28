import React, {type ReactNode, useState} from 'react';
import {Button, Modal, Spinner} from 'react-bootstrap';
import {ModalContext} from "./useModal";

export interface ConfirmationOptions {
    title: string;
    message: string;
    variant?: 'danger' | 'primary' | 'warning';
    confirmLabel?: string;
    cancelLabel?: string;
    onConfirm: () => Promise<void> | void;
}

export const ModalProvider: React.FC<{ children: ReactNode }> = ({children}) => {
    const [show, setShow] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [options, setOptions] = useState<ConfirmationOptions | null>(null);

    const showConfirmation = (opts: ConfirmationOptions) => {
        setOptions(opts);
        setShow(true);
    };

    const handleClose = () => {
        if (!isLoading) {
            setShow(false);
            setTimeout(() => setOptions(null), 300);
        }
    };

    const handleConfirm = async () => {
        if (options?.onConfirm) {
            try {
                setIsLoading(true);
                await options.onConfirm();
                setShow(false);
            } catch (error) {
                console.error("Error while in modal operation", error);
            } finally {
                setIsLoading(false);
            }
        } else {
            setShow(false);
        }
    };

    return (
        <ModalContext.Provider value={{showConfirmation}}>
            {children}

            <Modal show={show} onHide={handleClose} backdrop="static" keyboard={!isLoading} centered>
                {options && (
                    <>
                        <Modal.Header closeButton={!isLoading}>
                            <Modal.Title>{options.title}</Modal.Title>
                        </Modal.Header>
                        <Modal.Body>
                            <span>
                                {options.message}
                            </span>
                        </Modal.Body>
                        <Modal.Footer>
                            <Button variant="secondary" onClick={handleClose} disabled={isLoading}>
                                {options.cancelLabel || 'Cancel'}
                            </Button>
                            <Button
                                variant={options.variant || 'primary'}
                                onClick={handleConfirm}
                                disabled={isLoading}
                            >
                                {isLoading ? (
                                    <>
                                        <Spinner as="span" animation="border" size="sm" role="status" aria-hidden="true"
                                                 className="me-2"/>
                                        Processing...
                                    </>
                                ) : (
                                    options.confirmLabel || 'Confirm'
                                )}
                            </Button>
                        </Modal.Footer>
                    </>
                )}
            </Modal>
        </ModalContext.Provider>
    );
};