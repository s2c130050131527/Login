package raspi.alphabetagamma.login;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shreyngd on 1/4/17.
 */

public class Response {

    private List<DeviceStats> device = new ArrayList<DeviceStats>();

    public List<DeviceStats> getDevice() {
        return device;
    }
}
