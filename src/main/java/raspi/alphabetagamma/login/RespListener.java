package raspi.alphabetagamma.login;

import java.util.List;
import java.util.Timer;

/**
 * Created by shreyngd on 4/4/17.
 */

public interface RespListener {


    void onLoaded(List<Room> roomList);
    void onError();
}
