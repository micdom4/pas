import {useEffect, useMemo, useState} from 'react';
import {useParams, useNavigate} from 'react-router-dom';
import {Card, Button, Spinner, Badge, ListGroup, Row, Col} from 'react-bootstrap';
import {userApi} from '../../api/UserRestApi.ts';
import type {UserType} from '../../model/UserTypes.ts';
import useToast from '../../components/toasts/useToast.tsx';
import type {AllocationType} from "../../model/AllocationTypes.ts";
import {type Column, GenericTable} from "../../components/GenericTable.tsx";
import {formatDate} from "../../utils";
import {allocationApi} from "../../api/AllocationRestApi.ts";

export default function DetailedUser() {
    const {login} = useParams<{ login: string }>();
    const navigate = useNavigate();
    const {addToast} = useToast();

    const [user, setUser] = useState<UserType>();
    const [loading, setLoading] = useState(true);

    const [pastAllocations, setPastAllocations] = useState<AllocationType[]>()
    const [activeAllocations, setActiveAllocations] = useState<AllocationType[]>()

    const columns: Column<AllocationType>[] = useMemo(() => [
        {
            header: 'Client',
            render: (a) => `${a.client.login}, ${a.client.name} ${a.client.surname}`
        },
        {
            header: 'Virtual Machine ID',
            render: (a) => <span className="text-secondary">#{a.id}</span>
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
        }
    ], []);

    useEffect(() => {
        const loadData = async () => {
            if (!login) return;
            setLoading(true);

            try {
                const userResponse = await userApi.getByLogin(login);
                const fetchedUser = userResponse.data;

                setUser(fetchedUser);

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

        loadData();
    }, [login, navigate, addToast]);

    if (loading || !user) {
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
                &larr; Wróć do listy
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
                                {user.login.charAt(0)}
                                {user.login.charAt(1)}
                            </div>
                            <h3 className="mt-3">{user.login}</h3>
                            <Badge bg={user.active ? 'success' : 'secondary'}>
                                {user.active ? 'Active' : 'Not Active'}
                            </Badge>
                        </div>

                        <div className="col-md-8">
                            <ListGroup variant="flush">
                                <ListGroup.Item>
                                    <strong>ID:</strong> <span className="text-muted">{user.id}</span>
                                </ListGroup.Item>
                                <ListGroup.Item>
                                    <strong>Name:</strong> {user.name}
                                </ListGroup.Item>
                                <ListGroup.Item>
                                    <strong>Surname:</strong> {user.surname}
                                </ListGroup.Item>
                                <ListGroup.Item>
                                    <strong>Role:</strong> {user.type}
                                </ListGroup.Item>
                            </ListGroup>
                        </div>
                    </div>
                    <Row>
                        <Col>
                            <h3>Previous Allocations</h3>
                            <GenericTable data={pastAllocations || []} columns={columns}/>
                        </Col>
                        <Col>
                            <h3>Current Allocations</h3>
                            <GenericTable data={activeAllocations || []} columns={columns}/>
                        </Col>
                    </Row>
                </Card.Body>
            </Card>
        </div>
    );
};