import {useEffect, useMemo, useState} from "react";
import type {UserType} from "../../model/UserTypes.ts";
import {userApi} from "../../api/UserRestApi.ts";
import {type Column, GenericTable} from "../../components/GenericTable.tsx";
import {Badge} from "react-bootstrap";

export default function ListUsers() {
    const [users, setUsers] = useState<UserType[]>([])
    const [loading, setLoading] = useState(true)

    const columns: Column<UserType>[] = useMemo(() => [
        {header: 'Login', render: (u) => <strong>{u.login}</strong>},
        {header: 'Imię', render: (u) => u.name},
        {header: 'Nazwisko', render: (u) => u.surname},
        {header: 'Rola', render: (u) => u.type},
        {
            header: 'Status',
            render: (u) => (
                <Badge bg={u.active ? 'success' : 'secondary'}>
                    {u.active ? 'Aktywny' : 'Nieaktywny'}
                </Badge>
            )
        }
    ], []);

    useEffect(() => {
        userApi.getAll().then((response) => {
            setUsers(response.data);
            setLoading(false)
        })
    }, [])

    if (loading) {
        return <p>Ni ma</p>
    }

    return <>
        <h2>Lista użytkowników: </h2>
        <div >
            <GenericTable data={users} columns={columns}></GenericTable>
        </div>
    </>
}