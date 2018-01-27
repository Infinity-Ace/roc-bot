package jn.rocbot.missions;

abstract class Mission {
    int id;
    Type type;

    Mission(int id, Type type){
        this.id = id;
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("%s #%d", type.name, id);
    }

    enum Type {
        DAILY("Daily mission"),
        COMMUNITY("Community mission");
        
        String name;
        Type(String name) {
            this.name = name;
        }
    }

    enum Stage {
        // Zhey Auna, Arcology CQ-6, Prometheus Array, Ceres Major, Gau Prime
        // Credits to loki for the pattern

        Zhey_Auna,
        Arcology_CQ_6("Arcology_CQ-6"),
        Prometheus_Array,
        Ceres_Major,
        Gau_Prime;

        String name;
        Stage() {
            name = toString().replace("_", " ");
        }

        Stage(String name) {
            this.name = name.replace("_", " ");
        }
    }
}
