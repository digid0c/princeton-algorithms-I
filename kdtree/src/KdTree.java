import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KdTree {

    private Node root;
    private int size;

    private class Node {

        private static final boolean VERTICAL = true;
        private static final boolean HORIZONTAL = false;

        private Point2D point;
        private boolean level;
        private Node leftBottom;
        private Node rightTop;

        private Node(Point2D point, boolean level) {
            this.point = point;
            this.level = level;
        }
    }

    // construct an empty set of points
    public KdTree() {

    }

    // is the set empty?
    public boolean isEmpty() {
        return this.size == 0;
    }

    // number of points in the set
    public int size() {
        return this.size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Cannot insert null point");
        }
        if (!contains(p)) {
            root = insert(root, p, Node.VERTICAL);
            size++;
        }
    }

    private Node insert(Node node, Point2D p, boolean level) {
        if (node == null) {
            return new Node(p, level);
        }

        Comparator<Point2D> comparator = level == Node.HORIZONTAL ? Point2D.Y_ORDER : Point2D.X_ORDER;
        int cmp = comparator.compare(p, node.point);
        if (cmp < 0) {
            node.leftBottom = insert(node.leftBottom, p, !level);
        } else {
            node.rightTop = insert(node.rightTop, p, !level);
        }
        return node;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Search tree cannot contain null points");
        }

        Node node = root;
        boolean level = Node.VERTICAL;
        while (node != null) {
            if (p.compareTo(node.point) == 0) {
                return true;
            }
            Comparator<Point2D> comparator = level == Node.HORIZONTAL ? Point2D.Y_ORDER : Point2D.X_ORDER;
            int cmp = comparator.compare(p, node.point);
            if (cmp < 0) {
                node = node.leftBottom;
            } else {
                node = node.rightTop;
            }
            level = !level;
        }
        return false;
    }

    // draw all points to standard draw
    public void draw() {
        draw(root, new RectHV(0, 0, 1, 1));
    }

    private void draw(Node node, RectHV rect) {
        if (node == null) {
            return;
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.point.draw();
        StdDraw.setPenRadius(0.001);

        if (node.level == Node.VERTICAL) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.point.x(), rect.ymin(), node.point.x(), rect.ymax());
            draw(node.leftBottom, new RectHV(rect.xmin(), rect.ymin(), node.point.x(), rect.ymax()));
            draw(node.rightTop, new RectHV(node.point.x(), rect.ymin(), rect.xmax(), rect.ymax()));
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(rect.xmin(), node.point.y(), rect.xmax(), node.point.y());
            draw(node.leftBottom, new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.point.y()));
            draw(node.rightTop, new RectHV(rect.xmin(), node.point.y(), rect.xmax(), rect.ymax()));
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Rectangle cannot be null");
        }

        List<Point2D> result = new ArrayList<>();
        range(root, rect, result);
        return result;
    }

    private void range(Node node, RectHV rect, List<Point2D> pointsInRange) {
        if (node == null) {
            return;
        }
        Point2D point = node.point;
        if (rect.contains(point)) {
            pointsInRange.add(point);
        }

        if (node.level == Node.VERTICAL) {
            if (rect.xmin() <= point.x() && point.x() <= rect.xmax()) {
                range(node.leftBottom, rect, pointsInRange);
                range(node.rightTop, rect, pointsInRange);
            } else if (rect.xmax() < point.x()) {
                range(node.leftBottom, rect, pointsInRange);
            } else {
                range(node.rightTop, rect, pointsInRange);
            }
        } else {
            if (rect.ymin() <= point.y() && point.y() <= rect.ymax()) {
                range(node.leftBottom, rect, pointsInRange);
                range(node.rightTop, rect, pointsInRange);
            } else if (rect.ymax() < point.y()) {
                range(node.leftBottom, rect, pointsInRange);
            } else {
                range(node.rightTop, rect, pointsInRange);
            }
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Nearest point cannot be null");
        }
        if (isEmpty()) {
            return null;
        }
        return nearest(root, p, root.point);
    }

    private Point2D nearest(Node node, Point2D point, Point2D champion) {
        if (node == null) {
            return champion;
        }
        Point2D currentPoint = node.point;
        boolean isNewChampion = currentPoint.distanceTo(point) < champion.distanceTo(point);

        if (node.level == Node.VERTICAL) {
            if (point.x() > currentPoint.x()) {
                Point2D right = nearest(node.rightTop, point, isNewChampion ? currentPoint : champion);
                if (right.distanceTo(point) > Math.abs(currentPoint.x() - point.x())) {
                    Point2D left = nearest(node.leftBottom, point, right);
                    return right.distanceTo(point) > left.distanceTo(point) ? left : right;
                } else {
                    return right;
                }
            } else {
                Point2D left = nearest(node.leftBottom, point, isNewChampion ? currentPoint : champion);
                if (left.distanceTo(point) > Math.abs(currentPoint.x() - point.x())) {
                    Point2D right = nearest(node.rightTop, point, left);
                    return right.distanceTo(point) > left.distanceTo(point) ? left : right;
                } else {
                    return left;
                }
            }
        } else {
            if (point.y() > currentPoint.y()) {
                Point2D top = nearest(node.rightTop, point, isNewChampion ? currentPoint : champion);
                if (top.distanceTo(point) > Math.abs(currentPoint.y() - point.y())) {
                    Point2D bottom = nearest(node.leftBottom, point, top);
                    return top.distanceTo(point) > bottom.distanceTo(point) ? bottom : top;
                } else {
                    return top;
                }
            } else {
                Point2D bottom = nearest(node.leftBottom, point, isNewChampion ? currentPoint : champion);
                if (bottom.distanceTo(point) > Math.abs(currentPoint.y() - point.y())) {
                    Point2D top = nearest(node.rightTop, point, bottom);
                    return top.distanceTo(point) > bottom.distanceTo(point) ? bottom : top;
                } else {
                    return bottom;
                }
            }
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree kdTree = new KdTree();

        kdTree.insert(new Point2D(0.0, 0.0));
        kdTree.insert(new Point2D(0.5, 0.625));
        kdTree.insert(new Point2D(0.875, 1.0));
        kdTree.insert(new Point2D(0.25, 0.375));
        kdTree.insert(new Point2D(0.75, 0.75));

        System.out.println(kdTree.isEmpty());
        System.out.println(kdTree.size());

        System.out.println(kdTree.contains(new Point2D(0.5, 1.0)));
        System.out.println(kdTree.contains(new Point2D(0.4, 0.4)));
        System.out.println(kdTree.contains(new Point2D(1.0, 0.0)));

        System.out.println(kdTree.range(new RectHV(0.25, 0.75, 0.75, 1.0)));
        System.out.println(kdTree.nearest(new Point2D(0.625, 0.875)));
    }
}