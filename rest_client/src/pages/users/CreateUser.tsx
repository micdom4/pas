import {Formik} from 'formik';
import {Button, Col, Form, Row} from 'react-bootstrap';
import {StringTypeOfUser, TypeOfUser, UserSchema} from '../../model/UserTypes.ts';
import useToast from "../../components/toasts/useToast.tsx";
import useModal from "../../components/modals/useModal.tsx";
import {loginApi} from "../../api/LoginApi.ts";

export default function CreateUser() {
    const {addToast} = useToast()
    const {showConfirmation} = useModal()

    return (
        <>
            <Formik
                initialValues={{login: '', name: '', password: '', surname: '', type: TypeOfUser.CLIENT}}
                validationSchema={UserSchema}
                onSubmit={(values, {setSubmitting, resetForm}) => {
                    showConfirmation({
                        title: 'Confirmation of creation',
                        message: `Do you really want to create user with login "${values.login}"?`,
                        confirmLabel: 'Create',
                        variant: 'primary',

                        onConfirm: async () => {
                            console.log('Sending user to create: ', values);
                            loginApi.register(values)
                                .then(() => {
                                    addToast(
                                        'Success!',
                                        `User "${values.login}" has been successfully created.`,
                                        'success');
                                    resetForm();
                                })
                                .catch((error) => {
                                    addToast(
                                        'Error!',
                                        `Error occurred while creating user "${values.login}": ${error}`,
                                        'danger'
                                    );
                                })
                        }
                    });

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
                    <Form noValidate onSubmit={handleSubmit} className="p-4 border rounded shadow-sm bg-white">
                        <h4 className="mb-3">Create New User</h4>

                        <Form.Group className="mb-3" controlId="formLogin">
                            <Form.Label>Login</Form.Label>
                            <Form.Control
                                type="text"
                                name="login"
                                placeholder="e.g. WSmith"
                                value={values.login}
                                onChange={handleChange}
                                onBlur={handleBlur}
                                isInvalid={touched.login && !!errors.login}
                            />
                            <Form.Control.Feedback type="invalid">
                                {errors.login}
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Row>
                            <Col md={6}>
                                <Form.Group className="mb-3" controlId="formName">
                                    <Form.Label>Name</Form.Label>
                                    <Form.Control
                                        type="text"
                                        name="name"
                                        placeholder="e.g. Winston"
                                        value={values.name}
                                        onChange={handleChange}
                                        onBlur={handleBlur}
                                        isInvalid={touched.name && !!errors.name}
                                    />
                                    <Form.Control.Feedback type="invalid">
                                        {errors.name}
                                    </Form.Control.Feedback>
                                </Form.Group>
                            </Col>

                            <Col md={6}>
                                <Form.Group className="mb-3" controlId="formSurname">
                                    <Form.Label>Surname</Form.Label>
                                    <Form.Control
                                        type="text"
                                        name="surname"
                                        placeholder="e.g. Smith"
                                        value={values.surname}
                                        onChange={handleChange}
                                        onBlur={handleBlur}
                                        isInvalid={touched.surname && !!errors.surname}
                                    />
                                    <Form.Control.Feedback type="invalid">
                                        {errors.surname}
                                    </Form.Control.Feedback>
                                </Form.Group>
                            </Col>
                        </Row>

                        <Row>
                            <Col md={6}>
                                <Form.Group className="mb-3" controlId="formPassword">
                                    <Form.Label>Password</Form.Label>
                                    <Form.Control
                                        type="password"
                                        name="password"
                                        placeholder="e.g. ***** ***"
                                        value={values.password}
                                        onChange={handleChange}
                                        onBlur={handleBlur}
                                        isInvalid={touched.password && !!errors.password}
                                    />
                                    <Form.Control.Feedback type='invalid'>
                                        {errors.password}
                                    </Form.Control.Feedback>
                                </Form.Group>
                            </Col>
                            <Col md={6}>
                                <Form.Group className="mb-3" controlId="formType">
                                    <Form.Label>Type of user</Form.Label>
                                    <Form.Select
                                        name="type"
                                        value={values.type}
                                        onChange={handleChange}
                                        onBlur={handleBlur}
                                        isInvalid={touched.type && !!errors.type}
                                    >
                                        {Object.values(StringTypeOfUser).map((type) => (
                                            <option key={type} value={type}>
                                                {type}
                                            </option>
                                        ))}
                                    </Form.Select>
                                    <Form.Control.Feedback type="invalid">
                                        {errors.type}
                                    </Form.Control.Feedback>
                                </Form.Group>
                            </Col>
                        </Row>

                        <Button variant="primary" type="submit" disabled={isSubmitting}>
                            {isSubmitting ? 'Saving...' : 'Create'}
                        </Button>
                    </Form>
                )}
            </Formik>
        </>
    );
};