#include <FastLED.h>

bool command_complete = false;  // whether the command is complete
int arg1;
int arg2;

//Auckland declarations
#define LED_PIN     9
#define NUM_LEDS    120
#define BRIGHTNESS  75
#define LED_TYPE    WS2811
#define COLOR_ORDER GRB
CRGB leds[NUM_LEDS];
bool auckland = 0;

//Relay declarations
enum COMMANDS {OFF, ON, TOGGLE, AUCKLAND, ALL_OFF, KIM, LIGHT};
const size_t RELAY[10] = {7, 6, 5, 4, 3, 2, 8, 12, A0};
const size_t REL_ON[10]= {0, 0, 0, 0, 0, 0, 0, 0, 1};
bool state[10]         = {1, 1, 1, 1, 1, 1, 1, 1, 0};
bool auckland_light_state = 0;

void serialEvent() {
	while (Serial.available()) {
		arg1 = Serial.parseInt();
		arg2 = Serial.parseInt();
		if (Serial.read() == '\n') {
			command_complete = true; Serial.print(arg1);
			Serial.print(',');
			Serial.println(arg2);
		}
	}
	return;
}

void auckland_off() {
	for(int i=0; i<NUM_LEDS; i++) {
		leds[i]=0;
	}
	FastLED.show();
	return;
}

void auckland_light(int state) {
	CRGB color;
	color.r = 90;
	color.g = 90;
	color.b = 90;
	if (state == ON) {
		for(int i=0; i<NUM_LEDS; i++) {
			leds[i]=color;
		}
		FastLED.show();
		auckland_light_state = true;
	}
	else {
		auckland_off();
		auckland_light_state = false;
	}
	return;
}

void auckland_light() {
	CRGB color;
	color.r = 90;
	color.g = 90;
	color.b = 90;
	auckland_light_state = !auckland_light_state;
	if (auckland_light_state) {
		for(int i=0; i<NUM_LEDS; i++) {
			leds[i]=color;
		}
		FastLED.show();
	}
	else {
		auckland_off();
	}
	return;
}

void all_off() {
	for (int i = 0; i <= 8; i++) {
		digitalWrite(RELAY[i], !REL_ON[i]);
		delay(500);
	}
	auckland_off();
	return;
}

void all_off(size_t exclude) {
	for (size_t i = 0; i <= 8; i++) {
		if (i != exclude) {
			digitalWrite(RELAY[i], !REL_ON[i]);
			delay(500);
		}
	}
	auckland_off();
	return;
}

void setup() {
	delay( 3000 ); // power-up safety delay
	FastLED.addLeds<LED_TYPE, LED_PIN, COLOR_ORDER>(leds, NUM_LEDS).setCorrection( TypicalLEDStrip );
	FastLED.setBrightness(  BRIGHTNESS );
	// initialize serial:
	Serial.begin(9600);

	//main light
	pinMode(RELAY[8], OUTPUT);
	digitalWrite(RELAY[8], LOW);
	//relay board
	for (size_t i = 0; i < 8; i++) {
		pinMode(RELAY[i], OUTPUT);
		digitalWrite(RELAY[i], HIGH);
	}
	all_off();
}

void toggle(size_t port) {
	state[port] = !state[port];
	digitalWrite(RELAY[port], state[port]);
	return;
}

void on(size_t port) {
	state[port] = 1;
	digitalWrite(RELAY[port], REL_ON[port]);
	return;
}

void off(size_t port) {
	state[port] = 0;
	digitalWrite(RELAY[port], !REL_ON[port]);
	return;
}

CRGB wheel(int WheelPos, int dim) {
	CRGB color;
	if (85 > WheelPos) {
		color.r=0;
		color.g=WheelPos * 3/dim;
		color.b=(255 - WheelPos * 3)/dim;;
	}
	else if (170 > WheelPos) {
		color.r=WheelPos * 3/dim;
		color.g=(255 - WheelPos * 3)/dim;
		color.b=0;
	}
	else {
		color.r=(255 - WheelPos * 3)/dim;
		color.g=0;
		color.b=WheelPos * 3/dim;
	}
	return color;
}

void rainbowCycle(int wait, int cycles, int dim) {

	//loop several times with same configurations and same delay
	for(int cycle=0; cycle < cycles; cycle++){

		byte dir=random(0,2);
		int k=255;

		//loop through all colors in the wheel
		for (int j=0; j < 256; j++,k--) {

			if(k<0) {
				k=255;
			}

			//Set RGB color of each LED
			for(int i=0; i<NUM_LEDS; i++) {
				CRGB ledColor = wheel(((i * 256 / NUM_LEDS) + (dir==0?j:k)) % 256,dim);
				leds[i]=ledColor;
			}
			FastLED.show();
			FastLED.delay(wait);
		}
	}
}

void loop() {
	if(command_complete) {
		switch(arg1) {
			case(TOGGLE):
				toggle(arg2);
				break;
			case(ON):
				on(arg2);
				break;
			case(OFF):
				off(arg2);
				break;
			case(AUCKLAND):
				switch(arg2) {
					case(ON):
						auckland = 1;
						break;
					case(OFF):
						auckland = 0;
						auckland_off();
						break;
					case(TOGGLE):
						auckland = !auckland;
						if (!auckland) auckland_off();
						break;
					case(LIGHT):
						auckland_light();
						break;
				}
				break;
			case(ALL_OFF):
				if (arg2 < 0) all_off();
				else all_off(arg2);
				break;
			case(KIM):
				if (state[9]) FastLED.setBrightness(80);
				else FastLED.setBrightness(25);
				rainbowCycle(2,1,2);
				if (auckland_light_state) auckland_light(ON);
				else auckland_off();
		}
		command_complete = false;
	}

	if (auckland) {
		randomSeed(millis());

		int wait=random(10,30);
		int dim=random(4,6);
		int max_cycles=20;
		int cycles=random(1,max_cycles+1);

		rainbowCycle(wait, cycles, dim);
	}
}
