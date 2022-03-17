/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package system.observer;

public interface Subject {
	public void addObserver(Observer oNewObserver);
	public void removeObserver(Observer oObserver);
	public void notifyAllObserver();
}
