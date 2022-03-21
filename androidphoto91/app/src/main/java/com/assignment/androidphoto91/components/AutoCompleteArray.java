package com.assignment.androidphoto91.components;

import java.util.ArrayList;

public class AutoCompleteArray {
    public class AutoCompleteElement {
        private String name;
        private int count;

        AutoCompleteElement(String name) {
            this.name = name.trim();
            this.count++;
        }

        public int getCount() {
            return this.count;
        }

        public String getName() {
            return this.name;
        }

        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public boolean equals(Object obj) {
            boolean result = false;

            if (obj != null && obj instanceof AutoCompleteElement) {
                AutoCompleteElement o = (AutoCompleteElement) obj;
                result = this.name.equalsIgnoreCase(o.getName());
            }

            return result;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    private ArrayList<AutoCompleteElement> list;

    public AutoCompleteArray() {
        list = new ArrayList<>();
    }

    public void add(String name) {
        AutoCompleteElement o = new AutoCompleteElement(name);

        if (o != null) {
            for (int i = 0; i < list.size(); i++) {
                AutoCompleteElement element = list.get(i);

                if (element.equals(o)) {
                    int count = element.getCount() + 1;
                    element.setCount(count);
                    return;
                }
            }
            list.add(o);
        }
    }

    public void remove(String name) {
        AutoCompleteElement o = new AutoCompleteElement(name);

        if (o != null) {
            for (int i = 0; i < list.size(); i++) {
                AutoCompleteElement element = list.get(i);
                if (element.equals(o)) {
                    int count = element.getCount();

                    if (count > 0) {
                        count = count - 1;
                        element.setCount(count);
                    }

                    if (count == 0) {
                        list.remove(i);
                    }

                    return;
                }
            }
        }
    }

    public ArrayList<AutoCompleteElement> getArrayList() {
        return list;
    }

    public ArrayList<String> toStringArray() {
        ArrayList<String> array = new ArrayList<>();
        if (array != null) {
            for (AutoCompleteElement e : list) {
                array.add(e.getName());
            }
        }

        return array;
    }
}