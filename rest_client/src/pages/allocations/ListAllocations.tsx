import {useEffect, useMemo, useState, useTransition} from "react";
import type {AllocationType} from "../../model/AllocationTypes.ts";
import {allocationApi} from "../../api/AllocationRestApi.ts";
import {type Column, GenericTable} from "../../components/GenericTable.tsx";
import {Button} from "react-bootstrap";
import useModal from "../../components/modals/useModal.tsx";
import useToast from "../../components/toasts/useToast.tsx";
import {formatDate} from "../../utils";

export default function ListAllocations() {
    const [allocations, setAllocations] = useState<AllocationType[]>([])
    const [isPending, startTransition] = useTransition()
    const {showConfirmation} = useModal()
    const {addToast} = useToast()

    const handleFinish = (allocation: AllocationType) => {
        showConfirmation({
            title: 'Finishing allocation',
            message: 'Are you sure you want to finish this allocation?',
            variant: 'warning',
            cancelLabel: 'No',
            confirmLabel: 'Yes',

            onConfirm: () => {
                allocationApi.finish(allocation.id)
                    .then(() =>
                        addToast('Success!', `Allocation #${allocation.id} has been finished`, "success"))
                    .catch((err) =>
                        addToast("Error!", `Error while finishing allocation. Error: ${err}`, "danger"))

            }
        })
    }

    // eslint-disable-next-line react-hooks/preserve-manual-memoization
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
        },
        {
            header: 'Finish Allocation',
            render: (a) => (
                <Button variant={'info'} onClick={()=>handleFinish(a)} disabled={a.endTime !== null}>Finish</Button>
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
    }, [addToast])

    return (
        <div className="container">
            <h2>Allocation List</h2>
            {isPending ? <p>Fetching data...</p> : <GenericTable data={allocations} columns={columns}/>}
        </div>
    );
}