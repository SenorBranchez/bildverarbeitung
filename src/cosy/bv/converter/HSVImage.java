package cosy.bv.converter;


public class HSVImage extends Image {

    public String[] getChannelNames() {
    	String[] ret = {"h", "s", "v"};
    	return ret;
    }
}
