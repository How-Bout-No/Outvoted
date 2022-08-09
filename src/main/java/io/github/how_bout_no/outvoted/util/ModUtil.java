package io.github.how_bout_no.outvoted.util;

public final class ModUtil {
    public static int[] toIntArray(float[] arr) {
        if (arr == null) return null;
        int n = arr.length;
        int[] ret = new int[n];
        for (int i = 0; i < n; i++) {
            ret[i] = (int) arr[i];
        }
        return ret;
    }

    public static float[] toFloatArray(int[] arr) {
        if (arr == null) return null;
        int n = arr.length;
        float[] ret = new float[n];
        for (int i = 0; i < n; i++) {
            ret[i] = (float) arr[i];
        }
        return ret;
    }
}
