import {use, useEffect, useMemo, useState} from 'react';
import {useParams, useNavigate} from 'react-router-dom';
import {Card, Button, Spinner, Badge, ListGroup, Row, Col} from 'react-bootstrap';
import {userApi} from '../../api/UserRestApi.ts';
import type {UserType} from '../../model/UserTypes.ts';
import useToast from '../../components/toasts/useToast.tsx';
import type {AllocationType} from "../../model/AllocationTypes.ts";
import {type Column, GenericTable} from "../../components/GenericTable.tsx";
import {formatDate} from "../../utils";
import {allocationApi} from "../../api/AllocationRestApi.ts";
import LoggedUserContext from "../../contexts/LoggedUserContext";
import {ChangePasswordModal} from "../../components/modals/ChangePasswordModal.tsx";
import {emptyUser} from "../../contexts/LoggedUserContext/types.ts";
import useModal from "../../components/modals/useModal.tsx";

export default function DetailedUser() {
    const {login} = useParams<{ login: string }>();
    const navigate = useNavigate();
    const {addToast} = useToast();
    const {user, setUser} = use(LoggedUserContext)

    const [showPasswordModal, setShowPasswordModal] = useState(false);

    const [detailedUser, setDetailedUser] = useState<UserType>();
    const [loading, setLoading] = useState(true);

    const [pastAllocations, setPastAllocations] = useState<AllocationType[]>()
    const [activeAllocations, setActiveAllocations] = useState<AllocationType[]>()
    const {showConfirmation} = useModal()

    const handleFinish = (allocation: AllocationType) => {
        showConfirmation({
            title: 'Finishing allocation',
            message: 'Are you sure you want to finish this allocation?',
            variant: 'warning',
            cancelLabel: 'No',
            confirmLabel: 'Yes',

            onConfirm: () => {
                allocationApi.finish(allocation.id)
                    .then(() =>
                        addToast('Success!', `Allocation #${allocation.id} has been finished`, "success"))
                    .catch((err) =>
                        addToast("Error!", `Error while finishing allocation. Error: ${err}`, "danger"))
                loadData()
            }
        })
    }


    const columns: Column<AllocationType>[] = useMemo(() => [
        {
            header: 'Client',
            render: (a) => `${a.client.login}, ${a.client.name} ${a.client.surname}`
        },
        {
            header: 'Virtual Machine ID',
            render: (a) => <span className="text-secondary">#{a.vm.id}</span>
        },
        {
            header: 'Start Time',
            render: (a) => formatDate(a.startTime)
        },
        {
            header: 'End Time',
            render: (a) => (
                a.endTime ? formatDate(a.endTime) : <span className="text-success fw-bold">In progress</span>
            )
        },
        {
            header: 'Finish Allocation',
            render: (a) => (
                <Button variant={'info'} onClick={() => handleFinish(a)} disabled={a.endTime !== null}>Finish</Button>
            )
        }
    ], []);

    const handleSuccess = () => {
        console.log("Password has been changed for user: ", detailedUser?.login)
        addToast('You have been logged out', 'You need to log in with your new password.', 'warning')
        setUser(emptyUser)
        navigate('/login')
    }

    const loadData = async () => {
        if (!login) return;
        setLoading(true);

        try {
            const userResponse = await userApi.getByLogin(login);
            const fetchedUser = userResponse.data;

            setDetailedUser(fetchedUser);

            if (fetchedUser.type.toString() === "CLIENT") {
                const [pastRes, activeRes] = await Promise.all([
                    allocationApi.getPastForClient(fetchedUser.id),
                    allocationApi.getActiveForClient(fetchedUser.id)
                ]);

                setPastAllocations(pastRes.data);
                setActiveAllocations(activeRes.data);
            }

        } catch (error) {
            console.error(error);
            addToast('Error', `Could not fetch data for login: "${login}"`, 'danger');
            navigate('/users');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadData();
    }, [login]);

    if (loading || !detailedUser) {
        return (
            <div className="text-center mt-5">
                <Spinner animation="border" variant="primary"/>
                <p>Loading data...</p>
            </div>
        );
    }

    return (
        <div className="container mt-4">
            <Button variant="outline-secondary" className="mb-3" onClick={() => navigate('/users')}>
                &larr; Go Back
            </Button>

            <Card className="shadow-sm">
                <Card.Header as="h5" className="bg-primary text-white">
                    User Details
                </Card.Header>
                <Card.Body>
                    <div className="row">
                        <div className="col-md-4 text-center mb-3">
                            <div
                                className="rounded-circle bg-light d-flex align-items-center justify-content-center mx-auto"
                                style={{width: '150px', height: '150px', fontSize: '3rem', border: '2px solid #ddd'}}
                            >
                                {detailedUser.login.charAt(0)}
                                {detailedUser.login.charAt(1)}
                            </div>
                            <h3 className="mt-3">{detailedUser.login}</h3>
                            <Badge bg={detailedUser.active ? 'success' : 'secondary'}>
                                {detailedUser.active ? 'Active' : 'Not Active'}
                            </Badge>
                        </div>

                        <div className="col-md-8">
                            <ListGroup variant="flush">
                                <ListGroup.Item>
                                    <strong>ID:</strong> <span className="text-muted">{detailedUser.id}</span>
                                </ListGroup.Item>
                                <ListGroup.Item>
                                    <strong>Name:</strong> {detailedUser.name}
                                </ListGroup.Item>
                                <ListGroup.Item>
                                    <strong>Surname:</strong> {detailedUser.surname}
                                </ListGroup.Item>
                                <ListGroup.Item>
                                    <strong>Role:</strong> {detailedUser.type}
                                </ListGroup.Item>
                            </ListGroup>
                            {user.login == detailedUser.login &&
                                <Button variant={'link'}
                                        onClick={() => setShowPasswordModal(true)}>
                                    Change Password
                                </Button>}
                        </div>
                    </div>
                    <Row>
                        <Col>
                            <h3>Previous Allocations</h3>
                            <GenericTable data={pastAllocations || []}
                                          columns={columns.filter(
                                              (c) => c.header != "Finish Allocation")}/>
                        </Col>
                        <Col>
                            <h3>Current Allocations</h3>
                            <GenericTable data={activeAllocations || []} columns={columns}/>
                        </Col>
                    </Row>
                </Card.Body>
            </Card>

            <ChangePasswordModal
                show={showPasswordModal}
                handleClose={() => setShowPasswordModal(false)}
                user={user}
                onSuccess={handleSuccess}
            />
        </div>
    );
};