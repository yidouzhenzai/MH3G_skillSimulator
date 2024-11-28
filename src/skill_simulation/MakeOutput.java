package skill_simulation;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;


//import org.bouncycastle.util.Arrays;

public class MakeOutput {
    // フィールド宣言
	private static Object[][] sword = MakeInputScreen.sword;
	private static Object[][] ganner = MakeInputScreen.ganner;
	private static Object[][] ornament = MakeInputScreen.ornament;
	private static Object[][] skill = MakeInputScreen.skill;
	private static Object[][] pro_table;
	
	
    public static Map.Entry<Integer, String[]> getOptions(
    		//引数
    		String profession,
    		String gender,
    		Integer slot_wepon,
    		Integer slot_oma,
    		String oma1,
    		Integer oma_p1,
    		String oma2,
    		Integer oma_p2,
    		String[] selected
    		) {
    	
    	//変数宣言
    	Object[][] keitou = new Object[selected.length][2];
    	ArrayList<Integer> head = new ArrayList<>();
    	ArrayList<Integer> chest = new ArrayList<>();
    	ArrayList<Integer> arm = new ArrayList<>();
    	ArrayList<Integer> waist = new ArrayList<>();
    	ArrayList<Integer> leg = new ArrayList<>();
    	ArrayList<Integer> skillIndex = new ArrayList<Integer>();
    	
    	//professionによって参照する防具テーブルを設定
    	if (profession == "剣士") {
    		pro_table = sword;
    	} else {
    		pro_table = ganner;
    	}
    	//selectedから選ばれたスキル系統と必要なポイントの2次元配列keitouを得る
        for (int i = 0; i < selected.length; i++) {
        	for (int j = 0; j < skill.length; j++) {
        		if(selected[i].equals((String) skill[j][0])) {
        			keitou[i][0] = (String) skill[j][1];
        			keitou[i][1] = (Integer) skill[j][2];
        		}
        	}
        }
        //keitouのスキルをもつ各防具を集めて候補とする．
        for (int i = 0; i < keitou.length; i++) {
        	for (int j = 2; j < pro_table[0].length; j++) {
        		if(keitou[i][0].equals((String) pro_table[0][j])) {
        			skillIndex.add(j);
        			for(int k = 1; k < pro_table.length; k++) {
    					if((int) pro_table[k][j] > 0) {
    						if (k < 856) {
    							switch(k % 5) {
            					case 0:
            						leg.add(k);
            						break;
            					case 1:
            						head.add(k);
            						break;
            					case 2:
            						arm.add(k);
            						break;
            					case 3:
            						chest.add(k);
            						break;
            					case 4:
            						waist.add(k);
            					}
    						} else {
    							head.add(k);
    						}
    					}
        			}
        		}
        	}
        }
        
        //(防具テーブルでのスキル系統インデックス, 必要ポイント)のリストを作成
        ArrayList<Map.Entry<Integer, Integer>> keitouList = new ArrayList<>();
        for (Integer i = 0; i < skillIndex.size(); i++) {
        	keitouList.add(new AbstractMap.SimpleEntry<>(skillIndex.get(i), (int) keitou[i][1]));
        }
        
        //Fasterクラスをインスタンス化
        Faster faster = new Faster(keitouList, pro_table);
        
        //相当スキルが3より大きい防具に絞って新しくリストを作成
        ArrayList<Map.Entry<Integer, Double>> headVal = faster.getBetter(head);
        ArrayList<Map.Entry<Integer, Double>> chestVal = faster.getBetter(chest);
        ArrayList<Map.Entry<Integer, Double>> armVal = faster.getBetter(arm);
        ArrayList<Map.Entry<Integer, Double>> waistVal = faster.getBetter(waist);
        ArrayList<Map.Entry<Integer, Double>> legVal = faster.getBetter(leg);
        //お守り配列{{index, point}, {index, point}}を作成
        Integer[][] omamori = {{0, 0}, {0, 0}};
        for (int i = 0; i < pro_table[0].length; i++) {
    		if(((String) pro_table[0][i]).equals(oma1)) {
    			omamori[0][0] = i;
    			omamori[0][1] = oma_p1;
    		} else if (((String) pro_table[0][i]).equals(oma2)) {
    			omamori[1][0] = i;
    			omamori[1][1] = oma_p2;
    		}
        }
        //各パーツで最も良いものをarmorValにいれる
        ArrayList<Map.Entry<Integer, Double>> armorVal = new ArrayList<>();
        //三スロ防具として三眼を加える
        headVal.add(new AbstractMap.SimpleEntry<>(791, 3.));
        chestVal.add(new AbstractMap.SimpleEntry<>(793, 3.));
        armVal.add(new AbstractMap.SimpleEntry<>(792, 3.));
        waistVal.add(new AbstractMap.SimpleEntry<>(794, 3.));
        legVal.add(new AbstractMap.SimpleEntry<>(795, 3.));
        armorVal.add(headVal.get(0));
        armorVal.add(chestVal.get(0));
        armorVal.add(armVal.get(0));
        armorVal.add(waistVal.get(0));
        armorVal.add(legVal.get(0));
        //選択スキルが多すぎないかどうかのavailableを得る
        Boolean available = faster.judgeUnable(armorVal, omamori, slot_wepon, slot_oma);
        //available==falseならnullの列を出力
        if (!available) {
        	String[] result = {"可能な組み合わせはありません"};
    		return new AbstractMap.SimpleEntry<>(0, result);
        }
        //選択した防具候補で可能な防具，装飾品の組み合わせを全列挙計算
    	ArrayList<Map.Entry<Integer, Double>>[] armors = new ArrayList[5];
    	armors[0] = headVal;
    	armors[1] = chestVal;
    	armors[2] = armVal;
    	armors[3] = waistVal;
    	armors[4] = legVal;
    	Combination comb = new Combination(pro_table, ornament, armors, faster.keitouList, omamori, slot_wepon, slot_oma);
    	//余りスロット数が多い組合せを上位10個選ぶ
    	Map.Entry<Integer, CustomResult[]> top10Map;
    	top10Map = comb.calComb();
    	Integer combNumber = top10Map.getKey();
    	CustomResult[] top10 = top10Map.getValue();
    	//可能な組み合わせがないならメッセージ
    	if (combNumber == 0) {
    		String[] result = {"可能な組み合わせはありません"};
    		return new AbstractMap.SimpleEntry<>(0, result);
    	}
    	String[] result = new String[top10.length];
    	//top10からnullでない個数だけresultにいれる
    	for (Integer i = 0; i < top10.length; i++) {
    		if (top10[i] == null) {
    			result = new String[i+1];
    			break;
    		}
    	}
    	//結果を文字列の配列にして出力
    	for(int i=0; i < result.length; i++) {
    		//ないなら終了
    		if (top10[i] == null) {
    			break;
    		}
    		//胴系統倍加の個数douを計算
    		Integer dou = 0;
        	for (Integer bougu : top10[i].bouguComb) {
        		if (pro_table[bougu][0].toString().endsWith("胴")) {
        			dou++;
        		}
        	}
    		//装飾品に関する文字列生成
    		String strOrn = "";
    		if (top10[i].slotDou.size() == 0) {
    			for (Map.Entry<Integer, Integer[]> e1 : top10[i].entry) {
        			if (e1.getValue()[0] > 0) {
        				strOrn = strOrn + ornament[e1.getKey()-1][1].toString() +
        						"×" + e1.getValue()[0].toString() + ", ";
        			}
        			if (e1.getValue()[1] > 0) {
        				strOrn = strOrn + ornament[e1.getKey()-1][5].toString() +
        						"×" + e1.getValue()[1].toString() + ", ";
        			}
        			if (e1.getValue()[2] > 0) {
        				strOrn = strOrn + ornament[e1.getKey()-1][9].toString() +
        						"×" + e1.getValue()[2].toString() + ", ";
        			}
        		}
    		} else {
    			//胴につける装飾品に対するテキスト
    			for (Integer[] e1 : top10[i].slotDou) {
    				strOrn = strOrn + ornament[e1[0]-1][e1[1]*4-3].toString() +
    						"×" + e1[2].toString() + "(胴), ";
    			}
    			ArrayList<Map.Entry<Integer, Integer[]>> entryCopy =
    					(ArrayList<Entry<Integer, Integer[]>>) top10[i].entry.clone();
    			for (Integer j = 0; j < entryCopy.size(); j++) {
    				for (Integer[] e2 : top10[i].slotDou) {
    					if (entryCopy.get(j).getKey() == e2[0]) {
    						Integer[] eCopy = entryCopy.get(j).getValue().clone();
    						eCopy[e2[1]-1] = Math.max(0, eCopy[e2[1]-1]-e2[2]);
    						entryCopy.set(j, new AbstractMap.SimpleEntry<>(e2[0], eCopy));
    					}
    				}
    			}
    			for (Map.Entry<Integer, Integer[]> e1 : entryCopy) {
        			if (e1.getValue()[0] > 0) {
        				strOrn = strOrn + ornament[e1.getKey()-1][1].toString() +
        						"×" + e1.getValue()[0].toString() + ", ";
        			}
        			if (e1.getValue()[1] > 0) {
        				strOrn = strOrn + ornament[e1.getKey()-1][5].toString() +
        						"×" + e1.getValue()[1].toString() + ", ";
        			}
        			if (e1.getValue()[2] > 0) {
        				strOrn = strOrn + ornament[e1.getKey()-1][9].toString() +
        						"×" + e1.getValue()[2].toString() + ", ";
        			}
        		}
    		}
    		
    		//発動スキルに関する文字列生成
    		String strSkill = "";
    		for (Map.Entry<Integer, Integer> entry : top10[i].hatudouSkill) {
    			strSkill = strSkill + pro_table[0][entry.getKey()].toString() + entry.getValue().toString() + ", ";
    		}
    		//お守りに関する文字列生成
    		String strOma = "";
    		if (oma1.equals("-")) {
    			if (oma2.equals("-")) {
    				strOma = "スキル無し";
    			} else {
    				strOma = oma2 + "+" + oma_p2.toString();
    			}
    		} else {
    			if (oma2.equals("-")) {
    				strOma = oma1 + "+" + oma_p1.toString();
    			} else {
    				strOma = oma1 + "+" + oma_p1.toString() + ", " + oma2 + "+" + oma_p2.toString();
    			}
    		}
    		//防具に関する文字列生成
    		String strHead = pro_table[top10[i].bouguComb[0]][0].toString();
    		String strChest = pro_table[top10[i].bouguComb[1]][0].toString();
    		String strArm = pro_table[top10[i].bouguComb[2]][0].toString();
    		String strWaist = pro_table[top10[i].bouguComb[3]][0].toString();
    		String strLeg = pro_table[top10[i].bouguComb[4]][0].toString();
    		if (pro_table[top10[i].bouguComb[0]][0].toString().endsWith("胴")) {
    			strHead = "胴系統倍加";
    		}
    		if (pro_table[top10[i].bouguComb[3]][0].toString().endsWith("胴")) {
    			strWaist = "胴系統倍加";
    		}
    		if (pro_table[top10[i].bouguComb[4]][0].toString().endsWith("胴")) {
    			strLeg = "胴系統倍加";
    		}
    		result[i] =
    				gender + ", " + profession + ", 武器スロ" + slot_wepon.toString() + "\n" +
    				strHead + "\n" + strChest + "\n" + strArm + "\n" + strWaist + "\n" + strLeg + "\n" +
    				"護石：" + slot_oma.toString() + "スロ" + ", " + strOma + "\n" +
    				"装飾品：" + strOrn + "\n" +
    				"余りスロット：(1スロ, 2スロ, 3スロ) = (" + top10[i].margin[0].toString() + ", "  +
    					top10[i].margin[1].toString() + ", " + top10[i].margin[２].toString() + "), 計" +
    					String.valueOf(top10[i].margin[0]+top10[i].margin[1]*2+top10[i].margin[2]*3) + "スロ\n" +
    				"発動スキル：" + strSkill;
    	}
    	return new AbstractMap.SimpleEntry<>(combNumber, result);
    }
}
