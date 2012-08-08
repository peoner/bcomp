package commons.listening;


import java.util.Map;

public interface DataListener {
    void updateOnChanging(int newValue, int id, Map<Object,Object> parameters);
}
