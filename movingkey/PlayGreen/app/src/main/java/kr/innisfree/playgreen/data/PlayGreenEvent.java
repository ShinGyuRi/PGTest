package kr.innisfree.playgreen.data;

public class PlayGreenEvent {
	private int whatEvent;
	private Object data;

	public interface EVENT_TYPE {
		int PG21_REFRESH = 1;
		int TIMELINE_REFRESH = 2;
		int MYPAGE_REFRESH = 3;
		int LEAVE_MEMBER = 4;
		int ACTION_SEARCH_TAG = 5;
		int ACTION_SEARCH_USER = 6;
		int ALARM_COUNT_UPDATE = 7;
	}

	public PlayGreenEvent(int whatEvent) {
		this.whatEvent = whatEvent;
	}

	public PlayGreenEvent(int whatEvent, Object data) {
		this.whatEvent = whatEvent;
		this.data = data;
	}

	public int getEvent() {
		return whatEvent;
	}

	public Object getData() {
		if (data == null) return null;
		return data;
	}

}
