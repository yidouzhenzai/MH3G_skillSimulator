package skill_simulation;

import java.util.ArrayList;

public class CustomSlot {
    public Boolean available;
    public ArrayList<Integer[]> slot_dou;
    public Integer[] margin;

    // コンストラクタ
    public CustomSlot(
    		Boolean available,
    		ArrayList<Integer[]> slot_dou,
    		Integer[] margin) {
        this.available = available;
        this.slot_dou = slot_dou;
        this.margin = margin;
    }
}
