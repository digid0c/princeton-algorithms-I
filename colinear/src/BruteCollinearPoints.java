import edu.princeton.cs.algs4.Merge;

import java.util.ArrayList;
import java.util.List;

public class BruteCollinearPoints {

    private List<LineSegment> lineSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Points array must be defined");
        }

        checkNullPoints(points);
        checkRepeatedPoints(points);
        lineSegments = new ArrayList<>();

        for (int p = 0; p < points.length - 3; p++) {
            for (int q = p + 1; q < points.length - 2; q++) {
                double pqSlope = points[p].slopeTo(points[q]);
                for (int r = q + 1; r < points.length - 1; r++) {
                    double prSlope = points[p].slopeTo(points[r]);
                    if (pqSlope == prSlope) {
                        for (int s = r + 1; s < points.length; s++) {
                            double psSlope = points[p].slopeTo(points[s]);
                            if (prSlope == psSlope) {
                                addLineSegment(points[p], points[q], points[r], points[s]);
                            }
                        }
                    }
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.size();
    }

    // the line segments
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

    private void addLineSegment(Point p, Point q, Point r, Point s) {
        Point[] lineSegmentPoints = {p, q, r, s};
        Merge.sort(lineSegmentPoints);
        lineSegments.add(new LineSegment(lineSegmentPoints[0], lineSegmentPoints[3]));
    }
}