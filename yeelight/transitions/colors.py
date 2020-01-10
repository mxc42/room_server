#!/usr/bin/python3.6

from yeelight import *
from time import sleep

def intro(bpm=120):
    blue = 221
    violet = 295
    red = 359
    duration = int(60000 / bpm)
    transitions = [
        HSVTransition(blue, 100, duration=duration, brightness=100),
        HSVTransition(violet, 100, duration=duration, brightness=100),
        HSVTransition(red, 100, duration=duration, brightness=100),
        HSVTransition(violet, 100, duration=duration, brightness=100),
    ]
    return transitions

def everything_is_blue(bpm=200):
    blue = 220
    duration = int(60000 / bpm)
    transitions = [
        HSVTransition(blue, 100, duration=duration, brightness=100),
        HSVTransition(blue, 100, duration=duration, brightness=1),
    ]
    return transitions

def everything_is_grey(bpm=200):
    grey = 220
    saturation = 10
    duration = int(60000 / bpm)
    transitions = [
        HSVTransition(grey, saturation, duration=duration, brightness=20),
        HSVTransition(grey, saturation, duration=duration, brightness=1),
    ]
    return transitions

bulb1 = Bulb("10.0.0.22", auto_on=True)

bulb1.turn_on()

bulb1.start_flow( Flow(count=100, transitions = intro(bpm=50)) )
sleep(40.7)
bulb1.start_flow( Flow(count=100, transitions = everything_is_blue(bpm=200)) )
sleep(18.8)
bulb1.start_flow( Flow(count=100, transitions = everything_is_grey(bpm=200)) )
sleep(9.5)
bulb1.start_flow( Flow(count=100, transitions = everything_is_blue(bpm=200)) )
sleep(9.6)
bulb1.start_flow( Flow(count=100, transitions = intro(bpm=50)) )

sleep(38.0)
#everything is blue
bulb1.start_flow( Flow(count=100, transitions = everything_is_blue(bpm=200)) )
sleep(18.8)
#everything is grey
bulb1.start_flow( Flow(count=100, transitions = everything_is_grey(bpm=200)) )
sleep(9.8)
#he's blue
bulb1.start_flow( Flow(count=100, transitions = everything_is_blue(bpm=200)) )
sleep(10)


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
sleep(2)

blue = 221
#everything is blue
bulb1.start_flow( Flow(count=100, transitions = everything_is_blue(bpm=200)) )
sleep(18.8)
#everything is grey
bulb1.start_flow( Flow(count=100, transitions = everything_is_grey(bpm=200)) )
sleep(9.8)
#he's blue
bulb1.start_flow( Flow(count=100, transitions = everything_is_blue(bpm=200)) )
sleep(25)
bulb1.start_flow( Flow(count=1, action=Flow.actions.stay, transitions=[HSVTransition(blue, 100, duration=2500, brightness=100)]) )

sleep(10)
bulb1.start_flow( Flow(count=1, action=Flow.actions.stay, transitions=[RGBTransition(255, 255, 255, duration=1000, brightness=100)]) )
