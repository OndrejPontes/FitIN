package com.pv239.fitin.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This singleton class is used to handle data sending between various part of application.
 * Currently it is possible to save any Object or List of Objects. Objects are saved by type,
 * which means that saving two users will result in only saving the latter, because it will
 * replace the first saved one. To get Object, type is used as well (see getObject/getListObject).
 * When deleting, specify type as well.
 *
 * TODO: maybe use generics to be more safe, but probably too much overhead for 4 types... (generic getInstance() for each type)
 */
public class DataManager {

    private static DataManager dataManager = null;

    private static Map<String, Object> singleObjects;

    private static Map<String, List<Object>> listObjects;

    private DataManager() {
    }

    /**
     * get instance of Data Manager (singleton)
     * @return instance to Data Manager shared through entire app
     */
    public static DataManager getInstance() {
        if (dataManager == null) {
            dataManager = new DataManager();
            singleObjects = new HashMap<>();
            listObjects = new HashMap<>();
        }
        return dataManager;
    }

    /**
     * Saves Object for later retrieval. Object's key is used and if there
     * is another Object with given key saved, it will be replaced.
     * @param key key of object to be saved with
     * @param object object to save
     */
    public void putObject(String key, Object object) {
        singleObjects.put(key, object);
    }

    /**
     * Gets Object by its key in Map, or null if there is no Object with given key saved.
     * @param key class of Object we wish to retrieve
     * @return saved object with given key, or null is there is no such object saved.
     */
    public Object getObject(String key) {
        return singleObjects.get(key);
    }

    /**
     * Removed Object by its key.
     * @param key key of Object we wish to remove
     */
    public void removeObject(String key) {
        singleObjects.remove(key);
    }

    /**
     * Saves List for later retrieval. List's key is used and if there
     * is another List with given key saved, it will be replaced.
     * @param key key of object to be saved with
     * @param objectList list to save
     */
    public void putListObject(String key, List<Object> objectList) {
        listObjects.put(key, objectList);
    }

    /**
     * Gets List&lt;Object&gt; by its key (class), or null if there is no List with given key saved.
     * @param key key of List we wish to retrieve
     * @return saved list with given key, or empty list is there is no such list saved.
     */
    public List<Object> getListObject(String key) {
        return listObjects.get(key) == null ? new ArrayList<>() : listObjects.get(key);
    }

    /**
     * Removed List by its key.
     * @param key key of List we wish to remove
     */
    public void removeListObject(String key) {
        listObjects.remove(key);
    }
}
