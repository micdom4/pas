import {useEffect, useState} from "react";
import type {UserType} from "../model/UserTypes.ts";
import {userApi} from "../api/UserRestApi.ts";

export default function Home() {
    const [users, setUsers] = useState<UserType[]>([])
    const [loading, setLoading] = useState(true)

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
        {users.map((u)=>{
            return <li>
                {u.login}, {u.name}, {u.surname}, {u.type}
            </li>
        })}
    </>
}