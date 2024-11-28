package skill_simulation;

import java.util.ArrayList;
import java.util.Map;

public class CustomResult {
    public Integer[] bouguComb; // サイズ5
    public ArrayList<Map.Entry<Integer, Integer[]>> entry;
    public Integer[] margin; // サイズ3
    public ArrayList<Map.Entry<Integer, Integer>> hatudouSkill;
    public ArrayList<Integer[]> slotDou;

    // コンストラクタ
    public CustomResult(
    		Integer[] first,
    		ArrayList<Map.Entry<Integer, Integer[]>> second,
    		Integer[] third,
    		ArrayList<Map.Entry<Integer, Integer>> forth,
    		ArrayList<Integer[]> fifth) {
        this.bouguComb = first;
        this.entry = second;
        this.margin = third;
        this.hatudouSkill = forth;
        this.slotDou = fifth;
    }
}
