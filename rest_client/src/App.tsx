import './App.css'
import RoutesComponent from "./routes";
import {HashRouter} from "react-router-dom";
import {ToastProvider} from "./components/toasts/ToastProvider.tsx";
import {ModalProvider} from "./components/modals/ModalProvider.tsx";


function App() {

    return (
        <>
            <ToastProvider>
                <ModalProvider>
                    <HashRouter>
                        <RoutesComponent/>
                    </HashRouter>
                </ModalProvider>
            </ToastProvider>
        </>
    )
}

export default App
