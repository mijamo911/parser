package parser;

import java.util.Map;

public interface AutomatonBuilder {
    public Nfa buildAutomaton();
    public Map<StationKey,Station> getStations();
}
