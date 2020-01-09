#!/usr/bin/python3.6

from yeelight import *
from time import sleep

def disco(bpm=120):
    """
    Color changes to the beat.

    :param int bpm: The beats per minute to pulse to.

    :returns: A list of transitions.
    :rtype: list
    """

    blue = 222
    violet = 295
    red = 359
    duration = int(60000 / bpm)
    transitions = [
        HSVTransition(blue, 100, duration=duration, brightness=100),
        HSVTransition(blue, 100, duration=duration, brightness=1),
        HSVTransition(violet, 100, duration=duration, brightness=100),
        HSVTransition(violet, 100, duration=duration, brightness=1),
        HSVTransition(red, 100, duration=duration, brightness=100),
        HSVTransition(red, 100, duration=duration, brightness=1),
        HSVTransition(violet, 100, duration=duration, brightness=100),
        HSVTransition(violet, 100, duration=duration, brightness=1),
    ]
    return transitions

def blue_disco(bpm=120):
    """
    Color changes to the beat.

    :param int bpm: The beats per minute to pulse to.

    :returns: A list of transitions.
    :rtype: list
    """

    blue = 208
    duration = int(60000 / bpm)
    transitions = [
        HSVTransition(blue, 100, duration=duration, brightness=100),
        HSVTransition(blue, 100, duration=duration, brightness=1),
    ]
    return transitions

bulb1 = Bulb("10.0.0.22")

bulb1.turn_on()

bulb1.start_flow( Flow(count=100, transitions = disco(bpm=100)) )
sleep(40.8)
bulb1.start_flow( Flow(count=100, transitions = blue_disco(bpm=200)) )


