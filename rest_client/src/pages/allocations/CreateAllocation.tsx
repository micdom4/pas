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
    login: Yup.string().required('You must choose a client'),
    vmId: Yup.string().required('You must choose a resource'),
});

const AllocationSchema2 = Yup.object().shape({
    login: Yup.string(),
    vmId: Yup.string().required('You must choose a resource'),
});

export default function CreateAllocation() {
    const {addToast} = useToast();
    const {showConfirmation} = useModal()

    const {user} = use(LoggedUserContext)

    const [clients, setClients] = useState<UserType[]>([]);
    const [client, setClient] = useState<UserType>()
    const [resources, setResources] = useState<ResourceType[]>([]);
    const [isPending, startTransition] = useTransition()

    const fetchData = async () => {
        try {
            const [resourcesResponse, clientResponse] = await Promise.all([
                resourceApi.getAll(),
                (user.isAdmin() || user.isManager()) ? userApi.getAll() : userApi.getByLogin(user.login || '')
            ]);

            setResources(resourcesResponse.data);
            if (user.isAdmin() || user.isManager()) {
                setClients(clientResponse.data as UserType[])
            } else {
                setClient(clientResponse.data as UserType)
            }
        } catch (error) {
            addToast('Fetch data error', `Error while fetching resources. Error: ${error}`, 'danger');
            console.error(error);
        }
    };

    useEffect(() => {
        startTransition(async () => await fetchData());
    }, []);

    return (
        <Formik
            initialValues={{
                login: '',
                vmId: '',
            }}
            validationSchema={user.isClient() ? AllocationSchema2 : AllocationSchema}
            onSubmit={(values, {setSubmitting, resetForm}) => {

                showConfirmation({
                    title: 'Allocation Creation Confirmation',
                    message: `Do you really want to create allocation of VM #${values.vmId}?`,
                    variant: 'primary',

                    onConfirm: async () => {
                        const payload = {
                            clientId: user.isClient() ? client?.id || 'dupa' : clients.find((c) => c.login == values.login)?.id || 'dupsko',
                            resourceId: values.vmId,
                        };

                        console.log("Sending ID:", payload.clientId, payload.resourceId);

                        let etag
                        if (!user.isClient()) {
                            etag = await allocationApi.prepareForCreate(payload.clientId, payload.resourceId)
                        }

                        console.log("Received etag: ", etag)

                        allocationApi.create(payload, etag)
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
                  values,
                  errors,
                  touched,
                  handleChange,
                  handleBlur,
                  handleSubmit,
                  isSubmitting
              }) => (
                <Form noValidate onSubmit={handleSubmit} className="p-4 border rounded shadow-sm bg-white">
                    <h4 className="mb-3">New Allocation</h4>

                    {isPending ? (
                        <p className="text-muted">Loading list...</p>
                    ) : (
                        <>
                            <Row>
                                <Col md={6}>
                                    <Form.Group className="mb-3" controlId="formClient">
                                        <Form.Label>Client</Form.Label>
                                        {(user.isAdmin() || user.isManager()) && <>
                                            <Form.Select
                                                name="login"
                                                value={values.login}
                                                onChange={handleChange}
                                                onBlur={handleBlur}
                                                isInvalid={touched.login && !!errors.login}
                                            >
                                                <option value="">-- Choose Client --</option>
                                                {clients.filter(u => u.type.toString() === "CLIENT" && u.active)
                                                    .map((client) => (
                                                        <option key={client.id} value={client.login}>
                                                            {client.name} {client.surname} ({client.login})
                                                        </option>
                                                    ))}
                                            </Form.Select>
                                            <Form.Control.Feedback type="invalid">
                                                {errors.login}
                                            </Form.Control.Feedback>
                                        </>}
                                        {user.isClient() && <>
                                            <Form.Select
                                                name="login"
                                                value={values.login}
                                                onChange={handleChange}
                                                onBlur={handleBlur}
                                                isInvalid={touched.login && !!errors.login}
                                                disabled
                                            >
                                                <option key={user.login} value={user.login || 'ass'}>{user.login}</option>
                                            </Form.Select>
                                        </>}
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