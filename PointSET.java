/* *****************************************************************************
 *  Name: Kartik Kesarwani
 *  Date: 17/05/2020 DD/MM/YYYY
 *  Description: Set of points in the unit square
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {

    private final TreeSet<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("wrong argument");
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("wrong argument");
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        // StdDraw.setPenRadius(0.01);
        for (Point2D point : points) {
            point.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("wrong argument");
        Queue<Point2D> q = new Queue<>();
        for (Point2D point : points) {
            if (rect.contains(point)) {
                q.enqueue(point);
            }
        }
        return q;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("wrong argument");
        double minDistance = Double.POSITIVE_INFINITY;
        Point2D closestPoint = null;
        for (Point2D point : points) {
            double distance = point.distanceSquaredTo(p);
            if (Double.compare(minDistance, distance) > 0) {
                minDistance = distance;
                closestPoint = point;
            }
        }
        return closestPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

        String filename = args[0];
        In in = new In(filename);
        ArrayList<Point2D> points = new ArrayList<>();
        PointSET pointSET = new PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            pointSET.insert(p);
            points.add(p);
        }
        StdOut.println("---------Insertion to tree set complete----------");

        for (Point2D point : points) {
            StdOut.println("Search : " + point.x() + ", " + point.y());
            StdOut.println("Result : " + pointSET.contains(point));
        }
        Point2D p = new Point2D(0.498, 0.209);
        StdOut.println("Search : " + p.x() + ", " + p.y());
        StdOut.println("Result : " + pointSET.contains(p));
        StdOut.println("---------Search in tree set complete----------");
        pointSET.draw();

    }
}
