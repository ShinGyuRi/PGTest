package tiltcode.movingkey.com.tiltcode_new.Model;

/**
 * Created by Gyul on 2016-06-27.
 */
public class TiltCodeEvent {
    private int whatEvent;
    private Object data;

    public interface EVENT_TYPE {
        int COUPON_REFRESH = 1;
    }

    public TiltCodeEvent(int whatEvent) {this.whatEvent = whatEvent;}

    public TiltCodeEvent(int whatEvent, Object data)    {
        this.whatEvent = whatEvent;
        this.data = data;
    }

    public int getEvent()   {return whatEvent;}

    public Object getData() {
        if(data == null) return null;
        return data;
    }
}
