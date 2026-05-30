import { Link } from 'react-router-dom';

const capabilities = [
  'AI-assisted screening',
  'Candidate pipeline visibility',
  'Recruiter workflow automation',
  'Admin governance controls',
];

function Home() {
  return (
    <main>
      <section className="bg-slate-950 text-white">
        <div className="mx-auto grid min-h-[calc(100vh-88px)] max-w-7xl items-center gap-10 px-4 py-12 sm:px-6 lg:grid-cols-[1.05fr_0.95fr] lg:px-8">
          <div>
            <p className="text-sm font-bold uppercase tracking-wide text-cyan-300">
              Enterprise AI Recruitment Automation Platform
            </p>
            <h1 className="mt-5 max-w-4xl text-4xl font-black tracking-tight sm:text-5xl lg:text-6xl">
              Orchestrate hiring from intake to decision with role-based workspaces.
            </h1>
            <p className="mt-6 max-w-2xl text-lg leading-8 text-slate-300">
              A responsive frontend skeleton for candidates, recruiters, and admins, ready for API integration when backend features arrive.
            </p>
            <div className="mt-8 flex flex-col gap-3 sm:flex-row">
              <Link className="btn-primary" to="/register">
                Start skeleton flow
              </Link>
              <Link className="rounded-lg border border-white/20 px-4 py-2.5 text-center text-sm font-bold text-white transition hover:bg-white/10" to="/login">
                View login
              </Link>
            </div>
          </div>

          <div className="rounded-2xl border border-white/10 bg-white/5 p-5 shadow-2xl shadow-cyan-950/30">
            <div className="grid gap-4 sm:grid-cols-2">
              {capabilities.map((capability) => (
                <div key={capability} className="rounded-xl bg-white p-5 text-slate-950">
                  <div className="h-10 w-10 rounded-lg bg-cyan-100" />
                  <h2 className="mt-5 text-base font-bold">{capability}</h2>
                  <p className="mt-2 text-sm leading-6 text-slate-500">
                    Static UI block reserved for future platform modules.
                  </p>
                </div>
              ))}
            </div>
          </div>
        </div>
      </section>
    </main>
  );
}

export default Home;
