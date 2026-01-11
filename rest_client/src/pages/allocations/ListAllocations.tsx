import {useEffect, useMemo, useState, useTransition} from "react";
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
    const [isPending, startTransition] = useTransition()

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
                a.endTime ? formatDate(a.endTime) : <span className="text-success fw-bold">W trakcie</span>
            )
        }
    ], []);

    const loadAllocations = () => {
        startTransition(() => {
            allocationApi.getAll().then((response) => {
                setAllocations(response.data)
            })
        })
    }

    useEffect(() => {
        loadAllocations()
    }, [])

    return (
        <div className="container">
            <h2>Allocation List</h2>
            {isPending ? <p>Fetching data...</p> : <GenericTable data={allocations} columns={columns}/>}
        </div>
    );
}