// Same idea as api.js in your ticket booking frontend: one place that
// knows where the backend lives, so the rest of the app doesn't need
// to care whether you're running locally or hitting the deployed URL.
//
// While developing locally, leave this as localhost. Once you deploy
// the backend (see README), change this to your live Render/Railway URL,
// e.g. "https://energy-monitor-backend.onrender.com"
export const API_BASE = 'https://energy-monitor-cloud-dashboard.onrender.com'

export async function fetchRecentReadings() {
  const res = await fetch(`${API_BASE}/api/readings`)
  if (!res.ok) throw new Error('Failed to fetch readings')
  return res.json()
}
