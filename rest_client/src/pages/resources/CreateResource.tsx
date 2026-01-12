import { Formik } from 'formik';
import { Form, Button } from 'react-bootstrap';
import {ResourceSchema} from "../../model/ResourceTypes.ts";
import useToast from "../../components/toasts/useToast.tsx";
import {resourceApi} from "../../api/ResourceRestApi.ts";
import useModal from "../../components/modals/useModal.tsx";

export default function CreateResource () {
    const {addToast} = useToast()
    const {showConfirmation} = useModal()

    return (
        <Formik
            initialValues={{ cpuNumber: '', ramGiB: '', storageGiB: '' }}
            validationSchema={ResourceSchema}
            onSubmit={(values, { setSubmitting }) => {
                showConfirmation({
                    title: 'Create new resource confirmation',
                    message: 'Do you really want to create new resource with provided values?',
                    variant: 'primary',

                    onConfirm: async ()=> {
                        const formattedValues = {
                            cpuNumber: Number(values.cpuNumber),
                            ramGiB: Number(values.ramGiB),
                            storageGiB: Number(values.storageGiB)
                        };
                        console.log('Sending resource to create: ', values);
                        resourceApi.create(formattedValues)
                            .then((response) => {
                                addToast(
                                    'Success!',
                                    `New resource has been successfully created. ID: #${response.data.id}`,
                                    'success'
                                )
                            })
                            .catch((error) => {
                                addToast(
                                    'Error!',
                                    `Error occurred while creating new resource. Error: ${error}`,
                                    'danger'
                                )
                            })
                            .finally(() => setSubmitting(false))
                    }
                })
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
                    <h4 className="mb-3">Nowy Zasób (VM)</h4>

                    <Form.Group className="mb-3" controlId="formCpu">
                        <Form.Label>Liczba CPU (rdzenie)</Form.Label>
                        <Form.Control
                            type="number"
                            name="cpuNumber"
                            value={values.cpuNumber}
                            onChange={handleChange}
                            onBlur={handleBlur}
                            isInvalid={touched.cpuNumber && !!errors.cpuNumber}
                            min="1"
                        />
                        <Form.Control.Feedback type="invalid">
                            {errors.cpuNumber}
                        </Form.Control.Feedback>
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formRam">
                        <Form.Label>RAM [GiB]</Form.Label>
                        <Form.Control
                            type="number"
                            name="ramGiB"
                            value={values.ramGiB}
                            onChange={handleChange}
                            onBlur={handleBlur}
                            isInvalid={touched.ramGiB && !!errors.ramGiB}
                            min="1"
                        />
                        <Form.Control.Feedback type="invalid">
                            {errors.ramGiB}
                        </Form.Control.Feedback>
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formStorage">
                        <Form.Label>Storage [GiB]</Form.Label>
                        <Form.Control
                            type="number"
                            name="storageGiB"
                            value={values.storageGiB}
                            onChange={handleChange}
                            onBlur={handleBlur}
                            isInvalid={touched.storageGiB && !!errors.storageGiB}
                            min="1"
                        />
                        <Form.Control.Feedback type="invalid">
                            {errors.storageGiB}
                        </Form.Control.Feedback>
                    </Form.Group>

                    <Button variant="success" type="submit" disabled={isSubmitting}>
                        {isSubmitting ? 'Saving...' : 'Create Virtual Machine'}
                    </Button>
                </Form>
            )}
        </Formik>
    );
};