import {use, useEffect, useState, useTransition} from 'react';
import {Formik} from 'formik';
import {Button, Col, Form, Row} from 'react-bootstrap';
import useToast from '../../components/toasts/useToast.tsx';
import {resourceApi} from "../../api/ResourceRestApi.ts";
import {allocationApi} from "../../api/AllocationRestApi.ts";
import type {ResourceType} from "../../model/ResourceTypes.ts";
import * as Yup from "yup";
import useModal from "../../components/modals/useModal.tsx";
import LoggedUserContext from "../../contexts/LoggedUserContext";
import {userApi} from "../../api/UserRestApi.ts";
import type {UserType} from "../../model/UserTypes.ts";

const AllocationSchema = Yup.object().shape({
    clientId: Yup.string()
        .required('You must choose a client'),
    vmId: Yup.string()
        .required('You must choose a resource'),
});

export default function CreateAllocation() {
    const {addToast} = useToast();
    const {showConfirmation} = useModal()

    const {user} = use(LoggedUserContext)

    const [client, setClient] = useState<UserType>()
    const [resources, setResources] = useState<ResourceType[]>([]);
    const [isPending, startTransition] = useTransition()

    const fetchData = async () => {
        try {
            const [resourcesResponse, clientResponse] = await Promise.all([
                resourceApi.getAll(),
                userApi.getByLogin(user.login || '')
            ]);

            setResources(resourcesResponse.data);
            setClient(clientResponse.data)
        } catch (error) {
            addToast('Fetch data error', `Error while fetching resources. Error: ${error}`, 'danger');
            console.error(error);
        }
    };

    useEffect(() => {
        startTransition(() => fetchData());
    }, []);

    return (
        <Formik
            initialValues={{
                login: '',
                vmId: '',
            }}
            validationSchema={AllocationSchema}
            onSubmit={(values, {setSubmitting, resetForm}) => {

                showConfirmation({
                    title: 'Allocation Creation Confirmation',
                    message: `Do you really want to create allocation of VM #${values.vmId}?`,
                    variant: 'primary',

                    onConfirm: async () => {

                        const payload = {
                            clientId: client?.id || '',
                            resourceId: values.vmId,
                        };

                        console.log("Sending ID:", payload.clientId, payload.resourceId);

                        allocationApi.create(payload)
                            .then(() => {
                                addToast('Success', 'Allocation has been created successfully', 'success');
                                resetForm();
                            })
                            .catch((error) => {
                                addToast('Error', `Error while creating new allocation. Error: ${error}`, 'danger');
                            })
                    }
                })
                setSubmitting(false);
            }}
        >
            {({
                  values, errors, touched, handleChange, handleBlur, handleSubmit, isSubmitting
              }) => (
                <Form noValidate onSubmit={handleSubmit} className="p-4 border rounded shadow-sm bg-white">
                    <h4 className="mb-3">New Allocation</h4>

                    {isPending ? (
                        <p className="text-muted">Loading lists...</p>
                    ) : (
                        <>
                            <Row>
                                <Col md={6}>
                                    <Form.Group className="mb-3" controlId="formClient">
                                        <Form.Label>Client</Form.Label>
                                        <Form.Select
                                            name="login"
                                            value={values.login}
                                            disabled
                                        >
                                            <option value={user.login || ''}>{user.login}</option>
                                        </Form.Select>
                                    </Form.Group>
                                </Col>

                                <Col md={6}>
                                    <Form.Group className="mb-3" controlId="formVm">
                                        <Form.Label>Virtual Machine (VM)</Form.Label>
                                        <Form.Select
                                            name="vmId"
                                            value={values.vmId}
                                            onChange={handleChange}
                                            onBlur={handleBlur}
                                            isInvalid={touched.vmId && !!errors.vmId}
                                        >
                                            <option value="">-- Choose VM --</option>
                                            {resources.map((res) => (
                                                <option key={res.id} value={res.id}>
                                                    ID: {res.id} | CPU: {res.cpuNumber} | RAM: {res.ramGiB} |
                                                    Storage: {res.storageGiB}
                                                </option>
                                            ))}
                                        </Form.Select>
                                        <Form.Control.Feedback type="invalid">
                                            {errors.vmId}
                                        </Form.Control.Feedback>
                                    </Form.Group>
                                </Col>
                            </Row>

                            <Button variant="primary" type="submit" disabled={isSubmitting} className="mt-2">
                                {isSubmitting ? 'Saving...' : 'Create Allocation'}
                            </Button>
                        </>
                    )}
                </Form>
            )}
        </Formik>
    );
};