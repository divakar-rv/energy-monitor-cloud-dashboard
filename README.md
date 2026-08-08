# ⚡ Energy Monitor — Cloud Dashboard

A live, cloud-connected dashboard for an ESP32 energy monitor. Extends
the existing **Smart Energy Monitoring System** project: instead of
pushing readings to Blynk, the ESP32 now posts directly to a custom
Spring Boot backend, which stores the data and streams it live to a
React dashboard over WebSockets.

```
ESP32 (SCT-013 + ZMPT101B sensors)
   |  HTTP POST every 5s  { voltage, current }
   v
Spring Boot backend  --------------------->  Database (H2 locally / Postgres in prod)
   |  broadcasts new reading over WebSocket
   v
React dashboard (live chart + current stats)
```

## Why this project exists

This ties two skill sets together: the embedded/sensor side (already
proven in the original energy monitor project) and a full cloud
backend + live frontend (same stack as the ticket booking project).
The result is a live, clickable demo — not just code sitting in a
repo — which is the one thing missing across the rest of the
portfolio.

---

## Part 1 — Run everything locally first

Get this fully working on your laptop before touching the ESP32 or
any deployment. That way, if something breaks later, you know whether
the problem is your code or your deployment config.

### 1. Start the backend

```
cd backend
mvn spring-boot:run
```

This uses an in-memory H2 database by default (see
`application.properties`) — no installation needed, data just resets
each time you restart. It runs on **port 8082**.

Once it's running, test it without the ESP32 at all — open a second
terminal and simulate a sensor sending data:

```
curl -X POST http://localhost:8082/api/readings ^
  -H "Content-Type: application/json" ^
  -d "{\"voltage\": 231.5, \"current\": 1.8}"
```

(On Mac/Linux, replace `^` line-continuations with `\` — or just put
it on one line.)

If it responds with a JSON object including a calculated `powerWatts`
field, the backend logic is working correctly.

### 2. Start the frontend

```
cd frontend
npm install
npm run dev
```

Open **http://localhost:5174**. You should see the dashboard with
"Connecting..." in the top right. Send another `curl` POST like
above — the moment you do, the "Connecting..." badge should flip to
"● Live" and a data point should appear on the chart, in real time,
with no page refresh. That's the WebSocket push working.

Keep sending a few more `curl` POSTs with different numbers and watch
the chart update live — this is worth recording as a short screen
capture for your README/portfolio even before the ESP32 is involved.

### 3. Connect the real ESP32

Open `esp32/energy_monitor_cloud.ino` in the Arduino IDE (same
workflow as your original project). Before uploading:

- Fill in your real `WIFI_SSID` and `WIFI_PASSWORD`.
- Set `SERVER_URL` to your **laptop's local IP** (not `localhost` —
  the ESP32 is a separate device on your WiFi network). Find it with
  `ipconfig` on Windows, look for "IPv4 Address".
- Replace the placeholder math in `readVoltage()` / `readCurrent()`
  with the real calibration formulas from your original smart energy
  monitoring project — those sensor-reading fundamentals don't change,
  only how the result gets sent onward.

Upload, open the Serial Monitor, and confirm it prints "Sent reading,
server responded: 200" every 5 seconds. Your dashboard should now be
plotting real sensor data live.

---

## Part 2 — Deploy it so it's a live, clickable link

This is the step that makes it stand out on a resume — a link a
recruiter can open directly, with real hardware behind it (or at
least real backend logic, even if the ESP32 itself stays on your desk
at home).

### Deploy the backend (Render, free tier)

1. Push this whole project to GitHub (same `git init` / `add` /
   `commit` / `push` flow you already know from the ticket booking
   app).
2. On Render.com: **New +** → **Web Service** → connect your GitHub
   repo → set the **Root Directory** to `backend`.
3. Build command: `mvn clean package -DskipTests`
   Start command: `java -jar target/energy-monitor-1.0.0.jar`
4. Add a free Render **PostgreSQL** database (New + → PostgreSQL).
   Render gives you connection details — add them as environment
   variables on your web service: `DATABASE_URL`, `DATABASE_USERNAME`,
   `DATABASE_PASSWORD`.
5. In `application.properties`, comment out the H2 lines and
   uncomment the Postgres block at the bottom (it already reads from
   those exact environment variable names).
6. Deploy. Render gives you a live URL like
   `https://energy-monitor-backend.onrender.com`.

### Deploy the frontend (Vercel, free tier)

1. On vercel.com: **New Project** → import the same GitHub repo → set
   **Root Directory** to `frontend`.
2. In `src/api.js`, change `API_BASE` to your live Render backend URL
   from the step above.
3. Deploy. Vercel gives you a live URL like
   `https://energy-dashboard.vercel.app` — this is the link you put
   in your resume/portfolio.

### Point the ESP32 at the live backend

Change `SERVER_URL` in the `.ino` file from your laptop's local IP to
the live Render URL, re-upload, and your physical hardware is now
reporting to a real cloud service — the same pattern used by actual
commercial IoT products.

---

## What to put in your portfolio writeup

- The architecture diagram above.
- A short GIF/video of the chart updating live as you send readings.
- One line on the WebSocket vs. polling decision — shows you thought
  about *why*, not just that it works: polling would mean the
  frontend asking "any new data?" every few seconds regardless of
  whether anything changed; broadcasting means the server only
  speaks when there's something new to say.
- The live links (frontend + backend) front and center.
