package jn.rocbot.missions;


public class DailyMission extends Mission{
    public DailyMission(int id) {
        super(id, Type.DAILY);
    }

    /*
    public static Cycle current() {
        // Read from some file
    }
    */

    class Cycle {
        private final Stage[] stages;
        private int at;
        Cycle(int at, Stage... stages) {
            assert at >= 0 && at < stages.length;

            this.at = at;
            this.stages = stages;
        }

        public Stage current(){
            return stages[at];
        }

        public Stage next(){
            if(at < stages.length) at++;
            else at = 0;

            return stages[at];
        }
    }
}
