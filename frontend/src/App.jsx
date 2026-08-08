import { useEffect, useRef, useState } from 'react'
import SockJS from 'sockjs-client'
import { Client } from '@stomp/stompjs'
import { fetchRecentReadings, API_BASE } from './api'
import PowerChart from './components/PowerChart'
import StatCard from './components/StatCard'

export default function App() {
  // readings: the full history we plot on the chart
  const [readings, setReadings] = useState([])
  // connected: whether the live WebSocket link is currently active
  const [connected, setConnected] = useState(false)
  const clientRef = useRef(null)

  // Effect #1: runs once when the page loads.
  // Grabs recent history via a normal REST call, so the chart isn't
  // empty while we wait for the next live reading to arrive.
  useEffect(() => {
    fetchRecentReadings()
      .then(data => setReadings(data.reverse())) // oldest -> newest for the chart
      .catch(err => console.error('Failed to load history:', err))
  }, [])

  // Effect #2: opens the live WebSocket connection.
  // Whenever the backend broadcasts a new reading (see ReadingController's
  // messagingTemplate.convertAndSend call), it lands here instantly and
  // we append it to state, which re-renders the chart automatically.
  useEffect(() => {
    const client = new Client({
      webSocketFactory: () => new SockJS(`${API_BASE}/ws`),
      reconnectDelay: 5000,
      onConnect: () => {
        setConnected(true)
        client.subscribe('/topic/readings', (message) => {
          const newReading = JSON.parse(message.body)
          setReadings(prev => [...prev.slice(-49), newReading]) // keep last 50 points
        })
      },
      onDisconnect: () => setConnected(false),
    })

    client.activate()
    clientRef.current = client

    // Cleanup: close the connection if the component unmounts
    return () => client.deactivate()
  }, [])

  const latest = readings[readings.length - 1]

  return (
    <div className="app">
      <header className="header">
        <h1>⚡ Energy Monitor</h1>
        <span className={`status-badge ${connected ? 'live' : 'offline'}`}>
          {connected ? '● Live' : '○ Connecting...'}
        </span>
      </header>

      <div className="stats-row">
        <StatCard label="Voltage" value={latest?.voltage?.toFixed(1)} unit="V" />
        <StatCard label="Current" value={latest?.current?.toFixed(2)} unit="A" />
        <StatCard label="Power" value={latest?.powerWatts?.toFixed(1)} unit="W" />
      </div>

      <PowerChart readings={readings} />

      {readings.length === 0 && (
        <p className="empty-state">
          No readings yet. Once your ESP32 starts posting data, it'll show up here live.
        </p>
      )}
    </div>
  )
}
