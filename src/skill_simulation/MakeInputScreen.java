package skill_simulation;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MakeInputScreen extends JFrame implements ActionListener {
    // フィールド宣言
    JComboBox<Integer> slot_wepon;
    JComboBox<Integer> slot_oma;
    JComboBox<Integer> point1_omamori;
    JComboBox<Integer> point2_omamori;
    JComboBox<String> profession;
    JComboBox<String> gender;
    JComboBox<String> skill1_omamori;
    JComboBox<String> skill2_omamori;
    JCheckBox[] required_skill;
    JButton btn;
    
    public static Object[][] sword;
    public static Object[][] ganner;
    public static Object[][] ornament;
    public static Object[][] skill;
    public static Integer displayNumber = 10;
    
    private Integer panel_width;
    private Integer panel_hight;
    private Integer retu;

    // コンストラクタ
    MakeInputScreen(){
        try {
            // Excel読み込み
			ReadExcelFile read_sword = new ReadExcelFile("excel_data/all_DataSet.xlsx", "sword_headAdd");
            ReadExcelFile read_ganner = new ReadExcelFile("excel_data/all_DataSet.xlsx", "ganner_headAdd");
            ReadExcelFile read_ornament = new ReadExcelFile("excel_data/all_DataSet.xlsx", "ornament");
            ReadExcelFile read_skill = new ReadExcelFile("excel_data/all_DataSet.xlsx", "skill_order");
            sword = read_sword.getData();
            ganner = read_ganner.getData();
            ornament = read_ornament.getData();
            skill = read_skill.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //skillからポイントがプラスのもののみ取り出して表示用
        Object[][] plus = new Object[skill.length-1][3];
		int count = 0;
		// 元の配列をループしてk列目の数値が正のものだけを取り出す
	    for (int i = 1; i < skill.length; i++) {
	        if ((int) skill[i][2] > 0) {  // 2列目の値が正の場合
	            plus[count] = skill[i];  // i行目をcount行目にコピー
	            count++;
	        }
	    }
	    // 新しい配列を切り詰める
	    Object[][] plusOnly = new Object[count][2];
	    System.arraycopy(plus, 0, plusOnly, 0, count);
        
        panel_width = 1000;
        panel_hight = 1000;
        retu = 6;
        // プルダウンの追加
        Integer[] slotNumber = {0, 1, 2, 3};
        Integer[] pointNumber = new Integer[16];
        for (int i = 0; i < 16; i++) {
            pointNumber[i] = i;
        }
        String[] sword_gan = {"剣士", "ガンナー"};
        String[] man_woman = {"男性", "女性"};
        String[] hatudou_skill = new String[plusOnly.length];
        for (int i = 0; i < plusOnly.length; i++) {
            hatudou_skill[i] = (String) plusOnly[i][0];
        }
        String[] haifun_skill_keitou = new String[sword[0].length-1];
        haifun_skill_keitou[0] = "-";
        for (int i = 2; i < sword[0].length; i++) {
            haifun_skill_keitou[i-1] = (String) sword[0][i];
        }

        
        slot_wepon = new JComboBox<>(slotNumber);
        slot_oma = new JComboBox<>(slotNumber);
        point1_omamori = new JComboBox<>(pointNumber);
        point2_omamori = new JComboBox<>(pointNumber);
        profession = new JComboBox<>(sword_gan);
        gender = new JComboBox<>(man_woman);
        skill1_omamori = new JComboBox<>(haifun_skill_keitou);
        skill2_omamori = new JComboBox<>(haifun_skill_keitou);

        slot_wepon.setSelectedItem(0);
        slot_oma.setSelectedItem(0);
        point1_omamori.setSelectedItem(0);
        point2_omamori.setSelectedItem(0);
        profession.setSelectedItem("剣士");
        gender.setSelectedItem("男性");
        skill1_omamori.setSelectedItem("-");
        skill2_omamori.setSelectedItem("-");

        point1_omamori.setMaximumRowCount(pointNumber.length);
        point2_omamori.setMaximumRowCount(pointNumber.length);
        skill1_omamori.setMaximumRowCount(haifun_skill_keitou.length);
        skill2_omamori.setMaximumRowCount(haifun_skill_keitou.length);

        required_skill = new JCheckBox[hatudou_skill.length];
        for (int i = 0; i < hatudou_skill.length; i++) {
            required_skill[i] = new JCheckBox(hatudou_skill[i]);
        }

        // GUI画面の設定
        getContentPane().setLayout(new FlowLayout());
        
        //ボタン
        btn = new JButton("確認");
        btn.addActionListener(this);
        
        // プルダウンを画面に追加
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // 垂直方向に配置
        
        // 表形式で項目を表示するためのパネル
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(2, 7, 5, 5)); // 2行7列のグリッドレイアウト (スペース調整用にマージンを追加)
        
        // 1行目（ラベル部分）
        gridPanel.add(new JLabel("剣士 or ガンナー"));
        gridPanel.add(new JLabel("性別"));
        gridPanel.add(new JLabel("武器のスロット数"));
        gridPanel.add(new JLabel("お守りのスロット数"));
        gridPanel.add(new JLabel("お守りスキル1つ目"));
        gridPanel.add(new JLabel("スキルポイント"));
        gridPanel.add(new JLabel("お守りスキル2つ目"));
        gridPanel.add(new JLabel("スキルポイント"));
        
        // 2行目（コンポーネント部分）
        gridPanel.add(profession);
        gridPanel.add(gender);
        gridPanel.add(slot_wepon);
        gridPanel.add(slot_oma);
        gridPanel.add(skill1_omamori);
        gridPanel.add(point1_omamori);
        gridPanel.add(skill2_omamori);
        gridPanel.add(point2_omamori);
        
        // 欲しいスキルのラベルとチェックボックスを追加
        JPanel skillLabelPanel = new JPanel();
        skillLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        skillLabelPanel.add(new JLabel("欲しいスキル"));
        
        // チェックボックスを1行にretu個ずつ配置
        JPanel skillPanel = new JPanel();
        skillPanel.setLayout(new GridLayout(0, retu)); // 1行にretu個配置
        for (JCheckBox skill : required_skill) {
            skillPanel.add(skill); // 各チェックボックスを追加
        }
        // メインパネルにすべてを追加
        panel.add(gridPanel);      // グリッド形式の設定
        panel.add(skillLabelPanel); // "欲しいスキル"のラベル
        panel.add(skillPanel);     // スキルのチェックボックス
        panel.add(btn);            // 確認ボタン

        // スクロール可能なパネル
        JScrollPane scrollPane = new JScrollPane(panel);

        // スクロールを追加
        getContentPane().add(scrollPane);

        // GUIを閉じた時、プログラムも終了
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // サイズを設定
        setSize(panel_width, panel_hight);

        // GUI画面の可視化
        setVisible(true);
    }

    // ボタンクリック時の処理
    public void actionPerformed(ActionEvent ae) {
        // チェックされたチェックボックス項目を収集
        StringBuilder selected_skill = new StringBuilder("欲しいスキル: ");
        for (int i = 0; i < required_skill.length; i++) {
            if (required_skill[i].isSelected()) {
                selected_skill.append(required_skill[i].getText());
            }
        }
        //required_skillから選択されたものだけを集めてString[]型のrequiredArrayを作成
        List<String> list = new ArrayList<>();
        for (int i = 0; i < required_skill.length; i++) {
            if (required_skill[i].isSelected()) {
            	list.add(required_skill[i].getText());
            }
        }
        String[] requiredArray = list.toArray(new String[list.size()]);
        
        //実行時間を測る
        long startTime = System.currentTimeMillis();
        //防具の候補optionsを得る．
        Map.Entry<Integer, String[]> dataMap = MakeOutput.getOptions(
        		profession.getSelectedItem().toString(),
        		gender.getSelectedItem().toString(),
        		(int) slot_wepon.getSelectedItem(),
        		(int) slot_oma.getSelectedItem(),
        		skill1_omamori.getSelectedItem().toString(),
        		(int) point1_omamori.getSelectedItem(),
        		skill2_omamori.getSelectedItem().toString(),
        		(int) point2_omamori.getSelectedItem(),
        		requiredArray
        		);
        long endTime = System.currentTimeMillis();
        Integer combNumber = dataMap.getKey();
        String[] data = dataMap.getValue();
        // 全データをまとめる
        StringBuilder allData = new StringBuilder();
        allData.append("組合せ総数: " + combNumber.toString() + "通り, 実行時間: " + (double)(endTime-startTime)/1000 + "秒").append("\n\n");
        for (String entry : data) {
            allData.append(entry).append("\n\n");
        }
        // テキストエリアに全データを表示
        JTextArea textArea = new JTextArea(allData.toString());
        textArea.setEditable(false); // 編集不可
        textArea.setLineWrap(true);  // テキストの折り返しを有効化
        textArea.setWrapStyleWord(true);

        // スクロール可能なパネルを作成
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400)); // ダイアログのサイズを指定

        // メッセージダイアログを表示
        JOptionPane.showMessageDialog(
            null,
            scrollPane,
            "装備シミュレーション結果一覧",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    public static void main(String[] args) {
    	MakeInputScreen input_screen = new MakeInputScreen();
    }
}
