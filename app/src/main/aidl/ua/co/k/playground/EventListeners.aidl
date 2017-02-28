package ua.co.k.playground;

import ua.co.k.playground.Event;

interface EventListeners {
    oneway void eventHappened(in Event event);
}
