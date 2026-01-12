import {useEffect, useMemo, useState, useTransition} from "react";
import type {UserType} from "../../model/UserTypes.ts";
import {userApi} from "../../api/UserRestApi.ts";
import {type Column, GenericTable} from "../../components/GenericTable.tsx";
import {Badge, Button, ButtonGroup, CloseButton, Col, Dropdown, Form, InputGroup, Row} from "react-bootstrap";
import useToast from "../../components/toasts/useToast.tsx";
import {EditUserModal} from "../../components/modals/EditUserModal.tsx";
import {useNavigate} from "react-router-dom";
import {Paths} from "../../routes/paths.ts";

export default function ListUsers() {
    const [users, setUsers] = useState<UserType[]>([]);
    const [isPending, startTransition] = useTransition();
    const [searchTerm, setSearchTerm] = useState('');
    const {addToast} = useToast()

    const navigate = useNavigate()

    const [editingUser, setEditingUser] = useState<UserType | null>(null);
    const [showEditModal, setShowEditModal] = useState(false);

    const handleEdit = (user: UserType) => {
        setEditingUser(user);
        setShowEditModal(true);
    };

    const handleEditSuccess = () => {
        loadUsers();
    };

    const columns: Column<UserType>[] = useMemo(() => [
        {header: 'Login', render: (u) =>
                <strong>{u.login}</strong>},
        {header: 'Name', render: (u) => u.name},
        {header: 'Surname', render: (u) => u.surname},
        {header: 'Role', render: (u) => u.type},
        {
            header: 'Status',
            render: (u) => (
                <Badge bg={u.active ? 'success' : 'secondary'}>
                    {u.active ? 'Active' : 'Not Active'}
                </Badge>
            )
        },
        {
            header: 'Options',
            render: (u) => (
                <Dropdown as={ButtonGroup}>
                    <Button variant="primary" onClick={() =>
                        navigate(Paths.default.detailedUser.replace(":login",u.login))}>Details</Button>

                    <Dropdown.Toggle split variant="primary" id="dropdown-split-basic"/>

                    <Dropdown.Menu>
                        <Dropdown.Item onClick={() => {
                            userApi.activate(u.id).then(() =>
                                addToast('Success!', `User with login "${u.login}" has been activated`, 'success')
                            )
                        }}>Activate</Dropdown.Item>
                        <Dropdown.Item onClick={() => {
                            userApi.deactivate(u.id).then(() =>
                                addToast('Success!', `User with login "${u.login}" has been deactivated`, 'success')
                            )
                        }}>Deactivate</Dropdown.Item>
                        <Dropdown.Divider/>
                        <Dropdown.Item onClick={() => handleEdit(u)}>Edit</Dropdown.Item>
                    </Dropdown.Menu>
                </Dropdown>
            )
        }
    ], []);

    const loadUsers = () => {
        {
            userApi.getAll().then((response) => {
                setUsers(response.data);
            })
        }
    }

    function filterUsers(login: string) {
        setSearchTerm(login)
        if (!login || login === '') {
            loadUsers()
        } else {
            userApi.searchByLogin(login).then((r) => setUsers(r.data))
        }
    }

    useEffect(() => {
        startTransition(() => loadUsers())
    }, [addToast])

    return <>
        <h2>Users</h2>
        <div className={'mt-4'}>
            <Row className="mb-4">
                <Col md={16}>
                    <InputGroup>
                        <InputGroup.Text id="search-icon">🔍</InputGroup.Text>
                        <Form.Control
                            type="text"
                            placeholder="Search by login..."
                            value={searchTerm}
                            onChange={(e) => filterUsers(e.target.value)}
                        />
                        {searchTerm && (
                            <CloseButton
                                variant="outline-secondary"
                                onClick={() => filterUsers('')}
                            />
                        )}
                    </InputGroup>
                </Col>
            </Row>
            {isPending ? <p>Fetching data...</p> : <GenericTable data={users} columns={columns}></GenericTable>}

            <EditUserModal
                show={showEditModal}
                handleClose={() => setShowEditModal(false)}
                user={editingUser}
                onSuccess={handleEditSuccess}
            />
        </div>
    </>
}