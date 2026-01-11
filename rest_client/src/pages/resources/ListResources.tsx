import type {ResourceType} from "../../model/ResourceTypes.ts";
import {useEffect, useMemo, useState, useTransition} from "react";
import {resourceApi} from "../../api/ResourceRestApi.ts";
import {type Column, GenericTable} from "../../components/GenericTable.tsx";

export default function ListResources() {
    const [resources, setResources] = useState<ResourceType[]>([])
    const [isPending, startTransition] = useTransition()

    const columns: Column<ResourceType>[] = useMemo(() => [
        {header: 'ID', render: (r) => <span className="text-secondary">#{r.id}</span>},
        {header: 'CPUs', render: (r) => r.cpuNumber},
        {header: 'RAM', render: (r) => `${r.ramGiB} GiB`},
        {header: 'Storage', render: (r) => `${r.storageGiB} GiB`},
    ], []);

    const loadResources = () => {
        startTransition(() => {
            resourceApi.getAll().then((response) => {
                setResources(response.data);
            })
        })
    }

    useEffect(() => {
        loadResources()
    }, [])

    return (
        <div className="container mt-4">
            <h2>Virtual Machines</h2>
            {isPending ? <p>Fetching data...</p> : <GenericTable data={resources} columns={columns}/>}
        </div>
    );
}