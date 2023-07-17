import {BrowserRouter, Route, Routes} from "react-router-dom";
import Login from "./component/auth/Login";
import Home from "./component/Home";
import EditUser from "./component/user/EditUser";
import Signup from "./component/auth/Signup";
import Confirmation from "./component/auth/Confirmation";
import Token from "./component/auth/Token";
import DeleteUser from "./component/user/DeleteUser";
import PrivateRoutes from "./component/util/PrivateRoutes";
import NotFound from "./component/NotFound";
import ForgotPassword from "./component/auth/ForgotPassword";
import ResetPassword from "./component/auth/ResetPassword";
import UnauthenticatedRoutes from "./component/util/UnauthenticatedRoutes";
import OutgoingQuestions from "./component/questions/OutgoingQuestions";
import IncomingQuestions from "./component/questions/IncomingQuestions";

function App() {
    return (
        <div>
            <BrowserRouter>
                <Routes>
                    <Route element={<UnauthenticatedRoutes/>}>
                    <Route path={'/login'} element={<Login />} />
                        <Route path={'/forgot-password'} element={<ForgotPassword/>}/>
                        <Route path={'/signup'} element={<Signup/>}/>
                        <Route path={'/registrationConfirm/:token'} element={<Confirmation/>}/>
                        <Route path={'token/:token'} element={<Token/>}/>
                        <Route path={'/reset-password/:token'} element={<ResetPassword/>}/>
                    </Route>

                    <Route element={<PrivateRoutes/>}>
                        <Route path={'/'} element={<Home/>}/>
                        <Route path={'user/edit'} element={<EditUser/>}/>
                        <Route path={'user/delete'} element={<DeleteUser/>}/>
                        <Route path={'/questions/outgoing/'} element={<OutgoingQuestions/>}/>
                        <Route path={'/questions/incoming/'} element={<IncomingQuestions/>}/>
                    </Route>
                    <Route path={'*'} element={<NotFound/>}/>
                </Routes>
            </BrowserRouter>
        </div>
    );
}

export default App;