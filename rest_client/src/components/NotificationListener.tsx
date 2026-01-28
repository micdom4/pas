import {useEffect, useRef} from 'react';
import {fetchEventSource} from '@microsoft/fetch-event-source';
import useToast from "./toasts/useToast.tsx";
import {API_URL} from "../api/api.config.ts";

interface NotificationListenerProps {
    token: string | null;
    userId: string | null;
}

export const NotificationListener = ({token, userId}: NotificationListenerProps) => {
    const controllerRef = useRef<AbortController | null>(null);
    const {addToast} = useToast()

    useEffect(() => {
        if (!token || !userId) return;

        const connectToSse = async () => {
            if (controllerRef.current) {
                controllerRef.current.abort();
            }
            controllerRef.current = new AbortController();

            const url = `${API_URL}/notifications/subscribe/${userId}`;

            await fetchEventSource(url, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Accept': 'text/event-stream',
                },
                openWhenHidden: true,
                signal: controllerRef.current.signal,

                async onopen(response) {
                    if (response.ok && response.headers.get('content-type')?.includes('text/event-stream')) {
                        console.log("SSE Connected for user:", userId);
                        return;
                    }
                    if (response.status === 403) {
                        addToast("Unable to receive notifications", "Please log in again", 'warning')
                    }
                },

                onmessage(msg) {
                    if (msg.event === 'INIT') return;
                    if (msg.event === 'USER_NOTIFICATION' || msg.event === 'USER_BLOCK_EVENT') {
                        addToast("New Notification", msg.data, 'info')
                    }
                    if (msg.event === "USER_ACTIVATED") {
                        addToast("Account Activated", "Your account has just been activated", 'success')
                    }
                    if (msg.event === "USER_DEACTIVATED") {
                        addToast("Account Deactivated", "Your account has just been deactivated!", 'danger')
                    }
                },

                onerror(err) {
                    if (err.message === "Forbidden") {
                        throw err;
                    }
                }
            });
        };

        connectToSse();

        return () => {
            if (controllerRef.current) {
                controllerRef.current.abort();
            }
        };
    }, [token, userId]);

    return null;
};