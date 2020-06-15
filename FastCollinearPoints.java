/* *****************************************************************************
 *  Name: Kartik Kesarwani
 *  Date: 02/05/2020
 *  Description: Sort based algorithm to find line segments with 4 or more points
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {

    private final ArrayList<LineSegment> lines;
    private int numOfSegments;
    private final Point[] points;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] p) {
        if (p == null)
            throw new IllegalArgumentException("wrong argument");

        for (int i = 0; i < p.length; i++) {
            if (p[i] == null)
                throw new IllegalArgumentException("wrong argument");
        }

        int numOfPoints = p.length;
        points = p.clone();
        Arrays.sort(points);

        for (int i = 0; i < numOfPoints - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0)
                throw new IllegalArgumentException("wrong argument");
        }
        numOfSegments = 0;
        lines = new ArrayList<>();
        // StdOut.println("Points : " + Arrays.toString(points));
        findCollinear(numOfPoints, p);
    }

    private void findCollinear(int numOfPoints, Point[] p) {

        for (int i = 0; i < numOfPoints; i++) {
            Point currPoint = p[i];
            Comparator<Point> bySlope = currPoint.slopeOrder();
            Arrays.sort(points, bySlope);
            // StdOut.println("Sorted by slope : " + Arrays.toString(points));
            int collinearPointCount = 0;
            // StdOut.println("Slope with current point : " + currPoint.slopeTo(points[1]));
            int j;
            for (j = 2; j < numOfPoints; j++) {
                // StdOut.println("Slope with current point : " + currPoint.slopeTo(points[j]));
                int eq = Double
                        .compare(currPoint.slopeTo(points[j]), currPoint.slopeTo(points[j - 1]));
                if (eq == 0) {
                    collinearPointCount++;
                }
                else {
                    if (collinearPointCount >= 2) {
                        addLineSegment(collinearPointCount, j, currPoint);
                    }
                    collinearPointCount = 0;
                }
            }
            if (collinearPointCount >= 2) {
                addLineSegment(collinearPointCount, j, currPoint);
            }
        }
    }

    private void addLineSegment(int collinearPointCount, int j, Point currPoint) {

        int end = j - 1;
        int start = end - collinearPointCount;
        boolean addSegment = isSmallestPointOriginOfSegment(start, end, points,
                                                            currPoint);
        if (addSegment) {
            numOfSegments++;
            Point largestPoint = findLargestPoint(start, end, points, currPoint);
            LineSegment ls = new LineSegment(currPoint, largestPoint);
            lines.add(ls);
        }
    }

    private Point findLargestPoint(int start, int end, Point[] point, Point currPoint) {
        Point largestPoint = currPoint;
        for (int i = start; i <= end; i++) {
            if (largestPoint.compareTo(point[i]) < 0)
                largestPoint = point[i];
        }
        return largestPoint;
    }

    private boolean isSmallestPointOriginOfSegment(int start, int end, Point[] point,
                                                   Point currPoint) {
        for (int i = start; i <= end; i++) {
            if (currPoint.compareTo(point[i]) > 0)
                return false;
        }
        return true;
    }

    public int numberOfSegments() {
        return numOfSegments;
    }

    public LineSegment[] segments() {
        LineSegment[] linesArray = new LineSegment[lines.size()];
        linesArray = lines.toArray(linesArray);
        return linesArray;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();

    }
}
