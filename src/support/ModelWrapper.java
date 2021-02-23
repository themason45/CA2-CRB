package support;

import models.BaseModel;

import java.util.ArrayList;

/**
 * Wrap a model with a set of general functions which allow interactions with arrays
 *
 * @param <T> The type of Model we are wrapping
 */
public class ModelWrapper<T extends BaseModel> {

    /**
     * @param arr Input array, to search through
     * @param pk The desired pk
     * @return True if no object exists with that pk
     */
    public Boolean isUniquePk(ArrayList<T> arr, int pk) {
        return arr.stream().noneMatch(x -> x.pk == pk);
    }

    /**
     * @param arr Input array, to search through
     * @param pk The desired pk
     * @return The object in that array with the given pk, or null
     */
    public T findByPk(ArrayList<T> arr, int pk) {
        return arr.stream().filter(x -> x.pk == pk).findFirst().orElse(null);
    }

    /**
     * @param arr Input array, to find the last element of
     * @return The pk of the last element in the given arraylist input
     */
    public int lastPk(ArrayList<T>  arr) {
        if (arr.size() == 0) return 0;

        arr.sort((o1, o2) -> o1.pk > o2.pk ? 1 : 0);  // Sort by pk first
        return arr.get(arr.size() - 1).pk;
    }


    /**
     * @param arr Input array, to find the last element of
     * @return The next possible pk for the given ArrayList
     */
    public int nextPk(ArrayList<T> arr) {
        return lastPk(arr) + 1;
    }


    /**
     * Assuming the pk stays the same, then update the value with that pk
     * @param arr The input arraylist
     * @param newValue The new value to update the arraylist value with
     */
    public ArrayList<T> updateArr(ArrayList<T> arr, T newValue) {
        int i = 0;
        for (T obj : arr) {
            if (obj.pk == newValue.pk) {
                arr.set(i, newValue);
            }
            i++;
        }
        return arr;
    }
}
