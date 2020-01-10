#!/usr/bin/python3.6

from yeelight import *
from time import sleep

bulb1 = Bulb("10.0.0.22", auto_on=True)
#bulb1.turn_on()
#sleep(1)

blue = 255
lilac = 292

bulb1.turn_off(duration=2000)
sleep(2)
bulb1.start_flow( Flow(count=1, action=Flow.actions.stay,transitions=[RGBTransition(255, 0, 0, duration=2500, brightness=100)]) )
sleep(3)
bulb1.start_flow( Flow(count=1, action=Flow.actions.stay, transitions=[HSVTransition(blue, 100, duration=2500, brightness=100)]) )
#bulb1.start_flow( Flow(count=1, action=Flow.actions.off,transitions=[RGBTransition(255, 0, 0, duration=1700, brightness=100)]) )
sleep(4)
bulb1.start_flow( Flow(count=1, action=Flow.actions.stay, transitions=[HSVTransition(lilac, 100, duration=2500, brightness=100)]) )
#bulb1.start_flow( Flow(count=1, action=Flow.actions.off,transitions=[RGBTransition(255, 0, 0, duration=1700, brightness=100)]) )

sleep(7)
bulb1.turn_off(duration=1500)

#bulb1.turn_on(duration=50)
