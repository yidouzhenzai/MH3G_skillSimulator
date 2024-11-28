package skill_simulation;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class SkillSlotManager {

    public static CustomSlot slotCal(
        ArrayList<Map.Entry<Integer, Integer[]>> entry,
        Integer slotChest,
        Integer dou,
        Integer[] slot) {
    	//needSlot計算
    	Integer[] needSlot = {0, 0, 0};
    	for (Map.Entry<Integer, Integer[]> e1 : entry) {
    		needSlot[0] += e1.getValue()[0];
    		needSlot[1] += e1.getValue()[1];
    		needSlot[2] += e1.getValue()[2];
    	}
    	
    	//胴スロット0 or 胴系統倍加0ならそのまま返す
		if (dou == 0) {
			Integer[] slot2 = Arrays.copyOf(slot, 3);
			if (slotChest > 0) {
				slot2[slotChest-1] ++;
			}
			Map.Entry<Boolean, Integer[]> slotOkResult = slotOK(slot2, needSlot);
			Boolean available = slotOkResult.getKey();
    		ArrayList<Integer[]> slot_dou = new ArrayList<>();
    		Integer[] margin = slotOkResult.getValue();
			return new CustomSlot(available, slot_dou, margin);
		}
		if (slotChest == 0) {
			Map.Entry<Boolean, Integer[]> slotOkResult = slotOK(slot, needSlot);
			Boolean available = slotOkResult.getKey();
    		ArrayList<Integer[]> slot_dou = new ArrayList<>();
    		Integer[] margin = slotOkResult.getValue();
			return new CustomSlot(available, slot_dou, margin);
		}
		//胴スロ1なら胴スロを空きにするかe2のいずれかを入れてmarginが大きいものが現れたら更新
		if (slotChest == 1) {
			Map.Entry<Boolean, Integer[]> slotOkResult = slotOK(slot, needSlot);
			Boolean available = slotOkResult.getKey();
    		ArrayList<Integer[]> slot_dou = new ArrayList<>();
    		Integer[] margin = slotOkResult.getValue();
    		if (available) {
    			margin[slotChest-1] += dou + 1;
    			return new CustomSlot(available, slot_dou, margin);
    		} else {
    			//margin初期化
    			margin[0] = -1;
    			margin[1] = 0;
    			margin[2] = 0;
    			for (Map.Entry<Integer, Integer[]> e2 : entry) {
        			if (e2.getValue()[0] > 0) {
        				Integer[] needSlot2 = Arrays.copyOf(needSlot, 3);
        				needSlot2[0] = Math.max(needSlot2[0]-e2.getValue()[0], needSlot2[0]-(dou+1));
        				Map.Entry<Boolean, Integer[]> slotOkResult2 = slotOK(slot, needSlot2);
        				Boolean available2 = slotOkResult2.getKey();
        	    		ArrayList<Integer[]> slot_dou2 = new ArrayList<>();
        	    		slot_dou2.add(new Integer[]{e2.getKey(), 1, 1});
        	    		Integer[] margin2 = slotOkResult2.getValue();
        	    		if (available2 && margin[0]+margin[1]*2+margin[2]*3 < margin2[0]+margin2[1]*2+margin2[2]*3) {
        	    			available = available2;
    	    				slot_dou = slot_dou2;
    	    				margin = margin2;
        	    		}
        			}
        		}
    		}
    		return new CustomSlot(available, slot_dou, margin);
		} else if (slotChest == 2) {
			Map.Entry<Boolean, Integer[]> slotOkResult = slotOK(slot, needSlot);
			Boolean available = slotOkResult.getKey();
    		ArrayList<Integer[]> slot_dou = new ArrayList<>();
    		Integer[] margin = slotOkResult.getValue();
    		//胴に装飾品無しでOKなら即出力
    		if (available) {
    			margin[slotChest-1] += dou + 1;
    			return new CustomSlot(available, slot_dou, margin);
    		} else {
    			//margin初期化
    			margin[0] = -1;
    			margin[1] = 0;
    			margin[2] = 0;
    			//まずは1スロ装飾品1つのみ
    			for (Map.Entry<Integer, Integer[]> e2 : entry) {
        			if (e2.getValue()[０] > 0) {
        				Integer[] needSlot2 = Arrays.copyOf(needSlot, 3);
        				needSlot2[０] = Math.max(needSlot2[０]-e2.getValue()[0], needSlot2[0]-(dou+1));
        				Map.Entry<Boolean, Integer[]> slotOkResult2 = slotOK(slot, needSlot2);
        				Boolean available2 = slotOkResult2.getKey();
        	    		ArrayList<Integer[]> slot_dou2 = new ArrayList<>();
        	    		slot_dou2.add(new Integer[]{e2.getKey(), 1, 1});
        	    		Integer[] margin2 = slotOkResult2.getValue();
        	    		margin2[0] += dou + 1;
        	    		if (available2 && margin[0]+margin[1]*2+margin[2]*3 < margin2[0]+margin2[1]*2+margin2[2]*3) {
        	    			available = available2;
    	    				slot_dou = slot_dou2;
    	    				margin = margin2;
        	    		}
        			}
        		}
    			if (available) {
    				return new CustomSlot(available, slot_dou, margin);
    			}
    			//まだavailable=false, margin={0, 0, 0}
    			//次に2スロ装飾品1つのみ
    			for (Map.Entry<Integer, Integer[]> e2 : entry) {
        			if (e2.getValue()[1] > 0) {
        				Integer[] needSlot2 = Arrays.copyOf(needSlot, 3);
        				needSlot2[1] = Math.max(needSlot2[1]-e2.getValue()[1], needSlot2[1]-(dou+1));
        				Map.Entry<Boolean, Integer[]> slotOkResult2 = slotOK(slot, needSlot2);
        				Boolean available2 = slotOkResult2.getKey();
        				ArrayList<Integer[]> slot_dou2 = new ArrayList<>();
        	    		slot_dou2.add(new Integer[]{e2.getKey(), 2, 1});
        	    		Integer[] margin2 = slotOkResult2.getValue();
        	    		if (available2 && margin[0]+margin[1]*2+margin[2]*3 < margin2[0]+margin2[1]*2+margin2[2]*3) {
        	    			available = available2;
    	    				slot_dou = slot_dou2;
    	    				margin = margin2;
        	    		}
        			}
        		}
    			//次に1スロ装飾品2つ
    			for (Map.Entry<Integer, Integer[]> e2 : entry) {
    				for (Map.Entry<Integer, Integer[]> e3 : entry) {
    					Integer[] needSlot2 = Arrays.copyOf(needSlot, 3);
    					if (e2==e3) {
    						needSlot2[0] = Math.max(needSlot2[0]-e2.getValue()[0], needSlot2[0]-(dou+1)*2);
        	    		} else {
        	    			needSlot2[0] = Math.max(needSlot2[0]-e2.getValue()[0], needSlot2[0]-(dou+1));
            				needSlot2[0] = Math.max(needSlot2[0]-e3.getValue()[0], needSlot2[0]-(dou+1));
        	    		}
        				Map.Entry<Boolean, Integer[]> slotOkResult2 = slotOK(slot, needSlot2);
        				Boolean available2 = slotOkResult2.getKey();
        	    		ArrayList<Integer[]> slot_dou2 = new ArrayList<>();
        	    		if (e2==e3) {
        	    			slot_dou2.add(new Integer[]{e2.getKey(), 1, 2});
        	    		} else {
        	    			slot_dou2.add(new Integer[]{e2.getKey(), 1, 1});
            	    		slot_dou2.add(new Integer[]{e3.getKey(), 1, 1});
        	    		}
        	    		Integer[] margin2 = slotOkResult2.getValue();
        	    		if (available2 && margin[0]+margin[1]*2+margin[2]*3 < margin2[0]+margin2[1]*2+margin2[2]*3) {
        	    			available = available2;
    	    				slot_dou = slot_dou2;
    	    				margin = margin2;
        	    		}
        			}
    			}
    			return new CustomSlot(available, slot_dou, margin);
    		}
		} else {
			Map.Entry<Boolean, Integer[]> slotOkResult = slotOK(slot, needSlot);
			Boolean available = slotOkResult.getKey();
    		ArrayList<Integer[]> slot_dou = new ArrayList<>();
    		Integer[] margin = slotOkResult.getValue();
    		//胴に装飾品無しでOKなら即出力
    		if (available) {
    			margin[slotChest-1] += dou + 1;
    			return new CustomSlot(available, slot_dou, margin);
    		} else {
    			//margin初期化
    			margin[0] = -1;
    			margin[1] = 0;
    			margin[2] = 0;
    			//まずは1スロ装飾品1つのみ
    			for (Map.Entry<Integer, Integer[]> e2 : entry) {
        			if (e2.getValue()[０] > 0) {
        				Integer[] needSlot2 = Arrays.copyOf(needSlot, 3);
        				needSlot2[0] = Math.max(needSlot2[0]-e2.getValue()[0], needSlot2[0]-(dou+1));
        				Map.Entry<Boolean, Integer[]> slotOkResult2 = slotOK(slot, needSlot2);
        				Boolean available2 = slotOkResult2.getKey();
        	    		ArrayList<Integer[]> slot_dou2 = new ArrayList<>();
        	    		slot_dou2.add(new Integer[]{e2.getKey(), 1, 1});
        	    		Integer[] margin2 = slotOkResult2.getValue();
        	    		margin2[1] += dou + 1;
        	    		if (available2 && margin[0]+margin[1]*2+margin[2]*3 < margin2[0]+margin2[1]*2+margin2[2]*3) {
        	    			available = available2;
    	    				slot_dou = slot_dou2;
    	    				margin = margin2;
        	    		}
        			}
        		}
    			if (available) {
    				return new CustomSlot(available, slot_dou, margin);
    			}
    			//まだavailable=false, margin={0, 0, 0}
    			//次に2スロ装飾品1つのみ
    			for (Map.Entry<Integer, Integer[]> e2 : entry) {
        			if (e2.getValue()[1] > 0) {
        				Integer[] needSlot2 = Arrays.copyOf(needSlot, 3);
        				needSlot2[1] = Math.max(needSlot2[1]-e2.getValue()[1], needSlot2[1]-(dou+1));
        				Map.Entry<Boolean, Integer[]> slotOkResult2 = slotOK(slot, needSlot2);
        				Boolean available2 = slotOkResult2.getKey();
        	    		ArrayList<Integer[]> slot_dou2 = new ArrayList<>();
        	    		slot_dou2.add(new Integer[]{e2.getKey(), 2, 1});
        	    		Integer[] margin2 = slotOkResult2.getValue();
        	    		margin2[0] += dou + 1;
        	    		if (available2 && margin[0]+margin[1]*2+margin[2]*3 < margin2[0]+margin2[1]*2+margin2[2]*3) {
        	    			available = available2;
    	    				slot_dou = slot_dou2;
    	    				margin = margin2;
        	    		}
        			}
        		}
    			//次に1スロ装飾品2つ
    			for (Map.Entry<Integer, Integer[]> e2 : entry) {
    				for (Map.Entry<Integer, Integer[]> e3 : entry) {
    					Integer[] needSlot2 = Arrays.copyOf(needSlot, 3);
        				needSlot2[0] = Math.max(needSlot2[0]-e2.getValue()[0], needSlot2[0]-(dou+1));
        				needSlot2[0] = Math.max(needSlot2[0]-e3.getValue()[0], needSlot2[0]-(dou+1));
        				Map.Entry<Boolean, Integer[]> slotOkResult2 = slotOK(slot, needSlot2);
        				Boolean available2 = slotOkResult2.getKey();
        	    		ArrayList<Integer[]> slot_dou2 = new ArrayList<>();
        	    		if (e2==e3) {
        	    			slot_dou2.add(new Integer[]{e2.getKey(), 1, 2});
        	    		} else {
        	    			slot_dou2.add(new Integer[]{e2.getKey(), 1, 1});
            	    		slot_dou2.add(new Integer[]{e3.getKey(), 1, 1});
        	    		}
        	    		Integer[] margin2 = slotOkResult2.getValue();
        	    		margin2[0] += dou + 1;
        	    		if (available2 && margin[0]+margin[1]*2+margin[2]*3 < margin2[0]+margin2[1]*2+margin2[2]*3) {
        	    			available = available2;
    	    				slot_dou = slot_dou2;
    	    				margin = margin2;
        	    		}
        			}
    			}
    			if (available) {
    				return new CustomSlot(available, slot_dou, margin);
    			}
    			//まだavailable=false, margin={0, 0, 0}
    			//次に3スロ装飾品1つのみ
    			for (Map.Entry<Integer, Integer[]> e2 : entry) {
        			if (e2.getValue()[2] > 0) {
        				Integer[] needSlot2 = Arrays.copyOf(needSlot, 3);
        				needSlot2[2] = Math.max(needSlot2[2]-e2.getValue()[2], needSlot2[2]-(dou+1));
        				Map.Entry<Boolean, Integer[]> slotOkResult2 = slotOK(slot, needSlot2);
        				Boolean available2 = slotOkResult2.getKey();
        	    		ArrayList<Integer[]> slot_dou2 = new ArrayList<>();
        	    		slot_dou2.add(new Integer[]{e2.getKey(), 3, 1});
        	    		Integer[] margin2 = slotOkResult2.getValue();
        	    		if (available2 && margin[0]+margin[1]*2+margin[2]*3 < margin2[0]+margin2[1]*2+margin2[2]*3) {
        	    			available = available2;
    	    				slot_dou = slot_dou2;
    	    				margin = margin2;
        	    		}
        			}
        		}
    			//次に2スロ装飾品1つ1スロ装飾品1つ
    			for (Map.Entry<Integer, Integer[]> e2 : entry) {
    				for (Map.Entry<Integer, Integer[]> e3 : entry) {
    					Integer[] needSlot2 = Arrays.copyOf(needSlot, 3);
        				needSlot2[1] = Math.max(needSlot2[1]-e2.getValue()[1], needSlot2[1]-(dou+1));
        				needSlot2[0] = Math.max(needSlot2[0]-e3.getValue()[0], needSlot2[0]-(dou+1));
        				Map.Entry<Boolean, Integer[]> slotOkResult2 = slotOK(slot, needSlot2);
        				Boolean available2 = slotOkResult2.getKey();
        	    		ArrayList<Integer[]> slot_dou2 = new ArrayList<>();
        	    		slot_dou2.add(new Integer[]{e2.getKey(), 2, 1});
        	    		slot_dou2.add(new Integer[]{e3.getKey(), 1, 1});
        	    		Integer[] margin2 = slotOkResult2.getValue();
        	    		if (available2 && margin[0]+margin[1]*2+margin[2]*3 < margin2[0]+margin2[1]*2+margin2[2]*3) {
        	    			available = available2;
    	    				slot_dou = slot_dou2;
    	    				margin = margin2;
        	    		}
        			}
    			}
    			//次に1スロ装飾品3つ
    			for (Map.Entry<Integer, Integer[]> e2 : entry) {
    				for (Map.Entry<Integer, Integer[]> e3 : entry) {
    					for (Map.Entry<Integer, Integer[]> e4 : entry) {
    						Integer[] needSlot2 = Arrays.copyOf(needSlot, 3);
    						if (e2==e3 && e3==e4) {
    							needSlot2[0] = Math.max(needSlot2[0]-e2.getValue()[0], needSlot2[0]-(dou+1)*3);
            	    		} else if (e2==e3) {
            	    			needSlot2[0] = Math.max(needSlot2[0]-e2.getValue()[0], needSlot2[0]-(dou+1)*2);
            	    			needSlot2[0] = Math.max(needSlot2[0]-e4.getValue()[0], needSlot2[0]-(dou+1));
            	    		} else if (e2==e4) {
            	    			needSlot2[0] = Math.max(needSlot2[0]-e2.getValue()[0], needSlot2[0]-(dou+1)*2);
            	    			needSlot2[0] = Math.max(needSlot2[0]-e3.getValue()[0], needSlot2[0]-(dou+1));
            	    		} else if (e3==e4) {
            	    			needSlot2[0] = Math.max(needSlot2[0]-e3.getValue()[0], needSlot2[0]-(dou+1)*2);
            	    			needSlot2[0] = Math.max(needSlot2[0]-e2.getValue()[0], needSlot2[0]-(dou+1));
            	    		} else {
            	    			needSlot2[0] = Math.max(needSlot2[0]-e2.getValue()[0], needSlot2[0]-(dou+1));
            	    			needSlot2[0] = Math.max(needSlot2[0]-e3.getValue()[0], needSlot2[0]-(dou+1));
            	    			needSlot2[0] = Math.max(needSlot2[0]-e4.getValue()[0], needSlot2[0]-(dou+1));
            	    		}
            				Map.Entry<Boolean, Integer[]> slotOkResult2 = slotOK(slot, needSlot2);
            				Boolean available2 = slotOkResult2.getKey();
            	    		ArrayList<Integer[]> slot_dou2 = new ArrayList<>();
            	    		if (e2==e3 && e3==e4) {
            	    			slot_dou2.add(new Integer[]{e2.getKey(), 1, 3});
            	    		} else if (e2==e3) {
            	    			slot_dou2.add(new Integer[]{e2.getKey(), 1, 2});
                	    		slot_dou2.add(new Integer[]{e4.getKey(), 1, 1});
            	    		} else if (e2==e4) {
            	    			slot_dou2.add(new Integer[]{e2.getKey(), 1, 2});
                	    		slot_dou2.add(new Integer[]{e3.getKey(), 1, 1});
            	    		} else if (e3==e4) {
            	    			slot_dou2.add(new Integer[]{e3.getKey(), 1, 2});
                	    		slot_dou2.add(new Integer[]{e2.getKey(), 1, 1});
            	    		} else {
            	    			slot_dou2.add(new Integer[]{e2.getKey(), 1, 1});
            	    			slot_dou2.add(new Integer[]{e3.getKey(), 1, 1});
                	    		slot_dou2.add(new Integer[]{e4.getKey(), 1, 1});
            	    		}
            	    		Integer[] margin2 = slotOkResult2.getValue();
            	    		if (available2 && margin[0]+margin[1]*2+margin[2]*3 < margin2[0]+margin2[1]*2+margin2[2]*3) {
            	    			available = available2;
        	    				slot_dou = slot_dou2;
        	    				margin = margin2;
            	    		}
    					}
        			}
    			}
    			return new CustomSlot(available, slot_dou, margin);
    		}
		}
    }
    
    
    public static Map.Entry<Boolean, Integer[]> slotOK(Integer[] slot, Integer[] needSlot) {
		Integer[] slot2 = new Integer[3];
		Integer[] needSlot2 = new Integer[3];
		System.arraycopy(slot, 0, slot2, 0, 3);
		System.arraycopy(needSlot, 0, needSlot2, 0, 3);
		Boolean result = true;
		if (needSlot2[2] > slot2[2])
			result = false;
		else {
			slot2[2] = slot2[2] - needSlot2[2];
			if (needSlot2[1] > slot2[1]+slot2[2]) {
				result = false;
			} else {
				if(needSlot2[1] <= slot2[1]) {
					slot2[1] -= needSlot2[1];
				} else {
					Integer a = needSlot2[1]-slot2[1];
					slot2[0] += a;
					slot2[1] = 0;
					slot2[2] -= a;
				}
				if(needSlot2[0] > slot2[0]+slot2[1]*2+slot2[2]*3) {
					result = false;
				} else {
					if(needSlot2[0] <= slot2[0]) {
						slot2[0] -= needSlot2[0];
					} else if(needSlot2[0] <= slot2[0]+slot2[1]*2) {
						Integer b = needSlot2[0]-slot2[0];
						switch(b % 2){
						case 0:
							slot2[0] = 0;
							slot2[1] -= b/2;
							break;
						case 1:
							slot2[1] -= (b+1)/2;
							slot2[0] += 1;
						}
					} else {
						Integer c = needSlot2[0]-slot2[0]-slot2[1]*2;
						switch(c % 3){
						case 0:
							slot2[0] = 0;
							slot2[1] = 0;
							slot2[2] -= c/3;
							break;
						case 1:
							slot2[0] = 0;
							slot2[1] = 1;
							slot2[2] -= (c+2)/3;
						case 2:
							slot2[0] = 1;
							slot2[1] = 0;
							slot2[2] -= (c+1)/3;
						}
					}
				}
			}
		}
		Map.Entry<Boolean, Integer[]> output;
		output = new AbstractMap.SimpleEntry<>(result, slot2);
		return output;
	}
}
