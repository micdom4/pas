import {registerDataSchema, type RegisterDataType, type RegisterFormType} from "../../model/LoginTypes.ts";
import {Formik, type FormikHelpers} from "formik";
import {Button, Container, Form} from "react-bootstrap";
import {Link, useNavigate} from "react-router-dom";
import {loginApi} from "../../api/LoginApi.ts";
import useToast from "../../components/toasts/useToast.tsx";
import useModal from "../../components/modals/useModal.tsx";
import {TypeOfUser} from "../../model/UserTypes.ts";

export default function RegisterPage() {
    const {addToast} = useToast();

    const {showConfirmation} = useModal();

    const navigate = useNavigate();

    const handleRegistration = async (
        values: RegisterFormType,
        {setSubmitting, setStatus}: FormikHelpers<RegisterFormType>
    ) => {
        showConfirmation({
            title: 'Confirmation of registration',
            message: `Do you really want to create new account with username: "${values.login}"?`,
            confirmLabel: 'Yes',
            cancelLabel: 'No',
            variant: 'primary',

            onConfirm: async () => {
                try {
                    setStatus(null);

                    const payload: RegisterDataType = {
                        login: values.login,
                        name: values.name,
                        password: values.password,
                        surname: values.surname,
                        type: TypeOfUser.CLIENT
                    }

                    console.log('Wysyłanie danych do API:', payload);

                    await loginApi.register(payload)
                        .then((response) => {
                            console.log(response);

                            addToast(
                                'Signing up successful!',
                                `New user: "${values.login}" has been successfully created.`,
                                'success');
                            navigate('/login');
                        })
                        .catch(() => {
                            setStatus('Invalid username and/or password.');
                            addToast(
                                'Error!',
                                `Error while registration user: ${values.login}!`,
                                'danger'
                            );
                        })

                } catch (error) {
                    console.error('Błąd rejestracji', error);
                    setStatus('Nieprawidłowy login lub hasło.');
                } finally {
                    setSubmitting(false);
                }
            }
        });
    };

    return (
        <Formik
            initialValues={{login: '', name: '', password: '', confirmPassword: '', surname: ''}}
            validationSchema={registerDataSchema}
            onSubmit={handleRegistration}
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
                    <h3 className="mb-3">Registration Form</h3>

                    <Form.Group className="mb-3" controlId="formLogin">
                        <Form.Label>Login</Form.Label>
                        <Form.Control
                            type="text"
                            name="login"
                            value={values.login}
                            onChange={handleChange}
                            onBlur={handleBlur}
                            isInvalid={touched.login && !!errors.login}
                        />
                        <Form.Control.Feedback type="invalid">
                            {errors.login}
                        </Form.Control.Feedback>
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formLogin">
                        <Form.Label>Name</Form.Label>
                        <Form.Control
                            type="text"
                            name="name"
                            value={values.name}
                            onChange={handleChange}
                            onBlur={handleBlur}
                            isInvalid={touched.name && !!errors.name}
                        />
                        <Form.Control.Feedback type="invalid">
                            {errors.name}
                        </Form.Control.Feedback>
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formLogin">
                        <Form.Label>Surname</Form.Label>
                        <Form.Control
                            type="text"
                            name="surname"
                            value={values.surname}
                            onChange={handleChange}
                            onBlur={handleBlur}
                            isInvalid={touched.surname && !!errors.surname}
                        />
                        <Form.Control.Feedback type="invalid">
                            {errors.surname}
                        </Form.Control.Feedback>
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formPassword">
                        <Form.Label>Password</Form.Label>
                        <Form.Control
                            type="password"
                            name="password"
                            value={values.password}
                            onChange={handleChange}
                            onBlur={handleBlur}
                            isInvalid={touched.password && !!errors.password}
                        />
                        <Form.Control.Feedback type="invalid">
                            {errors.password}
                        </Form.Control.Feedback>
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formConfirmPassword">
                        <Form.Label>Confirm Password</Form.Label>
                        <Form.Control
                            type="password"
                            name="confirmPassword"
                            value={values.confirmPassword}
                            onChange={handleChange}
                            onBlur={handleBlur}
                            isInvalid={touched.confirmPassword && !!errors.confirmPassword}
                        />
                        <Form.Control.Feedback type="invalid">
                            {errors.confirmPassword}
                        </Form.Control.Feedback>
                    </Form.Group>

                    <Button variant="primary" type="submit" disabled={isSubmitting}>
                        {isSubmitting ? 'Registration in progress...' : 'Register'}
                    </Button>

                    <Container className={'mt-3'}>
                        <Form.Text>
                            Already have an account?
                        </Form.Text>
                    </Container>
                    <Container>
                        <Form.Text>
                            Go to the
                            <Link to={'/login'}> login page</Link>.
                        </Form.Text>
                    </Container>
                </Form>
            )}
        </Formik>
    );
}