package model;

import java.io.Serializable;

public class DataSet implements Serializable, Comparable<DataSet> {

    private String title;
    private String result;

    public DataSet() {
        super();
    }

    public DataSet(String title, String result) {

        this.title = title;
        this.result = result;

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public int compareTo(DataSet o) {
        return o.title.compareTo(this.title);
    }
}
