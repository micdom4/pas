import {useEffect, useMemo, useState, useTransition} from "react";
import type {UserType} from "../../model/UserTypes.ts";
import {userApi} from "../../api/UserRestApi.ts";
import {type Column, GenericTable} from "../../components/GenericTable.tsx";
import {Badge, CloseButton, Col, Form, InputGroup, Row} from "react-bootstrap";

export default function ListUsers() {
    const [users, setUsers] = useState<UserType[]>([]);
    const [isPending, startTransition] = useTransition();
    const [searchTerm, setSearchTerm] = useState('');

    const columns: Column<UserType>[] = useMemo(() => [
        {header: 'Login', render: (u) => <strong>{u.login}</strong>},
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
    }, [])

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
        </div>
    </>
}