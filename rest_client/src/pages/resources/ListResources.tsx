import type {ResourceType} from "../../model/ResourceTypes.ts";
import {useEffect, useMemo, useState} from "react";
import {resourceApi} from "../../api/ResourceRestApi.ts";
import {type Column, GenericTable} from "../../components/GenericTable.tsx";

export default function ListResources() {
    const [resources, setResources] = useState<ResourceType[]>([])
    const [loading, setLoading] = useState(true)

    const columns: Column<ResourceType>[] = useMemo(() => [
        { header: 'ID', render: (r) => <span className="text-secondary">#{r.id}</span> },
        { header: 'Ilość CPU', render: (r) => r.cpuNumber },
        { header: 'RAM', render: (r) => `${r.ramGiB} GiB` },
        { header: 'Pamięć trwała', render: (r) => `${r.storageGiB} GiB` },
    ], []);

    useEffect(() => {
        resourceApi.getAll().then((response) => {
            setResources(response.data);
            setLoading(false)
        })
    }, [])

    if (loading) {
        return <p>Ni ma</p>
    }

    return (
        <div className="container mt-4">
            <h2>Maszyny wirtualne</h2>
            <GenericTable data={resources} columns={columns} />
        </div>
    );
}