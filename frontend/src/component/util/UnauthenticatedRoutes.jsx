import {Navigate, Outlet} from "react-router-dom";

const UnauthenticatedRoutes = () => {
    let auth = {'token':localStorage.getItem('token')}
    return (
        !auth.token ? <Outlet/> : <Navigate to={'/'}/>
    )
}

export default UnauthenticatedRoutes;