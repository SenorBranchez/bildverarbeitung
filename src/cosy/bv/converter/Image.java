package cosy.bv.converter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class Image {

    private HashMap<String, Channel> channels;

    public Image() {
        channels = new HashMap<String, Channel>();
    }

    public void addChannel( String descriptor, Channel c ) {
        channels.put(descriptor,c);
    }

    public Channel getChannel( String descriptor ) {
        return channels.get(descriptor);
    }

    public String toString() {

        StringBuilder builder = new StringBuilder();

        Iterator it = channels.entrySet().iterator();

        while( it.hasNext() ) {
            Map.Entry pair = (Map.Entry) it.next();
            builder.append(pair.getKey() + "\n");
            builder.append(pair.getValue().toString());
        }

        return builder.toString();
    }


}
