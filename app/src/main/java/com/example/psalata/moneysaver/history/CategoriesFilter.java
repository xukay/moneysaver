package com.example.psalata.moneysaver.history;

import com.example.psalata.moneysaver.database.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Paweł on 07.07.2016.
 */
public class CategoriesFilter implements FilterOperations {

    private Map<String, Boolean> categoriesMap = new HashMap<>();

    public CategoriesFilter(List<String> allCategoriesList) {
        for (String categoryName : allCategoriesList) {
            categoriesMap.put(categoryName, true);
        }
    }

    public void put(String categoryName, boolean isEnabled) {
        categoriesMap.put(categoryName, isEnabled);
    }

    public boolean isEmpty() {
        return categoriesMap.isEmpty();
    }

    @Override
    public String getDBQueryPart() {
        StringBuilder categoriesQueryPart = new StringBuilder();
        List<String> enabledCategories = getListOfEnabledCategories();
        if (!enabledCategories.isEmpty()) {

            for (String categoryName : enabledCategories) {
                if (!categoryName.equals(enabledCategories.get(0))) {
                    categoriesQueryPart.append(" OR ");
                }
                categoriesQueryPart.append(DBHelper.KEY_CATEGORY + " = '" + categoryName + "'");
            }

            categoriesQueryPart.append(")");
        }
        return categoriesQueryPart.toString();
    }

    public Map<String, Boolean> getCategoriesMap() {
        return categoriesMap;
    }

    private List<String> getListOfEnabledCategories() {
        List<String> enabledCategories = new ArrayList<>();
        for (Map.Entry<String, Boolean> item : categoriesMap.entrySet()) {
            if ( item.getValue()) { //if category is enabled
                enabledCategories.add(item.getKey());
            }
        }
        return enabledCategories;
    }
}
