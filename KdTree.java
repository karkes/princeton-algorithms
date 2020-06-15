/* *****************************************************************************
 *  Name: Kartik Kesarwani
 *  Date: 17/05/2020 DD/MM/YYYY
 *  Description: 2d-tree to implement the same API as PointSET.java
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class KdTree {

    private int size;
    private Node root;

    private enum Orientation {
        VERTICAL, HORIZONTAL
    }

    private class Node {
        private final Point2D point;
        private final RectHV rect;
        // left or bottom point ref
        private Node lb;
        // right or top point ref
        private Node rt;
        private final Orientation ornt;

        Node(Point2D p, Orientation orientation, RectHV rec) {
            point = p;
            ornt = orientation;
            rect = rec;
        }
    }

    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("wrong argument");
        if (size == 0) {
            root = new Node(p, Orientation.VERTICAL, new RectHV(0, 0, 1, 1));
            size++;
            return;
        }
        insert(root, p, root.ornt, null);
        // StdOut.println("Root after insert of " + p.toString() + " is : " + r.point.toString());
    }

    private RectHV getRectangle(Point2D point, Orientation ornt, Node parentNode) {
        RectHV rec = null;
        if (ornt == Orientation.VERTICAL) {

            int top = Double.compare(point.y(), parentNode.point.y());
            if (top >= 0) { // top
                rec = new RectHV(parentNode.rect.xmin(), parentNode.point.y(),
                                 parentNode.rect.xmax(), parentNode.rect.ymax());
            }
            else { // bottom
                rec = new RectHV(parentNode.rect.xmin(), parentNode.rect.ymin(),
                                 parentNode.rect.xmax(), parentNode.point.y());
            }
        }
        else if (ornt == Orientation.HORIZONTAL) {

            int left = Double.compare(point.x(), parentNode.point.x());
            if (left >= 0) { // right
                rec = new RectHV(parentNode.point.x(), parentNode.rect.ymin(),
                                 parentNode.rect.xmax(), parentNode.rect.ymax());
            }
            else { // left
                rec = new RectHV(parentNode.rect.xmin(), parentNode.rect.ymin(),
                                 parentNode.point.x(), parentNode.rect.ymax());
            }
        }
        return rec;
    }

    private Node insert(Node node, Point2D point, Orientation ornt, Node parentNode) {
        if (node == null) {
            // StdOut.println("Orientation of new point : " + point.toString() + " is : " + ornt);
            RectHV rec = getRectangle(point, ornt, parentNode);
            // StdOut.println("Rectangle for new point is : " + rec.toString());
            size++;
            return new Node(point, ornt, rec);
        }
        if (point.equals(node.point)) return node;
        int cmp = 0;
        if (node.ornt == Orientation.VERTICAL) {
            // compare x
            cmp = Double.compare(node.point.x(), point.x());
            ornt = Orientation.HORIZONTAL;
        }
        else if (node.ornt == Orientation.HORIZONTAL) {
            // compare y
            cmp = Double.compare(node.point.y(), point.y());
            ornt = Orientation.VERTICAL;
        }

        if (cmp > 0) {
            node.lb = insert(node.lb, point, ornt, node);
        }
        else {
            node.rt = insert(node.rt, point, ornt, node);
        }
        return node;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("wrong argument");
        boolean exist = search(root, p);
        return exist;
    }

    private boolean search(Node node, Point2D point) {
        if (node == null) return false;
        if (point.equals(node.point)) return true;
        int cmp = 0;
        if (node.ornt == Orientation.VERTICAL) {
            // compare x
            cmp = Double.compare(node.point.x(), point.x());
        }
        else if (node.ornt == Orientation.HORIZONTAL) {
            // compare y
            cmp = Double.compare(node.point.y(), point.y());
        }
        if (cmp > 0) {
            // left
            return search(node.lb, point);
        }
        // right
        return search(node.rt, point);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.clear();
        inorder(root);
    }

    private void inorder(Node node) {
        if (node == null) return;
        inorder(node.lb);
        // line
        // StdDraw.pause(20);
        StdDraw.setPenRadius();
        if (node.ornt == Orientation.HORIZONTAL) {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.rect.xmin(), node.point.y(), node.rect.xmax(), node.point.y());

        }
        else if (node.ornt == Orientation.VERTICAL) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.point.x(), node.rect.ymin(), node.point.x(), node.rect.ymax());
        }
        // point
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        node.point.draw();
        inorder(node.rt);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("wrong argument");
        Queue<Point2D> rangeQ = new Queue<>();
        searchRange(rect, root, rangeQ);
        return rangeQ;
    }

    private void searchRange(RectHV rect, Node node, Queue<Point2D> rangeQ) {
        if (node == null) return;

        if (rect.intersects(node.rect)) {
            if (rect.contains(node.point)) {
                rangeQ.enqueue(node.point);
            }
            searchRange(rect, node.lb, rangeQ);
            searchRange(rect, node.rt, rangeQ);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("wrong argument");
        if (isEmpty()) {
            return null;
        }
        Point2D targetPt = root.point;
        targetPt = nearestHelper(root, p, targetPt);
        return targetPt;
    }

    private Point2D nearestHelper(Node node, Point2D srcPt, Point2D targetPt) {
        if (node == null) return targetPt;

        double distSrcRect = node.rect.distanceSquaredTo(srcPt);
        double distSrcTarget = srcPt.distanceSquaredTo(targetPt);
        if (Double.compare(distSrcRect, distSrcTarget) < 0) {
            double distSrcCurr = srcPt.distanceSquaredTo(node.point);
            if (Double.compare(distSrcCurr, distSrcTarget) < 0) {
                targetPt = node.point;
            }
            int cmp = 0;
            if (node.ornt == Orientation.HORIZONTAL) {
                cmp = Double.compare(node.point.y(), srcPt.y());
            }
            else if (node.ornt == Orientation.VERTICAL) {
                cmp = Double.compare(node.point.x(), srcPt.x());
            }
            if (cmp > 0) {
                targetPt = nearestHelper(node.lb, srcPt, targetPt);
                targetPt = nearestHelper(node.rt, srcPt, targetPt);
            }
            else {
                targetPt = nearestHelper(node.rt, srcPt, targetPt);
                targetPt = nearestHelper(node.lb, srcPt, targetPt);
            }
        }
        return targetPt;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

        String filename = args[0];
        In in = new In(filename);
        ArrayList<Point2D> points = new ArrayList<>();
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            points.add(p);
        }
        StdOut.println("Size of kd tree : " + kdtree.size());
        StdOut.println("---------Insertion to kd tree complete----------");

        for (Point2D point : points) {
            StdOut.println("Search : " + point.x() + ", " + point.y());
            StdOut.println("Result : " + kdtree.contains(point));
        }
        Point2D p = new Point2D(0.498, 0.209);
        StdOut.println("Search : " + p.x() + ", " + p.y());
        StdOut.println("Result : " + kdtree.contains(p));
        StdOut.println("---------Search in kd tree complete----------");
        kdtree.draw();

        // RectHV rec = new RectHV(0.1, 0.1,
        //                      0.2, 0.4);
        Point2D p1 = new Point2D(0.20001, 0.3);
        // StdOut.println("distance from point : " + rec.distanceSquaredTo(p1));
        StdOut.println("Nearest point : " + kdtree.nearest(p1));

    }
}
