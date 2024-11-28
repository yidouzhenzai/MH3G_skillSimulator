package skill_simulation;


import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Faster {
	private static Object[][] sword = MakeInputScreen.sword;
	private static Object[][] ganner = MakeInputScreen.ganner;
	private static Object[][] ornament = MakeInputScreen.ornament;
	private static Object[][] pro_table;
	private static ArrayList<Integer> skill = new ArrayList<>();
	public static ArrayList<Map.Entry<Integer, Integer>> keitouList = new ArrayList<>();
	private static ArrayList<Map.Entry<Integer, Double>> skill_val = new ArrayList<>();
	
	public Faster (ArrayList<Map.Entry<Integer, Integer>> keitouList, Object[][] pro_table) {
		
		this.pro_table = pro_table;
		
		// Mapを使ってkeitouListの重複を解決する
        Map<Integer, Integer> map = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : keitouList) {
            int key = entry.getKey();
            int value = entry.getValue();
            // 重複があれば右の値が大きい方を保持
            map.put(key, Math.max(map.getOrDefault(key, Integer.MIN_VALUE), value));
        }
        // Mapをリストに変換する
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            this.keitouList.add(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()));
        }
        //スキルインデックスのみのリストskillを生成
        for (Map.Entry<Integer, Integer> entry : this.keitouList) {
            skill.add(entry.getKey());
        }
		
    	//skillの重複をなくす
    	Set<Integer> uniqueSet2 = new HashSet<>(this.skill);
        this.skill = new ArrayList<>(uniqueSet2);
        
        //skill_valを計算
        skill_val = calVal(this.skill);
	}
	
	//防具のインデックスリストから相当スロット数3以下のものを除く
    public ArrayList<Map.Entry<Integer, Double>> getBetter(ArrayList<Integer> name) {
    	//nameの重複をなくす
    	Set<Integer> uniqueSet = new HashSet<>(name);
        name = new ArrayList<>(uniqueSet);
        
        //nameの各防具あたりの相当スロット数valを計算し，armor_val=(防具インデックス, val)のリスト作成
    	ArrayList<Map.Entry<Integer, Double>> armor_val = new ArrayList<>();
    	for (Integer entry : name) {
    		Double val = 0.;
    		for (Map.Entry<Integer, Double> entry2 : skill_val) {
    			val = val + (double) (int) pro_table[entry][entry2.getKey()] * entry2.getValue();
    		}
    		val = val + (double) (int) pro_table[entry][1];
    		armor_val.add(new AbstractMap.SimpleEntry<>(entry, val));
        }
    	
    	//相当スロット数が3以下の防具を除いてresultを得る
    	ArrayList<Map.Entry<Integer, Double>> result = new ArrayList<>();
    	for (Map.Entry<Integer, Double> entry : armor_val) {
			if (entry.getValue() > 3.) {
				result.add(entry);
				}
			}
    	// 降順でソート
        result.sort((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()));
        
        return result;
    }
    
    
    //skillの各スキルポイント1あたりのスロット数valを計算し,　(スキルインデックス, val)のリストskill_valを作成
    public ArrayList<Map.Entry<Integer, Double>> calVal(ArrayList<Integer> skill) {
    	ArrayList<Map.Entry<Integer, Double>> vals = new ArrayList<>();
    	ArrayList<Map.Entry<Integer, Double>> result = new ArrayList<>();
    	for(Integer i : skill) {
    		Double val = 0.;
    		if(ornament[i-1][9] != null) {
    			val = 3/((double) (int) ornament[i-1][10]);
    			vals.add(new AbstractMap.SimpleEntry<>(i, val));
    		}
    		else if(ornament[i-1][5] != null) {
    			val = 2/((double) (int) ornament[i-1][6]);
    			vals.add(new AbstractMap.SimpleEntry<>(i, val));
    		}
    		else if(ornament[i-1][1] != null) {
    			val = 1/((double) (int) ornament[i-1][2]);
    			vals.add(new AbstractMap.SimpleEntry<>(i, val));
    		}
    		else {
    			val = 10.;
    			vals.add(new AbstractMap.SimpleEntry<>(i, val));
    		}
    	}
    	return vals;
    }
    
    //選択したスキルが多すぎる場合はfalse
    public Boolean judgeUnable(ArrayList<Map.Entry<Integer, Double>> armorVal, Integer[][] omamori, Integer slot_wepon, Integer slot_oma) {
    	Double needSlot = 0.;
    	Double maxVal = (double) slot_wepon + (double) slot_oma;
    	
    	//選択スキルの必要合計相当スロット数を計算
    	for (int i=0; i < skill_val.size(); i++) {
    		if (keitouList.get(i).getKey() == omamori[0][0]) {
    			needSlot = needSlot + skill_val.get(i).getValue() * Math.max(keitouList.get(i).getValue() - omamori[0][1], 0);
    		} else if (keitouList.get(i).getKey() == omamori[1][0]) {
    			needSlot = needSlot + skill_val.get(i).getValue() * Math.max(keitouList.get(i).getValue() - omamori[1][1], 0);
    		} else {
    			needSlot = needSlot + skill_val.get(i).getValue() * keitouList.get(i).getValue();
    		}
    	}
    	//防具の最大合計相当スロット数を計算
    	for (Map.Entry<Integer, Double> entry : armorVal) {
    		maxVal = maxVal + entry.getValue();
    	}
    	return (maxVal >= needSlot);
    }
}
