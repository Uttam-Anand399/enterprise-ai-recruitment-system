import { useEffect, useMemo, useState } from 'react';
import { api, getAuthUser } from '../services/api.js';

function CandidateDashboard() {
  const user = useMemo(() => getAuthUser(), []);
  const [jobs, setJobs] = useState([]);
  const [jobId, setJobId] = useState('');
  const [file, setFile] = useState(null);
  const [result, setResult] = useState(null);
  const [status, setStatus] = useState('');
  const [isUploading, setIsUploading] = useState(false);

  useEffect(() => {
    api.get('/jobs')
      .then(({ data }) => {
        setJobs(data);
        if (data.length > 0) setJobId(String(data[0].id));
      })
      .catch(() => setStatus('Login as a candidate to load jobs and upload a resume.'));
  }, []);

  async function uploadResume(event) {
    event.preventDefault();
    if (!file || !jobId) {
      setStatus('Choose a job and PDF resume first.');
      return;
    }

    const formData = new FormData();
    formData.append('jobId', jobId);
    formData.append('file', file);

    setIsUploading(true);
    setStatus('');
    setResult(null);

    try {
      const { data } = await api.post('/candidate/resumes/upload', formData);
      setResult(data);
      setStatus('Resume parsed and matched successfully.');
    } catch (error) {
      setStatus(error.response?.data?.message || error.response?.data?.error || 'Resume upload failed.');
    } finally {
      setIsUploading(false);
    }
  }

  return (
    <main className="bg-slate-50">
      <section className="mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
        <div className="border-b border-slate-200 pb-8">
          <p className="eyebrow">Candidate Dashboard</p>
          <h1 className="mt-3 text-3xl font-bold tracking-tight sm:text-4xl">
            Resume Upload and AI Match Score
          </h1>
          <p className="mt-3 max-w-3xl text-base leading-7 text-slate-600">
            Upload a PDF resume, send it through Spring Boot to the Flask AI service, and receive a match score for the selected job.
          </p>
        </div>

        <div className="mt-8 grid gap-6 lg:grid-cols-[0.95fr_1.05fr]">
          <form className="rounded-lg border border-slate-200 bg-white p-6 shadow-sm" onSubmit={uploadResume}>
            <h2 className="text-xl font-bold">Submit Resume</h2>
            <p className="mt-2 text-sm text-slate-500">
              Signed in as {user?.fullName || 'candidate'}
            </p>

            <div className="mt-6 space-y-5">
              <label className="form-field">
                <span>Job</span>
                <select value={jobId} onChange={(event) => setJobId(event.target.value)} required>
                  {jobs.length === 0 && <option value="">No jobs loaded</option>}
                  {jobs.map((job) => (
                    <option key={job.id} value={job.id}>
                      {job.title} {job.location ? `- ${job.location}` : ''}
                    </option>
                  ))}
                </select>
              </label>

              <label className="form-field">
                <span>Resume PDF</span>
                <input type="file" accept="application/pdf" onChange={(event) => setFile(event.target.files?.[0] || null)} required />
              </label>
            </div>

            <button className="mt-8 w-full rounded-lg bg-cyan-600 px-4 py-3 text-sm font-bold text-white transition hover:bg-cyan-700 disabled:cursor-not-allowed disabled:bg-slate-300" type="submit" disabled={isUploading}>
              {isUploading ? 'Parsing and matching...' : 'Upload and Match'}
            </button>

            {status && (
              <p className="mt-5 rounded-lg border border-slate-200 bg-slate-50 px-4 py-3 text-sm font-semibold text-slate-700">
                {status}
              </p>
            )}
          </form>

          <section className="rounded-lg border border-slate-200 bg-white p-6 shadow-sm">
            <h2 className="text-xl font-bold">Match Result</h2>
            {!result ? (
              <div className="mt-6 rounded-lg bg-slate-50 p-6 text-sm leading-6 text-slate-500">
                Your match score and extracted keyword comparison will appear here after upload.
              </div>
            ) : (
              <div className="mt-6 space-y-5">
                <div className="rounded-xl bg-slate-950 p-6 text-white">
                  <p className="text-sm font-bold uppercase tracking-wide text-cyan-300">{result.jobTitle}</p>
                  <p className="mt-4 text-5xl font-black">{result.score}%</p>
                  <p className="mt-2 text-sm text-slate-300">AI match score</p>
                </div>

                <KeywordList title="Common Keywords" keywords={result.commonKeywords} />
                <KeywordList title="Missing Keywords" keywords={result.missingKeywords} />
              </div>
            )}
          </section>
        </div>
      </section>
    </main>
  );
}

function KeywordList({ title, keywords }) {
  return (
    <div>
      <h3 className="text-sm font-bold uppercase tracking-wide text-slate-500">{title}</h3>
      <div className="mt-3 flex flex-wrap gap-2">
        {(keywords || []).length === 0 && <span className="text-sm text-slate-500">None returned</span>}
        {(keywords || []).map((keyword) => (
          <span key={keyword} className="rounded-md bg-cyan-50 px-3 py-1.5 text-sm font-semibold text-cyan-800">
            {keyword}
          </span>
        ))}
      </div>
    </div>
  );
}

export default CandidateDashboard;
