/*
 * BİM304 Computer Algorithm Design (2020)
 * 2nd Assignment - HW2
 *
 * Bora Özdoğan
 * 20.04.2020
 * */

import java.util.*;

public class FindClosest {
    public static void main(String[] args) throws Exception{
        // create an object
        Scanner scan = new Scanner(System.in);

        //@ Girdi dosyalarında ondalık ayracı olarak '.' kullanılmış. Türkçe
        //@ işletim sisteminde sıkıntı çıkarıyordu.
        scan.useLocale(Locale.US);

        // read number of points and make arrays
        int numPoints = scan.nextInt();
        // actually, just one array
        Vector2D[] points = new Vector2D[numPoints];

        // read points and add to array
        for(int i = 0; i<numPoints; i++) {
            // read point coordinates and use them to create point objects
            // and then assign that object to current position in the array
            points[i] = new Vector2D(scan.nextDouble(), scan.nextDouble());
        }

        // invoke the divide&conquer algorithm with the array of points
        // just read from whatever input source we fed, in order to find
        // the pair of points whose distance between is smaller than the
        // other pairs of points in the set. and then, use this point pair
        // to provide useful information to the user, hence println statement.
        System.out.println(Algorithm.closestPair(points));
    }
}

/** @author Bora Özdoğan */
class Algorithm {
    //
    // Sorting Algorithm //
    /** Sort by ascending x-values */
    static void quicksort_x(Vector2D[] points, int left, int right) {
        int piv = _partition_x(points, left, right);

        if (left < piv-1)
            quicksort_x(points, left, piv - 1);

        if (piv < right)
            quicksort_x(points, piv, right);
    }

    /** Sort by ascending y-values */
    static void quicksort_y(Vector2D[] points, int left, int right) {
        int pivot = _partition_y(points, left, right);

        if (left < pivot - 1)
            quicksort_y(points, left, pivot - 1);

        if (pivot < right)
            quicksort_y(points, pivot, right);
    }

    private static int _partition_x(Vector2D[] points, int left, int right) {
        int ll = left;
        int rr = right;

        Vector2D pivot = points[(left + right) / 2];
        while (ll <= rr) {
            while (points[ll].x < pivot.x) ll++;
            while (points[rr].x > pivot.x) rr--;

            if (ll <= rr) {
                Vector2D tmp = points[ll];
                points[ll] = points[rr];
                points[rr] = tmp;

                ll++;
                rr--;
            }
        }

        return ll;
    }

    private static int _partition_y(Vector2D[] yPoints, int left, int right) {
        int ll = left;
        int rr = right;

        Vector2D pivot = yPoints[(left + right) / 2];
        while (ll <= rr) {
            while (yPoints[ll].y < pivot.y ) ll++;
            while (yPoints[rr].y > pivot.y) rr--;

            if (ll <= rr) {
                Vector2D tmp = yPoints[ll];
                yPoints[ll] = yPoints[rr];
                yPoints[rr] = tmp;

                ll++;
                rr--;
            }
        }

        return ll;
    }

    //
    // Closest Pair Algorithms //
    /** Brute-force algorithm for finding closest pair in the set */
    static PointPair bruteForce(Vector2D points[]){
        double best = Double.POSITIVE_INFINITY;
        PointPair bestPair = null;

        for(int i = 0; i < points.length; i++)
        for(int j = 0; j < points.length; j++) {
            if(i == j) continue;

            double dist = distance(points[i], points[j]);
            if(dist < best) {
                best = dist;
                bestPair = new PointPair(points[i], points[j]);
            }
        }

        return bestPair;
    }

    /** Divide&conquer algorithm for finding closest pair in the set */
    static PointPair closestPair(Vector2D[] points) {

        quicksort_x(points, 0, points.length-1);
        return _divideNConquer(points);
    }

    /**
     * @param points points must be sorted by ascending x values */
    private static PointPair _divideNConquer(Vector2D[] points) {
        if(points.length < 3)
            return bruteForce(points);

        int len = points.length;
        Vector2D[] Lx = new Vector2D[len/2];
        Vector2D[] Rx = new Vector2D[len - len/2];
        Vector2D[] Ly = new Vector2D[len/2];
        Vector2D[] Ry = new Vector2D[len - len/2];

        System.arraycopy(points, 0, Lx, 0, len/2);
        System.arraycopy(points, len/2, Rx, 0, len - len/2);
        System.arraycopy(points, 0, Ly, 0, len/2);
        System.arraycopy(points, len/2, Ry, 0, len - len/2);

        PointPair left = _divideNConquer(Lx);
        PointPair right = _divideNConquer(Rx);

        //@ Sağ-sol alt kümeler bitti. Şimdi ortaya yakın, biri sağda biri solda
        //@ kalan noktalardan daha yakını varsa onu bulmak gerek.
        PointPair closest = min(left, right);
        double delta = closest.getDistance();

        ArrayList<Vector2D> search = new ArrayList<>();
        Vector2D median = points[len/2];

        int p = len/2 - 1;
        int q = len/2 + 1;

        while(p >= 0 && Math.abs(median.x - points[p].x) <= delta) {
            search.add(points[p--]);
        }

        search.add(median);

        while(q < points.length && Math.abs(median.x - points[q].x) <= delta) {
            search.add(points[q++]);
        }

        Vector2D[] sorted_y = search.toArray(new Vector2D[0]);
        quicksort_y(sorted_y, 0, sorted_y.length-1);

        double best = delta;
        for(int i = 0; i < sorted_y.length; i++) {
            int j = i + 1;
            while(j < sorted_y.length
                    && Math.abs(sorted_y[i].y - sorted_y[j].y) <= delta) {

                PointPair pair = new PointPair(sorted_y[i], sorted_y[j]);

                if(pair.getDistance() < best) {
                    closest = pair;
                    best = pair.getDistance();
                }

                j++;
            }
        }

        return closest;
    }

    private static double distance(Vector2D v1, Vector2D v2) {
        return Math.sqrt(Math.pow(v1.x-v2.x, 2) + Math.pow(v1.y-v2.y, 2));
    }

    private static PointPair min(PointPair pair1, PointPair pair2) {
        //@ Null değerler sonsuz uzaklık olarak hesap edilecek.
        if(pair2 == null) return pair1;
        if(pair1 == null) return pair2;

        return pair1.getDistance() < pair2.getDistance() ? pair1 : pair2;
    }
}