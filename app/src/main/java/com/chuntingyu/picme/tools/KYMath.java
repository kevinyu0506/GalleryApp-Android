package com.chuntingyu.picme.tools;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.chuntingyu.picme.applications.PaintApp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KYMath {
    private static final float DEFAULT_INTERVAL_DEGREE = 2.5f;
    private static final int DEFAULT_INTERVAL_MINS = 5;

    public static Point screenSize() {
        WindowManager wm = (WindowManager) PaintApp.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);
        return screenSize;
    }

    public static double mod(double x, int y) {
        return ((x % y) + y) % y;
    }

    public static int mod(int x, int y) {
        return ((x % y) + y) % y;
    }

    public static int to360Degree(double angle, float dx, float dy) {
        int now_angle = (int) Math.toDegrees(angle);
        if (dx < 0) now_angle += Math.toDegrees(Math.PI);
        if (dx > 0 && dy < 0) now_angle += Math.toDegrees(2 * Math.PI);
        return (now_angle % 360 + 360) % 360;
    }

    public static Calendar angleToCalendar(int angle, boolean isAm) {
        Calendar cal = Calendar.getInstance();
        int mins = angle2Minutes(angle);
        cal.set(Calendar.HOUR, mins / 60);
        cal.set(Calendar.MINUTE, mins % 60);
        cal.set(Calendar.AM_PM, isAm ? Calendar.AM : Calendar.PM);
        return cal;
    }

    public static int angle2Minutes(int angle) {
        return DEFAULT_INTERVAL_MINS * Math.round(angle / DEFAULT_INTERVAL_DEGREE);
    }

    public static int mins2Angle(int mins) {
        return Math.round(DEFAULT_INTERVAL_DEGREE * ((float) mins / DEFAULT_INTERVAL_MINS));
    }

    public static int angleDiff(int s, int e) {
        return KYMath.mod((e - s), 360);
    }

    public static double distance(float aX, float aY, float bX, float bY) {
        return Math.sqrt(Math.pow(aX - bX, 2) + Math.pow(aY - bY, 2));
    }

    private static int dfs(int node, List<List<Integer>> graph, boolean[] visited) {
        if (visited[node]) {
            return 0;
        }
        int count = 1;
        visited[node] = true;
        for (int next : graph.get(node)) {
            count += dfs(next, graph, visited);
        }
        return count;
    }

    public static <E> void topologicalSorting(final List<E> list, Comparator<E> comparator) {
        List<List<Integer>> adjacentLists = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); ++i) {
            adjacentLists.add(new ArrayList<Integer>());
            for (int j = 0; j < list.size(); ++j) {
                if (i != j && comparator.compare(list.get(i), list.get(j)) > 0) {
                    adjacentLists.get(i).add(j);
                }
            }
        }
        final Map<E, Integer> visitedCount = new HashMap<>();
        for (int i = 0; i < list.size(); ++i) {
            boolean[] visited = new boolean[list.size()];
            visitedCount.put(list.get(i), dfs(i, adjacentLists, visited));
        }
        Collections.sort(list, new Comparator<E>() {

            @Override
            public int compare(E o1, E o2) {
                int a = visitedCount.get(o1);
                int b = visitedCount.get(o2);
                if (a > b) {
                    return 1;
                } else if (a < b) {
                    return -1;
                } else {
                    return 0;
                }

            }
        });
    }

    /**
     * Covert dp to px
     *
     * @param dp
     * @param context
     * @return pixel
     */
    public static float convertDpToPixel(float dp, Context context) {
        float px = dp * getDensity(context);
        return px;
    }

    /**
     * Covert px to dp
     *
     * @param px
     * @param context
     * @return dp
     */
    public static float convertPixelToDp(float px, Context context) {
        float dp = px / getDensity(context);
        return dp;
    }

    /**
     * 取得螢幕密度
     * 120dpi = 0.75
     * 160dpi = 1 (default)
     * 240dpi = 1.5
     *
     * @param context
     * @return
     */
    public static float getDensity(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.density;
    }
}
