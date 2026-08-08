import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts'

/**
 * Takes the readings array and plots power (watts) over time.
 * Recharts wants an array of plain objects -- we format the timestamp
 * into something readable for the X axis before rendering.
 */
export default function PowerChart({ readings }) {
  const data = readings.map(r => ({
    time: new Date(r.timestamp).toLocaleTimeString(),
    power: Number(r.powerWatts?.toFixed(1)),
  }))

  return (
    <div className="chart-card">
      <h3>Power Draw (W)</h3>
      <ResponsiveContainer width="100%" height={300}>
        <LineChart data={data}>
          <CartesianGrid strokeDasharray="3 3" stroke="#333" />
          <XAxis dataKey="time" stroke="#999" tick={{ fontSize: 11 }} />
          <YAxis stroke="#999" tick={{ fontSize: 11 }} />
          <Tooltip contentStyle={{ background: '#1a1a1a', border: '1px solid #444' }} />
          <Line type="monotone" dataKey="power" stroke="#e5a13d" strokeWidth={2} dot={false} />
        </LineChart>
      </ResponsiveContainer>
    </div>
  )
}
