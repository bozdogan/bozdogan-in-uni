/*
* BİM304 Computer Algorithm Design (2020)
* 2nd Assignment - HW2
*
* Bora Özdoğan
* 20.04.2020
* */

/** A pair of points */
public class PointPair {

    private Vector2D p1;
    private Vector2D p2;

    /** Creates a pair of points. */
    public PointPair(Vector2D p1, Vector2D p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Vector2D getFirst()
    {
        return p1;
    }
    public Vector2D getSecond()
    {
        return p2;
    }

    public double getDistance() {
        return Math.sqrt(Math.pow(p1.x-p2.x, 2) + Math.pow(p1.y-p2.y, 2));
    }

//    /** Compares with another pair
//     *
//     * @param p a pair
//     * @return negative if this distance is smaller, positive if it's larger,
//     * 0 if they're equal
//     */
//    public int compareWith(PointPair p) {
//        return (int)Math.signum(getDistance() - p.getDistance());
//    }
//
//    /** Normalize: point with smaller x value comes p1. If x values are
//     * equal, point with smaller y value comes p1. */
//    public PointPair normalize() {
//        if(p1.x>p2.x || p1.x==p2.x && p1.y>p2.y) {
//            Vector2D temp = p1;
//            p1 = p2;
//            p2 = temp;
//        }
//
//        return this;
//    }

    @Override
    public String toString()
    {
        if (p1.x > p2.x || p1.x == p2.x && p1.y > p2.y)
            return String.format("(%s,%s)-(%s,%s)=%s",
                    p2.x, p2.y, p1.x, p1.y, getDistance());
        else
            return String.format("(%s,%s)-(%s,%s)=%s",
                p1.x, p1.y, p2.x, p2.y, getDistance());
    }
}

/** Holds two coordinate values */
class Vector2D {
    double x;
    double y;

    public Vector2D() { }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }
}