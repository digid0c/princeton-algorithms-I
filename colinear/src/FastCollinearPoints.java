import edu.princeton.cs.algs4.Merge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {

    private List<LineSegment> lineSegments = new ArrayList<>();
    private Point[] pointsCopy;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Points array must be defined");
        }

        checkNullPoints(points);
        checkRepeatedPoints(points);
        pointsCopy = Arrays.copyOf(points, points.length);
        if (pointsCopy.length < 4) {
            return;
        }
        Merge.sort(pointsCopy);

        for (int i = 0; i < pointsCopy.length; i++) {
            Point p = pointsCopy[i];
            Point[] slopes = Arrays.copyOf(pointsCopy, pointsCopy.length);
            Arrays.sort(slopes, p.slopeOrder());

            double slope = p.slopeTo(slopes[1]);
            List<Point> lineSegmentPoints = new ArrayList<>();
            lineSegmentPoints.add(p);
            for (int k = 1; k < slopes.length; k++) {
                double currentSlope = p.slopeTo(slopes[k]);
                if (currentSlope == slope) {
                    lineSegmentPoints.add(slopes[k]);
                } else {
                    slope = currentSlope;
                    if (lineSegmentPoints.size() >= 4) {
                        addLineSegment(lineSegmentPoints.toArray(new Point[0]));
                    }
                    lineSegmentPoints.clear();
                    lineSegmentPoints.add(p);
                    lineSegmentPoints.add(slopes[k]);
                }
            }

            if (lineSegmentPoints.size() >= 4) {
                addLineSegment(lineSegmentPoints.toArray(new Point[0]));
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.size();
    }

    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[0]);
    }

    private void checkNullPoints(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Point cannot be null");
            }
        }
    }

    private void checkRepeatedPoints(Point[] points) {
        for (int i = 0; i < points.length - 1; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException("Points array contains repeated point");
                }
            }
        }
    }

    private void addLineSegment(Point... points) {
        Point pivot = points[0];
        Merge.sort(points);
        if (points[0].compareTo(pivot) == 0) {
            lineSegments.add(new LineSegment(points[0], points[points.length - 1]));
        }
    }
}
