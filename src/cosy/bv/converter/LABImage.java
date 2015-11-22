package cosy.bv.converter;


public class LABImage extends Image {

    public String[] getChannelNames() {
    	String[] ret = {"l", "a", "b"};
    	return ret;
    }
}
