#include <SoftwareSerial.h>

#define greenLed 11
#define redLed 12
#define tempSensor A1
#define gasSensor A0
#define buzzer 10
#define safety 60
#define button 5


int is_alarm = 0;
const float BETA = 3950; // should match the Beta Coefficient of the thermistor
void setup() {
  pinMode(buzzer, OUTPUT); // buzzer
  pinMode(redLed, OUTPUT); // red led
  pinMode(greenLed, OUTPUT); // green led
  digitalWrite(greenLed, HIGH);
  Serial.begin(9600);
  pinMode(tempSensor, INPUT);
  pinMode(gasSensor, INPUT);
  pinMode(button, INPUT);
}

void alarm(){
  tone(buzzer, 500 , 1000);
  digitalWrite(redLed, HIGH);
  digitalWrite(greenLed, LOW);
  Serial.print("ALARM*");
}

void turnOff(){
  digitalWrite(redLed, LOW);
  digitalWrite(greenLed, HIGH);
  Serial.print("STOP*");
}

void loop() {
  int total = 0; // reset total
  for (int x = 0; x < 64; x++) { // 64(max) analogue readings for averaging
    total = total + analogRead(tempSensor); // add each value
  }
  float celsius = (total * 3.3 / 64 / 1024) * 100; // Calibrate by changing the last digit(s)
  int analogGas = analogRead(gasSensor);

  String send="DATA:";
  send += String(celsius,2) + "#";
  send += String(analogGas) + "*";
  Serial.print(send);
  int click = digitalRead(button);
  if(is_alarm && click == HIGH){
    is_alarm=0;
    turnOff();
    delay(5000);
  }
  if((celsius > safety && analogGas > safety * 4.5) || (celsius > safety*1.5) || (analogGas > safety * 5) || (Serial.available() && Serial.readStringUntil('*').substring(0, 5) == "ALARM")){
    alarm();
    is_alarm = 1;
  }else if(is_alarm){
    turnOff();
  }

  delay(1000);

}
