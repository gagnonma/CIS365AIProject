package sample;

public class Model {
    enum SelectMode {
            WALLUP, WALLDOWN, WALLRIGHT, WALLLEFT, START, GOAL, CLEAR
    }

    private SelectMode currentMode;

    /**
     *
     */


    public Model () {
        setCurrentMode(SelectMode.CLEAR);
    }

    public SelectMode getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(SelectMode currentMode) {
        this.currentMode = currentMode;
    }
}
