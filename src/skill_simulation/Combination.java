package skill_simulation;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

public class Combination {
	//変数宣言
	private static Object[][] pro_table;
	private static Object[][] ornament;
	private static ArrayList<Map.Entry<Integer, Double>>[] armors;
	private static ArrayList<Map.Entry<Integer, Integer>> keitouList;
	private static Integer[][] omamori;
	private static Integer slot_wepon;
	private static Integer slot_oma;
	private static Integer[] resultArm;
	private static ArrayList<Integer> resultOrn;
	private static ArrayList<Map.Entry<Integer, Integer>> resultSkill;
	Integer displayNumber = MakeInputScreen.displayNumber;
	
	
	public Combination(
			//引数
			Object[][] pro_table,
			Object[][] ornament,
			ArrayList<Map.Entry<Integer, Double>>[] armors,
			ArrayList<Map.Entry<Integer, Integer>> keitouList,
			Integer[][] omamori,
			Integer slot_wepon,
			Integer slot_oma) {
		
		this.pro_table = pro_table;
		this.ornament = ornament;
		this.armors = armors;
		this.keitouList = keitouList;
		this.omamori = omamori;
		this.slot_wepon = slot_wepon;
		this.slot_oma = slot_oma;
	}
	
	public Map.Entry<Integer, CustomResult[]> calComb () {
		//結果を格納するresult = ArrayList<(防具インデックス[5], 装飾品の組合せ, 余りスロット)>
		ArrayList<CustomResult> results = new ArrayList<>();
		//高速化のため各パーツ上位5個に絞ったarmorsMiniを生成
		ArrayList<Map.Entry<Integer, Double>>[] armorsMini = new ArrayList[5];
		//armorsMini = armors;
		for (Integer i = 0; i < 5; i++) {
            if (armors[i].size() < 6) {//10
                armorsMini[i] = armors[i];
            } else {
            	armorsMini[i] = new ArrayList<>();
            	for (Integer j = 0; j < 5; j++) {//8
            		armorsMini[i].add(armors[i].get(j));
            	}
            	armorsMini[i].add(armors[i].get(armors[i].size()-1));
            }
        }
		for (Map.Entry<Integer, Double> chest : armorsMini[1]) {
			//胴系統倍加を考慮
			armorsMini[0].add(0, new AbstractMap.SimpleEntry<>(chest));
			armorsMini[3].add(0, new AbstractMap.SimpleEntry<>(chest));
			armorsMini[4].add(0, new AbstractMap.SimpleEntry<>(chest));
			for (Map.Entry<Integer, Double> head : armorsMini[0]) {
				for (Map.Entry<Integer, Double> arm : armorsMini[2]) {
					for (Map.Entry<Integer, Double> waist : armorsMini[3]) {
						for (Map.Entry<Integer, Double> leg : armorsMini[4]) {
							Integer dou = 0;
							//胴系統倍加の個数
							if (pro_table[head.getKey()][0].toString().endsWith("胴")) {
								dou++;
							}
							if (pro_table[waist.getKey()][0].toString().endsWith("胴")) {
								dou++;
							}
							if (pro_table[leg.getKey()][0].toString().endsWith("胴")) {
								dou++;
							}
							//slot配列を得る
							Integer[] slot = {0, 0, 0};
							if (slot_wepon != 0) {
								slot[slot_wepon - 1] += 1;
							}
							if (slot_oma != 0) {
								slot[slot_oma - 1] += 1;
							}
							if ((int) pro_table[head.getKey()][1] != 0) {
								slot[(int) pro_table[head.getKey()][1] - 1] += 1;
							}
							if ((int) pro_table[chest.getKey()][1] != 0) {
								slot[(int) pro_table[chest.getKey()][1] - 1] += 1;
							}
							if ((int) pro_table[arm.getKey()][1] != 0) {
								slot[(int) pro_table[arm.getKey()][1] - 1] += 1;
							}
							if ((int) pro_table[waist.getKey()][1] != 0) {
								slot[(int) pro_table[waist.getKey()][1] - 1] += 1;
							}
							if ((int) pro_table[leg.getKey()][1] != 0) {
								slot[(int) pro_table[leg.getKey()][1] - 1] += 1;
							}
							//allSkill配列を得る
							Integer[] allSkill = new Integer[pro_table[0].length];
							for (Integer i = 0; i < allSkill.length; i++) {
								allSkill[i] = 0;
							}
							//お守りの分を加算
							allSkill[omamori[0][0]] += omamori[0][1];
							allSkill[omamori[1][0]] += omamori[1][1];
							for (int i = 2; i < pro_table[0].length; i++) {
								allSkill[i] += (int) pro_table[head.getKey()][i] +
										(int) pro_table[chest.getKey()][i] +
										(int) pro_table[arm.getKey()][i] +
										(int) pro_table[waist.getKey()][i] +
										(int) pro_table[leg.getKey()][i];
							}
							//lackList生成
							ArrayList<Map.Entry<Integer, Integer>> lackList = new ArrayList<>();
							for (Map.Entry<Integer, Integer> entry : keitouList) {
								lackList.add(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()-allSkill[entry.getKey()]));
							}
							//各スキルの装飾品の全組合わせornListを得る
							ArrayList<Map.Entry<Integer, ArrayList<Integer[]>>> ornList = new ArrayList<>();
							for (Map.Entry<Integer, Integer> entry : lackList) {
								ArrayList<Integer[]> orn_comb = new ArrayList<>();
								if (ornament[entry.getKey()-1][10] != null) {
									if ((int) ornament[entry.getKey()-1][10] == 5) {
										orn_comb = ornComb(entry.getValue(), 3);
										ornList.add(new AbstractMap.SimpleEntry<>(entry.getKey(), orn_comb));
									} else if ((int) ornament[entry.getKey()-1][10] == 4) {
										orn_comb = ornComb(entry.getValue(), 2);
										ornList.add(new AbstractMap.SimpleEntry<>(entry.getKey(), orn_comb));
									} else if ((int) ornament[entry.getKey()-1][10] == 2) {
										orn_comb = ornComb(entry.getValue(), 4);
										ornList.add(new AbstractMap.SimpleEntry<>(entry.getKey(), orn_comb));
									}
								} else if (ornament[entry.getKey()-1][6] != null) {
									if ((int) ornament[entry.getKey()-1][6] == 3) {
										orn_comb = ornComb(entry.getValue(), 1);
										ornList.add(new AbstractMap.SimpleEntry<>(entry.getKey(), orn_comb));
									}
								} else if (ornament[entry.getKey()-1][2] != null) {
									if ((int) ornament[entry.getKey()-1][2] == 2) {
										orn_comb = ornComb(entry.getValue(), 0);
										ornList.add(new AbstractMap.SimpleEntry<>(entry.getKey(), orn_comb));
									} else if ((int) ornament[entry.getKey()-1][2] == 1) {
										orn_comb = ornComb(entry.getValue(), 5);
										ornList.add(new AbstractMap.SimpleEntry<>(entry.getKey(), orn_comb));
									}
								} else {
									//System.out.println("orn_comb error");
								}
							}
							//各スキルに対する装飾品の組合せのリストallCombinationを得る
							ArrayList<ArrayList<Map.Entry<Integer, Integer[]>>> allCombinations = new ArrayList<>();
							generateCombinations(ornList, 0, new ArrayList<>(), allCombinations);
							//装飾品の各組み合わせが可能か
							for (ArrayList<Map.Entry<Integer, Integer[]>> entry : allCombinations) {
					        	//装飾品で使用するスロットのリスト
					        	Integer[] needSlot = {0, 0, 0};
					        	//各スキルに対する装飾品のみのポイントリストsuffListを初期化
								ArrayList<Map.Entry<Integer, Integer>> suffList = new ArrayList<>();
								for (Map.Entry<Integer, Integer> suf : lackList) {
				        			suffList.add(new AbstractMap.SimpleEntry<>(suf.getKey(), 0));
				        		}
					        	for (Map.Entry<Integer, Integer[]> entry2 : entry) {
					        		//各装飾品の組合せで必要なスロット数の配列needSlotListを生成
					        		needSlot[0] += entry2.getValue()[0];
					        		needSlot[1] += entry2.getValue()[1];
					        		needSlot[2] += entry2.getValue()[2];
					        		//対応するマイナススキルインデックスを得る
					        		Integer minusIndex = 0;
					        		if (entry2.getValue()[0] > 0 && ornament[entry2.getKey()-1][3] != null) {
					        			minusIndex = returnIndex(ornament[entry2.getKey()-1][3].toString());
					        		} else if (entry2.getValue()[1] > 0 && ornament[entry2.getKey()-1][7] != null) {
					        			minusIndex = returnIndex(ornament[entry2.getKey()-1][7].toString());
					        		} else if (entry2.getValue()[2] > 0 && ornament[entry2.getKey()-1][11] != null) {
					        			minusIndex = returnIndex(ornament[entry2.getKey()-1][11].toString());
					        		}
					        		//装飾品による合計ポイントを計算
					        		Integer getPoint = 0;
					        		Integer minusPoint = 0;
					        		if (entry2.getValue()[0] > 0) {
					        			getPoint += (int) ornament[entry2.getKey()-1][2] * entry2.getValue()[0];
					        			if (ornament[entry2.getKey()-1][4] != null) {
					        				minusPoint += (int) ornament[entry2.getKey()-1][4] * entry2.getValue()[0];
					        			}
					        		}
					        		if (entry2.getValue()[1] > 0) {
					        			getPoint += (int) ornament[entry2.getKey()-1][6] * entry2.getValue()[1];
					        			if (ornament[entry2.getKey()-1][8] != null) {
					        				minusPoint += (int) ornament[entry2.getKey()-1][8] * entry2.getValue()[1];
					        			}
					        		}
					        		if (entry2.getValue()[2] > 0) {
					        			getPoint += (int) ornament[entry2.getKey()-1][10] * entry2.getValue()[2];
					        			if (ornament[entry2.getKey()-1][12] != null) {
					        				minusPoint += (int) ornament[entry2.getKey()-1][12] * entry2.getValue()[2];
					        			}
					        		}
					        		//suffListを完成
					        		for (Integer i = 0; i < suffList.size(); i++) {
					        			if (suffList.get(i).getKey() == entry2.getKey()) {
					        				suffList.set(i, new AbstractMap.SimpleEntry<>(suffList.get(i).getKey(), suffList.get(i).getValue() + getPoint));
					        			} else if (suffList.get(i).getKey() == minusIndex) {
					        				suffList.set(i, new AbstractMap.SimpleEntry<>(suffList.get(i).getKey(), suffList.get(i).getValue() + minusPoint));
					        			}
					        		}
								}
					        	//suffListがlackListを満たしているかを判定
					        	Boolean MoreThanLack = true;
					        	for (int i = 0; i < suffList.size(); i++) {
					                if (suffList.get(i).getValue() < lackList.get(i).getValue()) {
					                    MoreThanLack = false; // 一つでも lackListのほうが大きい要素があればfalseを返す
					                }
					            }
					        	//胴+胴系統倍加分のスロットを除いたslot2を生成
					        	Integer[] slot2 = Arrays.copyOf(slot, 3);
					        	Integer slotChest = (int) pro_table[chest.getKey()][1];
					        	if (slotChest > 0) {
					        		slot2[slotChest-1] -= (dou+1);
					        	}
					        	CustomSlot output = SkillSlotManager.slotCal(entry, slotChest, dou, slot2);
					        	//slotOKがtrueでMoreThanLackもOKなら解に加える
					            if (output.available && MoreThanLack) {
				        			Integer[] first = {head.getKey(), chest.getKey(), arm.getKey(), waist.getKey(), leg.getKey()};
				        			ArrayList<Map.Entry<Integer, Integer>> forth = new ArrayList<>();
				        			results.add(new CustomResult(first, entry, output.margin, forth, output.slot_dou));
				        		}
					        }
						}
					}
				}
			}
			armorsMini[0].remove(0);
			armorsMini[3].remove(0);
			armorsMini[4].remove(0);
		}
		//results.size()==0なら可能な組み合わせが存在しない
		if (results.size() == 0) {
			CustomResult[] res = new CustomResult[0];
			return new AbstractMap.SimpleEntry<>(0, res);
		}
		// resultsの各要素を余りスロットが多い順に並べ替え
        results.sort((o1, o2) -> {
            Integer[] t1 = o1.margin;
            Integer[] t2 = o2.margin;
            // a + b*2 + c*3 を計算
            int value1 = t1[0] + t1[1] * 2 + t1[2] * 3;
            int value2 = t2[0] + t2[1] * 2 + t2[2] * 3;
            // a + b*2 + c*3 の降順. a + b*2 + c*3が同じものはc, b, aの順で優先
            if (value1 != value2) {
                return Integer.compare(value2, value1);
            }
            if (!t1[2].equals(t2[2])) {
                return Integer.compare(t2[2], t1[2]);
            }
            if (!t1[1].equals(t2[1])) {
                return Integer.compare(t2[1], t1[1]);
            }
            return Integer.compare(t2[0], t1[0]);
        });
        //resultsから上位displayNumber個出力
        CustomResult[] results10 = new CustomResult[displayNumber];
        for (Integer i = 0; i < Math.min(results.size(), displayNumber); i++) {
        	Integer[] hatudou = new Integer[pro_table[0].length];
        	hatudou[0] = 0;
        	hatudou[1] = 1;
        	for (Integer j = 2; j < pro_table[0].length; j++) {
        		//防具の分のスキルポイントを加算
        		hatudou[j] = (int) pro_table[results.get(i).bouguComb[0]][j] +
            			(int) pro_table[results.get(i).bouguComb[1]][j] +
            			(int) pro_table[results.get(i).bouguComb[2]][j] +
            			(int) pro_table[results.get(i).bouguComb[3]][j] +
            			(int) pro_table[results.get(i).bouguComb[4]][j];
        	}
        	//装飾品の分のスキルポイントを加算
        	for (Map.Entry<Integer, Integer[]> entry : results.get(i).entry) {
        		//対応するマイナススキルインデックスを得る
        		Integer minusIndex = 0;
        		if (entry.getValue()[0] > 0 && ornament[entry.getKey()-1][3] != null) {
        			minusIndex = returnIndex(ornament[entry.getKey()-1][3].toString());
        		} else if (entry.getValue()[1] > 0 && ornament[entry.getKey()-1][7] != null) {
        			minusIndex = returnIndex(ornament[entry.getKey()-1][7].toString());
        		} else if (entry.getValue()[2] > 0 && ornament[entry.getKey()-1][11] != null) {
        			minusIndex = returnIndex(ornament[entry.getKey()-1][11].toString());
        		}
        		//装飾品による合計ポイントを計算
        		Integer getPoint = 0;
        		Integer minusPoint = 0;
        		if (entry.getValue()[0] > 0) {
        			getPoint += (int) ornament[entry.getKey()-1][2] * entry.getValue()[0];
        			if (ornament[entry.getKey()-1][4] != null) {
        				minusPoint += (int) ornament[entry.getKey()-1][4] * entry.getValue()[0];
        			}
        		}
        		if (entry.getValue()[1] > 0) {
        			getPoint += (int) ornament[entry.getKey()-1][6] * entry.getValue()[1];
        			if (ornament[entry.getKey()-1][8] != null) {
        				minusPoint += (int) ornament[entry.getKey()-1][8] * entry.getValue()[1];
        			}
        		}
        		if (entry.getValue()[2] > 0) {
        			getPoint += (int) ornament[entry.getKey()-1][10] * entry.getValue()[2];
        			if (ornament[entry.getKey()-1][12] != null) {
        				minusPoint += (int) ornament[entry.getKey()-1][12] * entry.getValue()[2];
        			}
        		}
        		//装飾品の分のスキルポイントを加算
        		hatudou[entry.getKey()] += getPoint;
        		hatudou[minusIndex] += minusPoint;
            }
        	//お守りの分のスキルポイントを加算
        	hatudou[omamori[0][0]] += omamori[0][1];
        	hatudou[omamori[1][0]] += omamori[1][1];
        	//+10以上 or -10以下のスキルをピックアップ
        	ArrayList<Map.Entry<Integer, Integer>> hatudouSkill = new ArrayList<>();
        	for (Integer k = 2; k < hatudou.length; k++) {
        		if (hatudou[k] >= 10 || hatudou[k] <= -10) {
        			hatudouSkill.add(new AbstractMap.SimpleEntry<>(k, hatudou[k]));
        		}
        	}
        	//result10に格納
        	//results10[i] = results.get(i);
        	results10[i] = new CustomResult(results.get(i).bouguComb, results.get(i).entry, results.get(i).margin, hatudouSkill, results.get(i).slotDou);
        }
        Map.Entry<Integer, CustomResult[]> output;
        output = new AbstractMap.SimpleEntry<>(results.size(), results10);
		return output;
	}
	
	
	
	public static ArrayList<Integer[]> ornComb (Integer requiredPoint, Integer ornType) {
		Integer[][] skillPoints = {{2, 0, 0}, {1, 3, 0}, {1, 0, 4}, {1, 3, 5}, {0, 1, 2}, {1, 0, 0}}; // 装飾品1, 2, 3のスキルポイント
        Integer[][] slotUsage = {{1, 0, 0}, {1, 2, 0}, {1, 0, 3}, {1, 2, 3}, {0, 2, 3}, {1, 0, 0}};  // 装飾品1, 2, 3のスロット使用数
        Integer[] sp = skillPoints[ornType]; //装飾品タイプから1つ選択
        Integer[] su = slotUsage[ornType]; //装飾品タイプから1つ選択
        ArrayList<Integer[]> result = new ArrayList<>(); //出力用
        // 各装飾品の個数を0から可能な範囲で試す
        //xの上限の設定
        Integer max_x = 0;
        if (requiredPoint > 0 && sp[2] > 0) {
        	max_x = (int) Math.ceil(((double)requiredPoint) / ((double)sp[2]));
        }
        for (Integer x = 0; x <= max_x; x++) { // 装飾品3
        	//yの上限の設定
            Integer max_y = 0;
            if (requiredPoint > 0 && sp[1] > 0) {
            	max_y = (int) Math.ceil(((double)(requiredPoint - x*sp[2])) / ((double)sp[1]));
            }
            for (Integer y = 0; y <= max_y; y++) { // 装飾品2
            	//zの上限の設定
                Integer max_z = 0;
                if (requiredPoint > 0 && sp[0] > 0) {
                	max_z = (int) Math.ceil(((double)(requiredPoint - x*sp[2] - y*sp[1])) / ((double)sp[0]));
                }
                for (Integer z = 0; z <= max_z; z++) { // 装飾品1
                    // 合計スキルポイントとスロット計算
                    int totalSkill = x * sp[2] + y * sp[1] + z * sp[0];
                    int totalSlot = x * su[2] + y * su[1] + z * su[0];
                    // 条件を満たす場合のみリストに追加
                    if (totalSkill >= requiredPoint) {
                    	//.out.println("スロ１: "+z.toString()+", "+"スロ２: "+y.toString()+", "+"スロ３: "+x.toString());
                        result.add(new Integer[]{z, y, x});
                    }
                }
            }
        }
        // 使用スロットが少ない順にソート
        result.sort(Comparator.comparingInt(arr -> arr[0] * su[0] + arr[1] * su[1] + arr[2] * su[2]));
        return result;
	}
	
	
	
	private static void generateCombinations(
            ArrayList<Map.Entry<Integer, ArrayList<Integer[]>>> ornList,
            int index,
            ArrayList<Map.Entry<Integer, Integer[]>> currentCombination,
            ArrayList<ArrayList<Map.Entry<Integer, Integer[]>>> allCombinations) {
        // ベースケース: ornListの最後まで到達した場合
        if (index == ornList.size()) {
            // 現在の組み合わせを結果に追加
            allCombinations.add(new ArrayList<>(currentCombination));
            return;
        }
        // 現在のエントリ
        Map.Entry<Integer, ArrayList<Integer[]>> currentEntry = ornList.get(index);
        // 現在のエントリの右側 (ArrayList<Integer[]>) から要素を1つずつ選ぶ
        for (Integer[] value : currentEntry.getValue()) {
            // 現在の選択を追加
            currentCombination.add(new AbstractMap.SimpleEntry<>(currentEntry.getKey(), value));
            // 次のインデックスに移動
            generateCombinations(ornList, index + 1, currentCombination, allCombinations);
            // 選択を取り除く (バックトラック)
            currentCombination.remove(currentCombination.size() - 1);
        }
    }
	
	
	public static Integer returnIndex(String keitou_mei) {
		Integer index = 0;
		for (Integer i = 2; i < pro_table[0].length; i++) {
			if ((pro_table[0][i].toString()).equals(keitou_mei)) {
				index = i;
			}
		}
		return index;
	}
}
