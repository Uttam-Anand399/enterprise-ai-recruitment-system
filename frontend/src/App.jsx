import { BrowserRouter, Link, NavLink, Route, Routes } from 'react-router-dom';
import AdminDashboard from './pages/AdminDashboard.jsx';
import CandidateDashboard from './pages/CandidateDashboard.jsx';
import Home from './pages/Home.jsx';
import Login from './pages/Login.jsx';
import RecruiterDashboard from './pages/RecruiterDashboard.jsx';
import Register from './pages/Register.jsx';

const navItems = [
  { label: 'Home', path: '/' },
  { label: 'Candidate', path: '/candidate' },
  { label: 'Recruiter', path: '/recruiter' },
  { label: 'Admin', path: '/admin' },
];

function AppShell() {
  return (
    <div className="min-h-screen bg-slate-50 text-slate-950">
      <header className="sticky top-0 z-30 border-b border-slate-200 bg-white/95 backdrop-blur">
        <nav className="mx-auto flex max-w-7xl flex-col gap-4 px-4 py-4 sm:px-6 lg:flex-row lg:items-center lg:justify-between lg:px-8">
          <Link to="/" className="flex items-center gap-3">
            <span className="flex h-10 w-10 items-center justify-center rounded-lg bg-cyan-600 text-sm font-black text-white">
              AI
            </span>
            <span>
              <span className="block text-base font-bold">RecruitFlow</span>
              <span className="block text-xs font-medium text-slate-500">
                Enterprise Hiring Automation
              </span>
            </span>
          </Link>

          <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between lg:gap-6">
            <div className="flex flex-wrap gap-2">
              {navItems.map((item) => (
                <NavLink
                  key={item.path}
                  to={item.path}
                  className={({ isActive }) =>
                    [
                      'rounded-md px-3 py-2 text-sm font-semibold transition',
                      isActive
                        ? 'bg-slate-950 text-white'
                        : 'text-slate-600 hover:bg-slate-100 hover:text-slate-950',
                    ].join(' ')
                  }
                >
                  {item.label}
                </NavLink>
              ))}
            </div>

            <div className="flex gap-2">
              <Link className="btn-secondary" to="/login">
                Login
              </Link>
              <Link className="btn-primary" to="/register">
                Register
              </Link>
            </div>
          </div>
        </nav>
      </header>

      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/candidate" element={<CandidateDashboard />} />
        <Route path="/recruiter" element={<RecruiterDashboard />} />
        <Route path="/admin" element={<AdminDashboard />} />
      </Routes>
    </div>
  );
}

function App() {
  return (
    <BrowserRouter>
      <AppShell />
    </BrowserRouter>
  );
}

export default App;
