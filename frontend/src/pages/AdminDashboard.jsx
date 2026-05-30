import DashboardLayout from '../components/DashboardLayout.jsx';

const metrics = [
  { label: 'Users', value: '1,284', change: '+42 this month' },
  { label: 'Departments', value: '16', change: '4 regions' },
  { label: 'Automations', value: '37', change: '+6 enabled' },
  { label: 'Audit Events', value: '8.9k', change: 'Healthy volume' },
];

const sections = [
  {
    title: 'Platform Governance',
    description: 'Administrative controls and compliance placeholders.',
    tag: 'Admin',
    items: ['Role permissions', 'Data retention', 'Audit policy'],
  },
  {
    title: 'System Health',
    description: 'Operational surface for future service monitoring.',
    tag: 'Ops',
    items: ['Backend API', 'AI service', 'Database cluster'],
  },
  {
    title: 'Organization Setup',
    description: 'Tenant, team, and workflow configuration areas.',
    tag: 'Config',
    items: ['Business units', 'Approval chains', 'Hiring stages'],
  },
];

function AdminDashboard() {
  return (
    <DashboardLayout
      title="Admin Dashboard"
      subtitle="A governance workspace for platform administrators managing users, permissions, automations, and system health."
      metrics={metrics}
      sections={sections}
      actions={['Manage Users', 'View Audit Log']}
    />
  );
}

export default AdminDashboard;
