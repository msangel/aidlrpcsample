package ua.co.k.playground;

import ua.co.k.playground.Event;
import ua.co.k.playground.EventListeners;
import ua.co.k.playground.AIDLEmitter;

interface IWindscribeInterface {
    void startVPN(String locale);
    void stopVPN();
    Event sendAndReceive(in Event event);
    oneway void registerListener(in EventListeners l);
    oneway void unregisterListener(in EventListeners l);
    oneway void acceptEmitter(in AIDLEmitter emitter, String param);
}
