import { useEffect, useMemo, useState } from 'react';
import { api } from '../services/api.js';

const statusOrder = ['SUBMITTED', 'SCREENING', 'SHORTLISTED', 'INTERVIEW', 'OFFERED', 'HIRED'];

const statusLabels = {
  SUBMITTED: 'Submitted',
  SCREENING: 'Screening',
  SHORTLISTED: 'Shortlisted',
  INTERVIEW: 'Interview',
  OFFERED: 'Offered',
  HIRED: 'Hired',
  REJECTED: 'Rejected',
  WITHDRAWN: 'Withdrawn',
};

const demoRankings = [
  {
    applicationId: 'demo-1',
    candidateId: 101,
    candidateName: 'Aarav Mehta',
    candidateEmail: 'aarav.mehta@example.com',
    status: 'SHORTLISTED',
    score: 94.2,
    appliedAt: '2026-05-18T10:30:00Z',
  },
  {
    applicationId: 'demo-2',
    candidateId: 102,
    candidateName: 'Maya Srinivasan',
    candidateEmail: 'maya.s@example.com',
    status: 'INTERVIEW',
    score: 88.6,
    appliedAt: '2026-05-17T14:15:00Z',
  },
  {
    applicationId: 'demo-3',
    candidateId: 103,
    candidateName: 'Kabir Rao',
    candidateEmail: 'kabir.rao@example.com',
    status: 'SCREENING',
    score: 81.9,
    appliedAt: '2026-05-16T09:45:00Z',
  },
  {
    applicationId: 'demo-4',
    candidateId: 104,
    candidateName: 'Nisha Kapoor',
    candidateEmail: 'nisha.k@example.com',
    status: 'SUBMITTED',
    score: 73.4,
    appliedAt: '2026-05-15T12:05:00Z',
  },
];

function RecruiterDashboard() {
  const [jobs, setJobs] = useState([]);
  const [jobId, setJobId] = useState('');
  const [rankings, setRankings] = useState([]);
  const [status, setStatus] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [useDemoData, setUseDemoData] = useState(false);

  useEffect(() => {
    api.get('/jobs')
      .then(({ data }) => {
        setJobs(data);
        if (data.length > 0) setJobId(String(data[0].id));
      })
      .catch(() => {
        setUseDemoData(true);
        setRankings(demoRankings);
        setStatus('Backend data is unavailable, showing preview analytics.');
      });
  }, []);

  const visibleRankings = useDemoData ? demoRankings : rankings;

  const analytics = useMemo(() => {
    const total = visibleRankings.length;
    const averageScore = total
      ? visibleRankings.reduce((sum, candidate) => sum + Number(candidate.score || 0), 0) / total
      : 0;
    const shortlisted = visibleRankings.filter((candidate) => ['SHORTLISTED', 'INTERVIEW', 'OFFERED', 'HIRED'].includes(candidate.status)).length;
    const interviews = visibleRankings.filter((candidate) => candidate.status === 'INTERVIEW').length;

    return [
      { label: 'Applicants', value: total, detail: 'Matched candidates' },
      { label: 'Avg. Match', value: `${averageScore.toFixed(1)}%`, detail: 'Across ranked pool' },
      { label: 'Shortlist Rate', value: total ? `${Math.round((shortlisted / total) * 100)}%` : '0%', detail: 'Ready for review' },
      { label: 'Interviews', value: interviews, detail: 'Currently scheduled' },
    ];
  }, [visibleRankings]);

  const funnel = useMemo(() => {
    const source = visibleRankings.length ? visibleRankings : [];
    return statusOrder.map((statusName) => ({
      status: statusName,
      label: statusLabels[statusName],
      count: source.filter((candidate) => candidate.status === statusName).length,
    }));
  }, [visibleRankings]);

  const statusGroups = useMemo(() => {
    return visibleRankings.reduce((groups, candidate) => {
      const group = candidate.status || 'SUBMITTED';
      return {
        ...groups,
        [group]: [...(groups[group] || []), candidate],
      };
    }, {});
  }, [visibleRankings]);

  async function loadRankings(event) {
    event.preventDefault();
    if (!jobId) {
      setStatus('Choose a job first.');
      return;
    }

    setIsLoading(true);
    setStatus('');
    setUseDemoData(false);

    try {
      const { data } = await api.get(`/recruiter/jobs/${jobId}/rankings`);
      setRankings(data);
      setStatus(data.length ? 'Candidate ranking loaded.' : 'No applications have been matched for this job yet.');
    } catch (error) {
      setStatus(error.response?.data?.message || error.response?.data?.error || 'Unable to load rankings.');
    } finally {
      setIsLoading(false);
    }
  }

  return (
    <main className="min-h-screen bg-slate-50">
      <section className="mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
        <div className="flex flex-col gap-5 border-b border-slate-200 pb-8 lg:flex-row lg:items-end lg:justify-between">
          <div>
            <p className="eyebrow">Recruiter Dashboard</p>
            <h1 className="mt-3 text-3xl font-bold tracking-tight sm:text-4xl">
              Hiring Command Center
            </h1>
            <p className="mt-3 max-w-3xl text-base leading-7 text-slate-600">
              Review AI-ranked candidates, monitor funnel movement, inspect hiring analytics, and track application status for each open role.
            </p>
          </div>

          <form className="flex w-full flex-col gap-3 rounded-lg border border-slate-200 bg-white p-4 shadow-sm lg:max-w-xl lg:flex-row lg:items-end" onSubmit={loadRankings}>
            <label className="form-field flex-1">
              <span>Open role</span>
              <select value={jobId} onChange={(event) => setJobId(event.target.value)} required>
                {jobs.length === 0 && <option value="">No jobs loaded</option>}
                {jobs.map((job) => (
                  <option key={job.id} value={job.id}>
                    {job.title} {job.location ? `- ${job.location}` : ''}
                  </option>
                ))}
              </select>
            </label>
            <button className="rounded-lg bg-cyan-600 px-5 py-3 text-sm font-bold text-white transition hover:bg-cyan-700 disabled:cursor-not-allowed disabled:bg-slate-300" type="submit" disabled={isLoading}>
              {isLoading ? 'Loading...' : 'Refresh'}
            </button>
          </form>
        </div>

        {status && (
          <p className="mt-5 rounded-lg border border-slate-200 bg-white px-4 py-3 text-sm font-semibold text-slate-700">
            {status}
          </p>
        )}

        <section className="mt-8 grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
          {analytics.map((item) => (
            <article key={item.label} className="rounded-lg border border-slate-200 bg-white p-5 shadow-sm">
              <p className="text-sm font-semibold text-slate-500">{item.label}</p>
              <p className="mt-3 text-3xl font-black tracking-tight text-slate-950">{item.value}</p>
              <p className="mt-2 text-sm font-medium text-slate-500">{item.detail}</p>
            </article>
          ))}
        </section>

        <section className="mt-6 grid gap-6 xl:grid-cols-[1.15fr_0.85fr]">
          <CandidateRanking rankings={visibleRankings} />
          <HiringFunnel funnel={funnel} total={visibleRankings.length} />
        </section>

        <section className="mt-6 grid gap-6 xl:grid-cols-[0.9fr_1.1fr]">
          <AnalyticsPanel rankings={visibleRankings} />
          <ApplicationStatusBoard statusGroups={statusGroups} />
        </section>
      </section>
    </main>
  );
}

