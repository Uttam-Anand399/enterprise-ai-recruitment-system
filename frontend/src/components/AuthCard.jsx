import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { api, storeAuth } from '../services/api.js';

function AuthCard({ title, subtitle, mode }) {
  const isRegister = mode === 'register';
  const navigate = useNavigate();
  const [form, setForm] = useState({
    fullName: '',
    email: '',
    password: '',
    role: 'CANDIDATE',
  });
  const [status, setStatus] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  function updateField(event) {
    setForm((current) => ({
      ...current,
      [event.target.name]: event.target.value,
    }));
  }

  function destinationForRole(role) {
    if (role === 'RECRUITER') return '/recruiter';
    if (role === 'ADMIN') return '/admin';
    return '/candidate';
  }

  async function submit(event) {
    event.preventDefault();
    setIsSubmitting(true);
    setStatus('');

    try {
      const payload = isRegister
        ? form
        : { email: form.email, password: form.password };
      const endpoint = isRegister ? '/auth/register' : '/auth/login';
      const { data } = await api.post(endpoint, payload);
      storeAuth(data);
      navigate(destinationForRole(data.user.role));
    } catch (error) {
      setStatus(error.response?.data?.message || error.response?.data?.error || 'Authentication failed');
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <main className="auth-page">
      <section className="mx-auto grid min-h-[calc(100vh-88px)] w-full max-w-7xl items-center gap-10 px-4 py-10 sm:px-6 lg:grid-cols-[1fr_480px] lg:px-8">
        <div className="max-w-2xl">
          <p className="eyebrow">Secure talent operations</p>
          <h1 className="mt-4 text-4xl font-bold tracking-tight text-slate-950 sm:text-5xl">
            {title}
          </h1>
          <p className="mt-5 text-lg leading-8 text-slate-600">{subtitle}</p>
          <div className="mt-8 grid gap-3 sm:grid-cols-3">
            {['JWT-ready auth', 'Role-based portals', 'Enterprise controls'].map((item) => (
              <div key={item} className="rounded-lg border border-slate-200 bg-white p-4 text-sm font-semibold text-slate-700 shadow-sm">
                {item}
              </div>
            ))}
          </div>
        </div>

        <form className="rounded-xl border border-slate-200 bg-white p-6 shadow-xl shadow-slate-200/60 sm:p-8" onSubmit={submit}>
          <div>
            <h2 className="text-2xl font-bold">{isRegister ? 'Create account' : 'Welcome back'}</h2>
            <p className="mt-2 text-sm text-slate-500">
              Connected to the Spring Boot JWT authentication API.
            </p>
          </div>

          <div className="mt-8 space-y-5">
            {isRegister && (
              <label className="form-field">
                <span>Full name</span>
                <input name="fullName" type="text" placeholder="Alex Morgan" value={form.fullName} onChange={updateField} required />
              </label>
            )}
            <label className="form-field">
              <span>Email address</span>
              <input name="email" type="email" placeholder="alex@company.com" value={form.email} onChange={updateField} required />
            </label>
            <label className="form-field">
              <span>Password</span>
              <input name="password" type="password" placeholder="Enter password" value={form.password} onChange={updateField} minLength={8} required />
            </label>
            {isRegister && (
              <label className="form-field">
                <span>Account type</span>
                <select name="role" value={form.role} onChange={updateField}>
                  <option value="CANDIDATE">Candidate</option>
                  <option value="RECRUITER">Recruiter</option>
                  <option value="ADMIN">Admin</option>
                </select>
              </label>
            )}
          </div>

          {status && (
            <p className="mt-5 rounded-lg border border-rose-200 bg-rose-50 px-4 py-3 text-sm font-semibold text-rose-700">
              {status}
            </p>
          )}

          <button className="mt-8 w-full rounded-lg bg-cyan-600 px-4 py-3 text-sm font-bold text-white transition hover:bg-cyan-700 disabled:cursor-not-allowed disabled:bg-slate-300" type="submit" disabled={isSubmitting}>
            {isSubmitting ? 'Please wait...' : isRegister ? 'Create account' : 'Sign in'}
          </button>

          <p className="mt-6 text-center text-sm text-slate-500">
            {isRegister ? 'Already have an account?' : 'Need an account?'}{' '}
            <Link className="font-bold text-cyan-700 hover:text-cyan-800" to={isRegister ? '/login' : '/register'}>
              {isRegister ? 'Login' : 'Register'}
            </Link>
          </p>
        </form>
      </section>
    </main>
  );
}

export default AuthCard;
