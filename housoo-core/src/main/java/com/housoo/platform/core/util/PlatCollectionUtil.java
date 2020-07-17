package com.housoo.platform.core.util;

import com.alibaba.fastjson.JSONArray;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 描述 封装对集合操作的工具类
 *
 * @author
 * @created 2017年2月2日 下午8:01:39
 */
public class PlatCollectionUtil {
    /**
     * 描述 对数组进行升序排
     *
     * @param arr
     * @return
     */
    public static int[] sortByAsc(int[] arr) {
        Arrays.sort(arr);
        return arr;
    }

    /**
     * 描述 对数组进行降序排
     *
     * @param arr
     * @return
     */
    public static int[] sortByDesc(int[] arr) {
        Arrays.sort(arr);
        int[] nums = new int[arr.length];
        int j = 0;
        for (int i = arr.length - 1; i >= 0; i--) {
            nums[j] = arr[i];
            j++;
        }
        return nums;
    }

    /**
     * 多个集合取交集
     *
     * @param lists
     * @return intersection
     */
    public static List<Map<String, Object>> intersection(List<List<Map<String, Object>>> lists) {
        if (lists == null || lists.size() == 0) {
            return null;
        }
        List<List<Map<String, Object>>> arrayList = new ArrayList<>(lists);
        for (int i = 0; i < arrayList.size(); i++) {
            List<Map<String, Object>> list = arrayList.get(i);
            // 去除空集合
            if (list == null || list.size() == 0) {
                arrayList.remove(list);
                i--;
            }
        }
        if (arrayList.size() == 0) {
            return null;
        }
        List<Map<String, Object>> intersection = arrayList.get(0);
        // 就只有一个非空集合，结果就是他
        if (arrayList.size() == 1) {
            return intersection;
        }
        // 有多个非空集合，直接挨个交集
        for (int i = 1; i < arrayList.size() - 1; i++) {
            intersection.retainAll(arrayList.get(i));
        }
        return intersection;
    }

    /**
     * 多个集合取并集
     *
     * @param lists
     * @return intersection
     */
    public static Set<Map<String, Object>> unionSet(List<List<Map<String, Object>>> lists) {
        if (lists == null || lists.size() == 0) {
            return null;
        }
        List<List<Map<String, Object>>> arrayList = new ArrayList<>(lists);
        for (int i = 0; i < arrayList.size(); i++) {
            List<Map<String, Object>> list = arrayList.get(i);
            // 去除空集合
            if (list == null || list.size() == 0) {
                arrayList.remove(list);
                i--;
            }
        }
        if (arrayList.size() == 0) {
            return null;
        }
        Set<Map<String, Object>> intersection = new HashSet<>(arrayList.get(0));
        // 就只有一个非空集合，结果就是他
        if (arrayList.size() == 1) {
            return intersection;
        }
        // 有多个非空集合，直接挨个交集
        for (int i = 1; i < arrayList.size() - 1; i++) {
            intersection.addAll(arrayList.get(i));
        }
        return intersection;
    }

    /**
     * 查找相同元素
     *
     * @param ccIdArray
     * @param list
     * @return
     */
    public static List<Map<String, Object>> findSameElements(JSONArray ccIdArray, List<Map<String, Object>> list) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (ccIdArray.size() > 0 && list != null && list.size() > 0) {
            if (ccIdArray.size() > 1) {
                for (int i = 0; i < list.size(); i++) {
                    int sameConut = 0;
                    Map<String, Object> temp = list.get(i);
                    for (int j = list.size() - 1; j > i; j--) {
                        if (list.get(i).get("RESID").equals(list.get(j).get("RESID"))) {
                            List<Map<String, Object>> secondResList = (List<Map<String, Object>>) list.get(i).get("childResList");
                            List<Map<String, Object>> secondResList1 = (List<Map<String, Object>>) list.get(j).get("childResList");
                            secondResList.retainAll(secondResList1);
                            temp.put("childResList", secondResList);
                            sameConut++;
                        }
                    }
                    if (sameConut == ccIdArray.size() - 1) {
                        result.add(temp);
                    }
                }
            } else {
                return list.stream().distinct().collect(Collectors.toList());
            }

        }
        return result.stream().distinct().collect(Collectors.toList());
    }

}
