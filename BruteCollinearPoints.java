/* *****************************************************************************
 *  Name: Kartik Kesarwani
 *  Date: 02/05/2020
 *  Description: Brute force algorithm to find line segments(4 collinear points)
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {

    private final ArrayList<LineSegment> lines;
    private int numOfSegments;
    private final Point[] points;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] p) {
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
        findCollinear(numOfPoints);
    }

    private void findCollinear(int numOfPoints) {

        for (int firstPoint = 0; firstPoint < numOfPoints - 3; firstPoint++) {
            for (int secondPoint = firstPoint + 1; secondPoint < numOfPoints - 2; secondPoint++) {
                for (int thirdPoint = secondPoint + 1; thirdPoint < numOfPoints - 1; thirdPoint++) {
                    for (int fourthPoint = thirdPoint + 1; fourthPoint < numOfPoints;
                         fourthPoint++) {
                        double m1 = points[firstPoint].slopeTo(points[secondPoint]);
                        double m2 = points[firstPoint].slopeTo(points[thirdPoint]);
                        double m3 = points[firstPoint].slopeTo(points[fourthPoint]);
                        if (Double.compare(m1, m2) == 0
                                && Double.compare(m2, m3) == 0) { // get line segment
                            numOfSegments++;
                            LineSegment ls = new LineSegment(points[firstPoint],
                                                             points[fourthPoint]);
                            lines.add(ls);
                        }
                    }
                }
            }
        }
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
        // FastCollinearPoints collinear = new FastCollinearPoints(points);
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();

    }
}
