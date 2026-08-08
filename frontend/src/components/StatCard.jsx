// A single "latest value" tile -- e.g. Voltage: 231.2 V
export default function StatCard({ label, value, unit }) {
  return (
    <div className="stat-card">
      <span className="stat-label">{label}</span>
      <span className="stat-value">{value ?? '--'} <small>{unit}</small></span>
    </div>
  )
}
