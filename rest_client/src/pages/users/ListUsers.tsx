import {useEffect, useMemo, useState, useTransition} from "react";
import type {UserType} from "../../model/UserTypes.ts";
import {userApi} from "../../api/UserRestApi.ts";
import {type Column, GenericTable} from "../../components/GenericTable.tsx";
import {Badge} from "react-bootstrap";

export default function ListUsers() {
    const [users, setUsers] = useState<UserType[]>([]);
    const [isPending, startTransition] = useTransition();

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
        startTransition(() => {
            userApi.getAll().then((response) => {
                setUsers(response.data);
            })
        })
    }

    useEffect(() => {
        loadUsers()
    }, [])

    return <>
        <h2>Users</h2>
        <div>
            {isPending ? <p>Fetching data...</p> : <GenericTable data={users} columns={columns}></GenericTable>}
        </div>
    </>
}