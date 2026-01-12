import React from 'react';
import {Button, Form, Modal} from 'react-bootstrap';
import {Formik} from 'formik';
import * as Yup from 'yup';
import type {UserType} from '../../model/UserTypes.ts';
import useToast from '../../components/toasts/useToast.tsx';
import {userApi} from "../../api/UserRestApi.ts";
import useModal from "./useModal.tsx";

interface EditUserModalProps {
    show: boolean;
    handleClose: () => void;
    user: UserType | null;
    onSuccess: () => void;
}

const EditSchema = Yup.object().shape({
    surname: Yup.string()
        .min(2,)
        .matches(/^[A-Z][a-z]{1,19}(-[A-Z][a-z]{1,19})?$/, "surname must start with a capital letter")
        .required(),
});

export const EditUserModal: React.FC<EditUserModalProps> = ({show, handleClose, user, onSuccess}) => {
    const {addToast} = useToast();
    const {showConfirmation} = useModal()

    if (!user) return null;

    return (
        <Modal show={show} onHide={handleClose} centered>
            <Modal.Header closeButton>
                <Modal.Title>Edit user: {user.login}</Modal.Title>
            </Modal.Header>

            <Formik
                enableReinitialize={true}
                initialValues={{surname: user.surname}}
                validationSchema={EditSchema}
                onSubmit={(values, {setSubmitting}) => {
                    showConfirmation({
                        title: 'Surname change',
                        message: `Do you really want to change surname of "${user.login}"?`,
                        cancelLabel: 'No',
                        confirmLabel: 'Yes',

                        onConfirm: () => {
                            userApi.edit(user.id, {surname: values.surname})
                                .then(() => {
                                    addToast('Success!', 'Surname has been updated', 'success');
                                    onSuccess();
                                    handleClose();
                                })
                                .catch((e) => {
                                    addToast('Error!', `Error occurred while updating surname. Error: ${e}`, 'danger');
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
                            <Form.Group controlId="formLogin" className="mb-3">
                                <Form.Label>Login</Form.Label>
                                <Form.Control type="text" value={user.login} disabled/>
                            </Form.Group>

                            <Form.Group controlId="formName" className="mb-3">
                                <Form.Label>Name</Form.Label>
                                <Form.Control type="text" value={user.name} disabled/>
                            </Form.Group>

                            <Form.Group controlId="formSurname">
                                <Form.Label>Surname</Form.Label>
                                <Form.Control
                                    type="text"
                                    name="surname"
                                    value={values.surname}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    isInvalid={touched.surname && !!errors.surname}
                                    autoFocus
                                />
                                <Form.Control.Feedback type="invalid">
                                    {errors.surname}
                                </Form.Control.Feedback>
                            </Form.Group>
                        </Modal.Body>

                        <Modal.Footer>
                            <Button variant="secondary" onClick={handleClose}>
                                Cancel
                            </Button>
                            <Button type="submit" variant="primary" disabled={isSubmitting}>
                                {isSubmitting ? 'Saving...' : 'Save'}
                            </Button>
                        </Modal.Footer>
                    </Form>
                )}
            </Formik>
        </Modal>
    );
};