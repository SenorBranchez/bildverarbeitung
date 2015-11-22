package cosy.bv.converter;


public class YUVImage extends Image {

    public String[] getChannelNames() {
    	String[] ret = {"y", "u", "v"};
    	return ret;
    }

}