function CandidateRanking({ rankings }) {
  return (
    <section className="rounded-lg border border-slate-200 bg-white shadow-sm">
      <div className="flex flex-col gap-2 border-b border-slate-200 px-5 py-4 sm:flex-row sm:items-center sm:justify-between">
        <div>
          <h2 className="text-lg font-bold">Candidate Ranking</h2>
          <p className="text-sm text-slate-500">Sorted by AI match score</p>
        </div>
        <span className="rounded-md bg-cyan-50 px-3 py-1 text-sm font-bold text-cyan-800">
          {rankings.length} candidates
        </span>
      </div>

      <div className="divide-y divide-slate-100">
        {rankings.map((candidate, index) => (
          <article key={candidate.applicationId} className="grid gap-4 px-5 py-4 md:grid-cols-[56px_1fr_120px_130px] md:items-center">
            <span className="flex h-11 w-11 items-center justify-center rounded-lg bg-slate-100 text-sm font-black text-slate-500">
              #{index + 1}
            </span>
            <div>
              <h3 className="font-bold text-slate-950">{candidate.candidateName}</h3>
              <p className="text-sm text-slate-500">{candidate.candidateEmail}</p>
            </div>
            <ScoreMeter score={Number(candidate.score || 0)} />
            <StatusPill status={candidate.status} />
          </article>
        ))}

        {rankings.length === 0 && (
          <div className="px-5 py-12 text-center text-sm text-slate-500">
            No ranked candidates yet.
          </div>
        )}
      </div>
    </section>
  );
}

function HiringFunnel({ funnel, total }) {
  const maxCount = Math.max(...funnel.map((stage) => stage.count), 1);

  return (
    <section className="rounded-lg border border-slate-200 bg-white p-5 shadow-sm">
      <div>
        <h2 className="text-lg font-bold">Hiring Funnel</h2>
        <p className="mt-1 text-sm text-slate-500">Stage distribution for this role</p>
      </div>

      <div className="mt-6 space-y-4">
        {funnel.map((stage) => {
          const width = `${Math.max((stage.count / maxCount) * 100, stage.count ? 16 : 4)}%`;
          return (
            <div key={stage.status}>
              <div className="mb-2 flex items-center justify-between text-sm">
                <span className="font-bold text-slate-700">{stage.label}</span>
                <span className="text-slate-500">{stage.count}</span>
              </div>
              <div className="h-3 overflow-hidden rounded-full bg-slate-100">
                <div className="h-full rounded-full bg-cyan-600" style={{ width }} />
              </div>
            </div>
          );
        })}
      </div>

      <div className="mt-6 rounded-lg bg-slate-950 p-4 text-white">
        <p className="text-sm font-semibold text-slate-300">Total pipeline</p>
        <p className="mt-2 text-3xl font-black">{total}</p>
      </div>
    </section>
  );
}

