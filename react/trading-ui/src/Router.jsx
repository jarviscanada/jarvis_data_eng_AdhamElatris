import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Dashboard from './page/Dashboard/Dashboard';
import QuotePage from './page/QuotePage/QuotePage';

export default function Router() {
        return (
            <BrowserRouter>
                <Routes>
                    <Route exact path="/" element={<Dashboard />} />
                    <Route exact path="/dashboard" element={<Dashboard />} />
                    <Route path="/quotes" element={<QuotePage />} />

               </Routes>
            </BrowserRouter>
        )
    }