package model;

import java.util.ArrayList;
import java.util.List;

public class ObjectForMessage {
    private List<String> data;

//    public ObjectForMessage() {
//        data = new ArrayList<>();
//    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public ObjectForMessage getCopy() {
        var result = new ObjectForMessage();
        result.setData(new ArrayList<>(data));
        return result;
    }

    @Override
    public String toString() {
        return "ObjectForMessage{" +
                "data=" + data +
                '}';
    }
}