function AnalyticsPanel({ rankings }) {
  const topScore = rankings[0]?.score || 0;
  const lowScoreCount = rankings.filter((candidate) => Number(candidate.score || 0) < 75).length;
  const activeCount = rankings.filter((candidate) => !['REJECTED', 'WITHDRAWN'].includes(candidate.status)).length;

  return (
    <section className="rounded-lg border border-slate-200 bg-white p-5 shadow-sm">
      <h2 className="text-lg font-bold">Analytics</h2>
      <p className="mt-1 text-sm text-slate-500">Fast hiring signals for recruiter review</p>

      <div className="mt-6 space-y-4">
        <Insight label="Top candidate match" value={`${Number(topScore).toFixed(1)}%`} tone="strong" />
        <Insight label="Active applications" value={activeCount} />
        <Insight label="Needs review below 75%" value={lowScoreCount} />
      </div>

      <div className="mt-6 grid grid-cols-6 items-end gap-2 rounded-lg bg-slate-50 p-4">
        {(rankings.length ? rankings : demoRankings).slice(0, 6).map((candidate) => (
          <div key={candidate.applicationId} className="flex flex-col items-center gap-2">
            <div className="w-full rounded-t-md bg-cyan-600" style={{ height: `${Math.max(Number(candidate.score || 0), 12)}px` }} />
            <span className="text-xs font-bold text-slate-500">{Math.round(Number(candidate.score || 0))}</span>
          </div>
        ))}
      </div>
    </section>
  );
}

function ApplicationStatusBoard({ statusGroups }) {
  const columns = ['SUBMITTED', 'SCREENING', 'SHORTLISTED', 'INTERVIEW'];

  return (
    <section className="rounded-lg border border-slate-200 bg-white p-5 shadow-sm">
      <div className="flex flex-col gap-2 sm:flex-row sm:items-center sm:justify-between">
        <div>
          <h2 className="text-lg font-bold">Application Status</h2>
          <p className="text-sm text-slate-500">Candidates grouped by current workflow stage</p>
        </div>
      </div>

      <div className="mt-6 grid gap-4 md:grid-cols-2 xl:grid-cols-4">
        {columns.map((status) => (
          <div key={status} className="rounded-lg border border-slate-200 bg-slate-50 p-4">
            <div className="flex items-center justify-between">
              <h3 className="text-sm font-black text-slate-700">{statusLabels[status]}</h3>
              <span className="rounded-md bg-white px-2 py-1 text-xs font-bold text-slate-500">
                {(statusGroups[status] || []).length}
              </span>
            </div>
            <div className="mt-4 space-y-3">
              {(statusGroups[status] || []).map((candidate) => (
                <div key={candidate.applicationId} className="rounded-lg border border-slate-200 bg-white p-3">
                  <p className="text-sm font-bold text-slate-950">{candidate.candidateName}</p>
                  <p className="mt-1 text-xs font-semibold text-cyan-700">{Number(candidate.score || 0).toFixed(1)}% match</p>
                </div>
              ))}
              {(statusGroups[status] || []).length === 0 && (
                <p className="rounded-lg border border-dashed border-slate-200 bg-white p-3 text-xs font-medium text-slate-400">
                  No candidates
                </p>
              )}
            </div>
          </div>
        ))}
      </div>
    </section>
  );
}

function ScoreMeter({ score }) {
  return (
    <div>
      <div className="flex items-center justify-between text-sm">
        <span className="font-black text-cyan-700">{score.toFixed(1)}%</span>
        <span className="font-medium text-slate-400">match</span>
      </div>
      <div className="mt-2 h-2 overflow-hidden rounded-full bg-slate-100">
        <div className="h-full rounded-full bg-cyan-600" style={{ width: `${Math.min(score, 100)}%` }} />
      </div>
    </div>
  );
}

function StatusPill({ status }) {
  return (
    <span className="inline-flex w-fit rounded-md bg-slate-100 px-3 py-1.5 text-xs font-black text-slate-700">
      {statusLabels[status] || status}
    </span>
  );
}

function Insight({ label, value, tone }) {
  return (
    <div className="flex items-center justify-between rounded-lg border border-slate-200 px-4 py-3">
      <span className="text-sm font-semibold text-slate-500">{label}</span>
      <span className={tone === 'strong' ? 'text-xl font-black text-cyan-700' : 'text-xl font-black text-slate-950'}>
        {value}
      </span>
    </div>
  );
}

export default RecruiterDashboard;
