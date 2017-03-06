package ua.co.k.playground;
import ua.co.k.playground.Event;

interface AIDLEmitter {
oneway void onNext(in Event value);
// take this: https://github.com/lencinhaus/androjena/blob/master/androjena.test.android/src/it/polimi/dei/dbgroup/pedigree/androjena/test/ParcelableException.java
oneway void onError(in String error);
oneway void onComplete();
}
