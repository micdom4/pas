import {useEffect, useMemo, useState} from "react";
import type {AllocationType} from "../../model/AllocationTypes.ts";
import {allocationApi} from "../../api/AllocationRestApi.ts";
import {type Column, GenericTable} from "../../components/GenericTable.tsx";

const formatDate = (date: Date | null) => {
    if (!date) return '-';
    return new Date(date).toLocaleDateString('pl-PL') + ' ' + new Date(date).toLocaleTimeString('pl-PL',
        {hour: '2-digit', minute: '2-digit'});
};

export default function ListAllocations() {
    const [allocations, setAllocations] = useState<AllocationType[]>([])
    const [loading, setLoading] = useState(true)

    const columns: Column<AllocationType>[] = useMemo(() => [
        {
            header: 'Klient',
            render: (a) => `${a.client.login}, ${a.client.name} ${a.client.surname}`
        },
        {
            header: 'ID Zasobu',
            render: (a) => <span className="text-secondary">#{a.id}</span>
        },
        {
            header: 'Rozpoczęcie',
            render: (a) => formatDate(a.startTime)
        },
        {
            header: 'Zakończenie',
            render: (a) => (
                a.endTime ? formatDate(a.endTime) : <span className="text-success fw-bold">W trakcie</span>
            )
        }
    ], []);

    useEffect(() => {
        allocationApi.getAll().then((response) => {
            setAllocations(response.data)
            setLoading(false)
        })
    }, [])

    if (loading) {
        return <p>Ni ma</p>
    }

    return (
        <div className="container">
            <h2>Lista Alokacji</h2>
            <GenericTable data={allocations} columns={columns}/>
        </div>
    );
}