#include <Arduino.h>
#include <WiFi.h>
#include <HTTPClient.h>
#include <SoftwareSerial.h>
#include <Firebase_ESP_Client.h>
//Provide the token generation process info.
#include "addons/TokenHelper.h"
//Provide the RTDB payload printing info and other helper functions.
#include "addons/RTDBHelper.h"

#define LED 5
#define ssid "TP-Link_BAC2"
#define password "khongbietmatkhau"

#define URL "https://fcm.googleapis.com/fcm/send"
#define SERVER_KEY "AAAAiDPQdm8:APA91bG4KAt4TTNEL2nKfKR148UDt2--IQHsco85yOt2ZSYM69qOcmNhdAiG6mCryMXGb4xINA30LnLsCQfu50vljhXd-DXKSzYb7WUCXzTCRCE8BREO1hCb7aytSqcW21IOgEiJHc2_"
SoftwareSerial zigbee(16, 17);
String DEVICE_REGISTRATION_ID_TOKEN = "fL4wE653TkC8I4IkI4WN4U:APA91bH4dO3Hqz01eUwK7f6RP2QSgUmP017xCxZJMsGamAPE1ICrvCYlTX3m5-ysK1y-KX-P2JwEzaxPkpvsLUVq6TiXlgryYUdkzat83MmMEokYvLuudO_xc_OGECeUqZSNDZqx0ITo";

/* 6. Define the Firebase Data object */
FirebaseData fbdo;

int isSent = 0;


void alarm() {
  digitalWrite(LED, HIGH);
  if(!isSent) {
    sendAlarm();
    isSent = 1;
  }
}

void stop() {
  digitalWrite(LED, LOW);
}

void setup() {
  Serial.begin(9600);
  zigbee.begin(9600);
  pinMode(LED, OUTPUT);
  digitalWrite(LED, HIGH);


  WiFi.begin(ssid, password);
  Serial.print("Connecting to Wi-Fi");
  unsigned long ms = millis();
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(300);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  Serial.printf("Firebase Client v%s\n\n", FIREBASE_CLIENT_VERSION);

}

void loop() {
  if (zigbee.available() > 0) {
    String recv = zigbee.readStringUntil('*');
    if (recv.substring(0, 5) == "ALARM") {
      alarm();
    } else if (recv.substring(0, 5) == "STOP") {
      stop();
      isSent=0;
    }
    Serial.println("Receive: ");
    Serial.println(recv);
  } else {
    stop();
  }
}

void sendAlarm() {
  Serial.print("Send Firebase Cloud Messaging... ");

  HTTPClient http;
  http.begin(URL);
  http.addHeader("Content-Type", "application/json");
  http.addHeader("Authorization", "key=" + String(SERVER_KEY));

  String httpRequestData = 
  "{\"to\":\"" + DEVICE_REGISTRATION_ID_TOKEN + "\"," + 
  "\"data\":{\"title\":\"chay roiii\",\"location\":\"test123\",\"time\":\"" +  String(millis()) +"\"}}";
  int httpResponseCode = http.POST(httpRequestData);
  if (httpResponseCode > 0) {
    String response = http.getString();
    Serial.println(httpResponseCode);
    Serial.println(response);
  } else {
    Serial.print("Error on sending POST: ");
    Serial.println(httpResponseCode);
  }
}