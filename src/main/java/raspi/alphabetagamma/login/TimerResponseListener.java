package raspi.alphabetagamma.login;

import java.util.List;

/**
 * Created by shreyngd on 7/4/17.
 */

public interface TimerResponseListener {

    void onTimersLoad(List<Timers> timersList);
    void onTimerError();

}
