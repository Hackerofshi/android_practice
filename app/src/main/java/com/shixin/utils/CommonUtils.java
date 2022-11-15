package com.shixin.utils;

import java.util.List;
import java.util.Objects;

public class CommonUtils {

    /**
     * 判断点是否在多边形内
     *
     * @param point 测试点
     * @param pts   多边形的点
     * @return boolean
     */
    public static boolean isInPolygon(Point point, List<Point> pts) {

        int N = pts.size();
        boolean boundOrVertex = true;
        int intersectCount = 0;//交叉点数量
        double precision = 2e-10; //浮点类型计算时候与0比较时候的容差
        Point p1, p2;//临近顶点

        p1 = pts.get(0);
        for (int i = 1; i <= N; ++i) {
            if (point.equals(p1)) {
                return boundOrVertex;
            }

            p2 = pts.get(i % N);
            if (point.x < Math.min(p1.x, p2.x) || point.x > Math.max(p1.x, p2.x)) {
                p1 = p2;
                continue;
            }

            //射线穿过算法
            if (point.x > Math.min(p1.x, p2.x) && point.x < Math.max(p1.x, p2.x)) {
                if (point.y <= Math.max(p1.y, p2.y)) {
                    if (p1.x == p2.x && point.y >= Math.min(p1.y, p2.y)) {
                        return boundOrVertex;
                    }

                    if (p1.y == p2.y) {
                        if (p1.y == point.y) {
                            return boundOrVertex;
                        } else {
                            ++intersectCount;
                        }
                    } else {
                        double xinters = (point.x - p1.x) * (p2.y - p1.y) / (p2.x - p1.x) + p1.y;
                        if (Math.abs(point.y - xinters) < precision) {
                            return boundOrVertex;
                        }

                        if (point.y < xinters) {
                            ++intersectCount;
                        }
                    }
                }
            } else {
                if (point.x == p2.x && point.y <= p2.y) {
                    Point p3 = pts.get((i + 1) % N);
                    if (point.x >= Math.min(p1.x, p3.x) && point.x <= Math.max(p1.x, p3.x)) {
                        ++intersectCount;
                    } else {
                        intersectCount += 2;
                    }
                }
            }
            p1 = p2;
        }

        //偶数在多边形外 奇数在多边形内
        return intersectCount % 2 != 0;
    }

   public static class Point {
        public double x;
        public double y;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return Double.compare(point.x, x) == 0 && Double.compare(point.y, y) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
