package pros.app.com.pros.base;

public interface HttpServiceView {

    /**
     * method to catch response
     */

    public void response(String response, int tag);

    /***
     * If Error or Exception occured
     */
    public void onError(int tag);
}
