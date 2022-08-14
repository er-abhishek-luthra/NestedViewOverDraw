package com.example.nestedviewoverdraw.nestedviewhierarchy.data;

import java.util.ArrayList;

public class LocalDataSource {

    ArrayList<String> orginalDataSet = null;
    ArrayList<String> itemChangedDataSet = null;

    public LocalDataSource() {
        if (orginalDataSet == null) {
            orginalDataSet = getMessages();
        }
        if (itemChangedDataSet == null) {
            itemChangedDataSet = getItemChangedDataSet("Item Changed ", 1);
        }
        orginalDataSet = getMessages();
    }

    public ArrayList<String> getMessages() {
        if(orginalDataSet == null){
            ArrayList<String> dataSet = new ArrayList<>();
            for (int index = 0; index < 1000; index++) {
                dataSet.add("Orignal String");
            }
            orginalDataSet= dataSet;
        }
        return orginalDataSet;

    }

    public ArrayList<String> getItemChangedDataSet(String prefix, int index) {
        if(itemChangedDataSet != null){
            return itemChangedDataSet;
        }
        ArrayList<String> dataSet = new ArrayList<>(getMessages());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(prefix).append(" : ").append(index);
        dataSet.set(index, stringBuilder.toString());
        stringBuilder = null;
        itemChangedDataSet = dataSet;
        return itemChangedDataSet;
    }



}
