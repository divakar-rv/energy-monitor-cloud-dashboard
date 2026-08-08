/*
  ESP32 Energy Monitor -- Cloud Dashboard version
  ------------------------------------------------
  Adapted from your existing Smart Energy Monitoring System project.
  Instead of sending data to Blynk, this posts readings directly to
  your own Spring Boot backend as JSON, every few seconds.

  Hardware (same as your original project):
    - SCT-013 current sensor  -> analog pin
    - ZMPT101B voltage sensor -> analog pin

  What changed vs. the Blynk version: everything about reading the
  sensors stays the same. Only the "send the data somewhere" part is
  different -- instead of Blynk.virtualWrite(...), we build a small
  JSON string and HTTP POST it to our backend's /api/readings endpoint.
*/

#include <WiFi.h>
#include <HTTPClient.h>

// ---- WiFi credentials ----
const char* WIFI_SSID = "YOUR_WIFI_NAME";
const char* WIFI_PASSWORD = "YOUR_WIFI_PASSWORD";

// ---- Backend URL ----
// While testing locally: use your laptop's local IP (not "localhost" --
// the ESP32 is a separate device on your network and can't resolve that).
// Find your laptop's IP with `ipconfig` (Windows) and look for IPv4 Address.
// Once deployed: swap this for your live Render/Railway URL.
const char* SERVER_URL = "http://192.168.1.100:8082/api/readings";

// ---- Sensor pins (match your existing project's wiring) ----
const int VOLTAGE_PIN = 34;
const int CURRENT_PIN = 35;

// How often to send a reading, in milliseconds
const unsigned long SEND_INTERVAL_MS = 5000;
unsigned long lastSendTime = 0;

void setup() {
  Serial.begin(115200);
  connectToWiFi();
}

void loop() {
  if (millis() - lastSendTime >= SEND_INTERVAL_MS) {
    lastSendTime = millis();

    float voltage = readVoltage();
    float current = readCurrent();

    sendReading(voltage, current);
  }
}

void connectToWiFi() {
  Serial.print("Connecting to WiFi");
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("\nConnected! IP address: " + WiFi.localIP().toString());
}

// Reuse your existing calibration logic here -- this is a simplified
// placeholder. Replace the math with whatever formula your original
// smart-energy-monitoring project already uses for the ZMPT101B.
float readVoltage() {
  int raw = analogRead(VOLTAGE_PIN);
  float voltage = (raw / 4095.0) * 250.0; // placeholder calibration
  return voltage;
}

// Same note as above -- reuse your existing SCT-013 calibration formula.
float readCurrent() {
  int raw = analogRead(CURRENT_PIN);
  float current = (raw / 4095.0) * 10.0; // placeholder calibration
  return current;
}

void sendReading(float voltage, float current) {
  if (WiFi.status() != WL_CONNECTED) {
    Serial.println("WiFi disconnected, skipping send.");
    return;
  }

  HTTPClient http;
  http.begin(SERVER_URL);
  http.addHeader("Content-Type", "application/json");

  // Build the JSON body by hand -- simple enough at just two fields
  // that we don't need a full JSON library for it.
  String payload = "{\"voltage\":" + String(voltage, 2) +
                    ",\"current\":" + String(current, 2) + "}";

  int responseCode = http.POST(payload);

  if (responseCode > 0) {
    Serial.println("Sent reading, server responded: " + String(responseCode));
  } else {
    Serial.println("POST failed, error: " + http.errorToString(responseCode));
  }

  http.end();
}
