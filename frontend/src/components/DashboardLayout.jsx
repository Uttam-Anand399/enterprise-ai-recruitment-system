function DashboardLayout({ title, subtitle, metrics, sections, actions }) {
  return (
    <main className="bg-slate-50">
      <section className="mx-auto w-full max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
        <div className="flex flex-col gap-5 border-b border-slate-200 pb-8 lg:flex-row lg:items-end lg:justify-between">
          <div>
            <p className="eyebrow">Dashboard</p>
            <h1 className="mt-3 text-3xl font-bold tracking-tight sm:text-4xl">{title}</h1>
            <p className="mt-3 max-w-3xl text-base leading-7 text-slate-600">{subtitle}</p>
          </div>
          <div className="flex flex-wrap gap-3">
            {actions.map((action) => (
              <button key={action} className="btn-secondary" type="button">
                {action}
              </button>
            ))}
          </div>
        </div>

        <div className="mt-8 grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
          {metrics.map((metric) => (
            <article key={metric.label} className="rounded-lg border border-slate-200 bg-white p-5 shadow-sm">
              <p className="text-sm font-semibold text-slate-500">{metric.label}</p>
              <p className="mt-3 text-3xl font-bold">{metric.value}</p>
              <p className="mt-2 text-sm text-emerald-700">{metric.change}</p>
            </article>
          ))}
        </div>

        <div className="mt-8 grid gap-6 lg:grid-cols-3">
          {sections.map((section) => (
            <article key={section.title} className="rounded-lg border border-slate-200 bg-white p-6 shadow-sm">
              <div className="flex items-start justify-between gap-4">
                <div>
                  <h2 className="text-lg font-bold">{section.title}</h2>
                  <p className="mt-1 text-sm text-slate-500">{section.description}</p>
                </div>
                <span className="rounded-md bg-slate-100 px-2.5 py-1 text-xs font-bold text-slate-600">
                  {section.tag}
                </span>
              </div>
              <div className="mt-6 space-y-3">
                {section.items.map((item) => (
                  <div key={item} className="flex items-center justify-between rounded-lg border border-slate-100 bg-slate-50 px-4 py-3 text-sm">
                    <span className="font-medium text-slate-700">{item}</span>
                    <span className="h-2 w-2 rounded-full bg-cyan-500" />
                  </div>
                ))}
              </div>
            </article>
          ))}
        </div>
      </section>
    </main>
  );
}

export default DashboardLayout;
