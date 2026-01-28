import React from 'react';
import {Button, Form, Modal} from 'react-bootstrap';
import {Formik} from 'formik';
import useToast from '../../components/toasts/useToast.tsx';
import useModal from "./useModal.tsx";
import {changePasswordSchema} from "../../model/LoginTypes.ts";
import {loginApi} from "../../api/LoginApi.ts";
import type {LoggedUser} from "../../contexts/LoggedUserContext/types.ts";

interface ChangePasswordModalProps {
    show: boolean;
    handleClose: () => void;
    user: LoggedUser;
    onSuccess: () => void;
}

export const ChangePasswordModal: React.FC<ChangePasswordModalProps> = ({show, handleClose, user, onSuccess}) => {
    const {addToast} = useToast();
    const {showConfirmation} = useModal()

    if (!user) return null;

    return (
        <Modal show={show} onHide={handleClose} centered>
            <Modal.Header closeButton>
                <Modal.Title>Change password for user: {user.login}</Modal.Title>
            </Modal.Header>

            <Formik
                enableReinitialize={true}
                initialValues={{oldPassword: '', newPassword: '', confirmNewPassword: ''}}
                validationSchema={changePasswordSchema}
                onSubmit={(values, {setSubmitting}) => {
                    showConfirmation({
                        title: 'Password change',
                        message: `Do you really want to change your password?`,
                        cancelLabel: 'No',
                        confirmLabel: 'Yes',
                        variant: 'warning',

                        onConfirm: () => {
                            loginApi.changePassword(values)
                                .then(() => {
                                    addToast('Success!', 'Your password has been changed', 'success');
                                    onSuccess();
                                    handleClose();
                                })
                                .catch((e) => {
                                    addToast('Error!', `Cannot change password! Error: ${e}`, 'danger');
                                })
                        }
                    })
                    setSubmitting(false);
                }}
            >
                {({
                      values,
                      errors,
                      touched,
                      handleChange,
                      handleBlur,
                      handleSubmit,
                      isSubmitting,
                  }) => (
                    <Form noValidate onSubmit={handleSubmit}>
                        <Modal.Body>
                            <Form.Group controlId="formOldPassword">
                                <Form.Label>Old Password</Form.Label>
                                <Form.Control
                                    type="password"
                                    name="oldPassword"
                                    placeholder="Your old password"
                                    value={values.oldPassword}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    isInvalid={touched.oldPassword && !!errors.oldPassword}
                                    autoFocus
                                />
                                <Form.Control.Feedback type="invalid">
                                    {errors.oldPassword}
                                </Form.Control.Feedback>
                            </Form.Group>

                            <Form.Group controlId="formNewPassword">
                                <Form.Label>New Password</Form.Label>
                                <Form.Control
                                    type="password"
                                    name="newPassword"
                                    placeholder="Your new password"
                                    value={values.newPassword}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    isInvalid={touched.newPassword && !!errors.newPassword}
                                />
                                <Form.Control.Feedback type="invalid">
                                    {errors.newPassword}
                                </Form.Control.Feedback>
                            </Form.Group>

                            <Form.Group controlId="formConfirmNewPassword">
                                <Form.Label>Confirm New Password</Form.Label>
                                <Form.Control
                                    type="password"
                                    name="confirmNewPassword"
                                    value={values.confirmNewPassword}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    isInvalid={touched.confirmNewPassword && !!errors.confirmNewPassword}
                                />
                                <Form.Control.Feedback type="invalid">
                                    {errors.confirmNewPassword}
                                </Form.Control.Feedback>
                            </Form.Group>
                        </Modal.Body>

                        <Modal.Footer>
                            <Button variant="secondary" onClick={handleClose}>
                                Cancel
                            </Button>
                            <Button type="submit" variant="primary" disabled={isSubmitting}>
                                {isSubmitting ? 'Changing...' : 'Change'}
                            </Button>
                        </Modal.Footer>
                    </Form>
                )}
            </Formik>
        </Modal>
    );
};