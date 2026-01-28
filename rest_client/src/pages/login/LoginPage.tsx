import {loginDataSchema, type LoginDataType} from "../../model/LoginTypes.ts";
import {Formik, type FormikHelpers} from "formik";
import {Button, Container, Form} from "react-bootstrap";
import {Link, useNavigate} from "react-router-dom";
import {loginApi} from "../../api/LoginApi.ts";
import useToast from "../../components/toasts/useToast.tsx";
import {use} from "react";
import LoggedUserContext from "../../contexts/LoggedUserContext";
import {LoggedUser} from "../../contexts/LoggedUserContext/types.ts";
import {convertRole} from "../../utils";

export default function LoginPage() {
    const {addToast} = useToast();

    const {setUser} = use(LoggedUserContext);

    const navigate = useNavigate();

    const handleLogin = async (
        values: LoginDataType,
        {setSubmitting, setStatus}: FormikHelpers<LoginDataType>
    ) => {
        try {
            setStatus(null);

            console.log('Wysyłanie danych do API:', values);

            await loginApi.login(values)
                .then((response) => {
                    console.log(response);
                    setUser(new LoggedUser(values.login, response.data.token, convertRole(response.data.roles)));

                    addToast(
                        'Login successful!',
                        `You are now logged as user: "${values.login}".`,
                        'success');
                    navigate('/home');
                })
                .catch(() => {
                    setStatus('Invalid login and/or password.');
                    addToast(
                        'Error!',
                        `Invalid credentials!`,
                        'danger'
                    );
                })

        } catch (error) {
            console.error('Error while logging in', error);
            setStatus('Incorrect login or password.');
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <Formik
            initialValues={{login: '', password: ''}}
            validationSchema={loginDataSchema}
            onSubmit={handleLogin}
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
                    <h3 className="mb-3">Login</h3>

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

                    <Button variant="primary" type="submit" disabled={isSubmitting}>
                        {isSubmitting ? 'Logging in...' : 'Login'}
                    </Button>

                    <Container className={'mt-3'}>
                        <Form.Text>
                            Don't have any account yet?
                        </Form.Text>
                    </Container>
                    <Container>
                        <Form.Text>
                            Go to the
                            <Link to={'/register'}> registration page</Link>.
                        </Form.Text>
                    </Container>

                </Form>
            )}
        </Formik>
    );
}