package main;

import java.awt.Rectangle;

public class EventRect extends Rectangle {

	private static final long serialVersionUID = 1L;

	// variaveis de controle da posicao default p quando precisar RESETAR p posicao
	// inicial apos checkar a collision
	private int eventRectDefaultX, eventRectDefaultY;
	private boolean eventDone;

	public int getEventRectDefaultX() {
		return eventRectDefaultX;
	}

	public void setEventRectDefaultX(int eventRectDefaultX) {
		this.eventRectDefaultX = eventRectDefaultX;
	}

	public int getEventRectDefaultY() {
		return eventRectDefaultY;
	}

	public void setEventRectDefaultY(int eventRectDefaultY) {
		this.eventRectDefaultY = eventRectDefaultY;
	}

	public boolean isEventDone() {
		return eventDone;
	}

	public void setEventDone(boolean eventDone) {
		this.eventDone = eventDone;
	}

}
