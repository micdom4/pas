import {type ReactNode} from 'react';
import {Table} from 'react-bootstrap';
import type {BaseType} from "../model/BaseType.ts";

export interface Column<T> {
    header: string;
    render: (item: T) => ReactNode;
}

interface GenericTableProps<T extends BaseType> {
    data: T[];
    columns: Column<T>[];
}

export const GenericTable = <T extends BaseType>({data, columns}: GenericTableProps<T>) => {
    if (!data || data.length === 0) {
        return <div className="p-3 text-center text-muted">No data to display.</div>;
    }

    return (
        <Table striped bordered hover responsive className="mt-3 shadow-sm">
            <thead className="bg-light">
                <tr>
                    {columns.map((col, index) => (
                        <th key={index}>{col.header}</th>
                    ))}
                </tr>
            </thead>
            <tbody>
                {data.map((item) => (
                    <tr key={item.id}>
                        {columns.map((col, index) => (
                            <td key={index} style={{verticalAlign: 'middle'}}>
                                {col.render(item)}
                            </td>
                        ))}
                    </tr>
                ))}
            </tbody>
        </Table>
    );
};