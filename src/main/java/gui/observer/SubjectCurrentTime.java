/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.observer;

import java.util.ArrayList;
import java.util.List;

import system.observer.Observer;
import system.observer.Subject;

public class SubjectCurrentTime implements Subject {

	private List<Observer> lObservers = new ArrayList<Observer>();
	
	@Override
	public void addObserver(Observer oNewObserver) {
		lObservers.add(oNewObserver);
	}

	@Override
	public void removeObserver(Observer oObserver) {
		lObservers.remove(oObserver);
	}

	@Override
	public void notifyAllObserver() {
		for(Observer observer:lObservers)
			observer.update();
	}

}
