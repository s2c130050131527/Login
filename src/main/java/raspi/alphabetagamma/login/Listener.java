package raspi.alphabetagamma.login;

import java.util.List;



public interface Listener {

    void onLoaded(List<DeviceStats> deviceStatsList);

    void onError();
}