package commons.listening;


import java.util.Map;

public interface GateListener {
    public void updateOnExecution(boolean open,int id, Map<Object,Object> parameters);
}
