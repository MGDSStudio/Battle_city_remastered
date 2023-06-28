package io.itch.mgdsstudio.engine.libs;

public class Coordinate {
    public float x,y,z;

    public Coordinate(float x, float y) {
        this.x = x;
        this.y = y;
        z = 0;
    }

    public Coordinate(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return x+"x"+y;
    }

    public final Coordinate clone(){
        return new Coordinate(x,y,z);
    }
}
