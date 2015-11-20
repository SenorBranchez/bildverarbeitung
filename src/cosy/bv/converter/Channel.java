package cosy.bv.converter;

import java.util.ArrayList;

public class Channel<T> {

    private ArrayList<ArrayList<T>> pixels;
    private int width;
    private int height;


    public Channel(int width, int height) {

        this.width = width;
        this.height = height;

        pixels = new ArrayList<ArrayList<T>>();

        for(int i=0; i<width; i++) {
            ArrayList<T> subList = new ArrayList<T>();

            for(int j=0; j<height; j++) {
                subList.add( null );
            }
            pixels.add(subList);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public T getPixelValue(int x, int y) {
        return pixels.get(x).get(y);
    }

    public void setPixelValue(int x, int y, T value) {
        pixels.get(x).set(y,value);
    }

    public String toString () {

        StringBuilder builder = new StringBuilder();

        for( ArrayList<T> list : pixels) {

            for( T pixel : list ) {
                System.out.println(pixel + " ");
                //builder.append( pixel + " " );
            }
            System.out.println("\n");
            builder.append( "\n" );

        }

        return builder.toString();
    }

    public T[][] toArray() {
        Object[][] array = new  Object[pixels.size()][];
        for (int i = 0; i < pixels.size(); i++) {
            ArrayList<T> row = pixels.get(i);
            array[i] = row.toArray(new Object[row.size()]);
        }

        return (T[][]) array;

    }
}

