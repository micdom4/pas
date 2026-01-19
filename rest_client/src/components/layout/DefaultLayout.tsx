import {Container, Nav, Navbar, NavDropdown} from "react-bootstrap";
import {Paths} from "../../routes/paths.ts";
import {type ReactNode, use} from "react";
import {useNavigate} from "react-router-dom";
import LoggedUserContext from "../../contexts/LoggedUserContext";
import {emptyUser} from "../../contexts/LoggedUserContext/types.ts";
import {loginApi} from "../../api/LoginApi.ts";
import useToast from "../toasts/useToast.tsx";

interface LayoutProps {
    children: ReactNode
}

export default function DefaultLayout({children}: LayoutProps) {
    const navigate = useNavigate()
    const {user, setUser} = use(LoggedUserContext)
    const {addToast} = useToast()

    async function logOut() {
        await loginApi.logout()
            .then(() =>
                addToast('Logout successful',
                `Successfully logged out from account '${user.login}'`,
                    'success')
            )
            .catch((err) => {
                console.error(err)
                addToast('Error in logout',
                    `Error while trying to log out. Error: ${err}`,
                    'danger')
            })
        setUser(emptyUser)
    }

    return (
        <div>
            <Navbar bg={"primary"} fixed={"top"} expand="lg" data-bs-theme={"dark"}>
                <Container>
                    <Navbar.Brand onClick={() => navigate(Paths.default.home)}>polVirt</Navbar.Brand>
                    <Navbar.Collapse>
                        <Nav className="me-auto">
                            <Nav.Link onClick={() => navigate(Paths.default.home)}>Home</Nav.Link>

                            {user.isAuthenticated() ? <>
                                <NavDropdown title="Users" id="basic-nav-dropdown">
                                    <NavDropdown.Item onClick={() => navigate(Paths.default.listUsers)}>
                                        List Users
                                    </NavDropdown.Item>
                                    <NavDropdown.Item onClick={() => navigate(Paths.default.createUser)}>
                                        Create New User
                                    </NavDropdown.Item>
                                </NavDropdown>
                                <NavDropdown title="Resources" id="basic-nav-dropdown">
                                    <NavDropdown.Item onClick={() => navigate(Paths.default.listResources)}>
                                        List Resources
                                    </NavDropdown.Item>
                                    <NavDropdown.Item onClick={() => navigate(Paths.default.createResource)}>
                                        Create New Resource
                                    </NavDropdown.Item>
                                </NavDropdown>
                                <NavDropdown title="Allocations" id="basic-nav-dropdown">
                                    <NavDropdown.Item onClick={() => navigate(Paths.default.listAllocations)}>
                                        List Allocations
                                    </NavDropdown.Item>
                                    <NavDropdown.Item onClick={() => navigate(Paths.default.createAllocations)}>
                                        Create New Allocation
                                    </NavDropdown.Item>
                                </NavDropdown>
                                <Nav.Link onClick={logOut}>Logout</Nav.Link>
                            </> : <>
                                <Nav.Link onClick={() => navigate(Paths.anonymous.login)}>Login</Nav.Link>
                                <Nav.Link onClick={() => navigate(Paths.anonymous.register)}>Register</Nav.Link>
                            </>}
                        </Nav>
                    </Navbar.Collapse>
                </Container>
            </Navbar>
            <Container>
                {children}
            </Container>
        </div>
    );
}