package GameSapXepSo;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class HighScore extends JFrame implements ActionListener{
	int level = 1;
	Container cn;
	private JLabel  chonLv_Lb;
	private JButton[]lv_bt = new JButton[5];
	DefaultTableModel model;
	JTable tb;
	JScrollPane jsc;
	public HighScore (int lv) {
		super("Game Sắp xếp số- VKU");
		level = lv;
		cn = init();
	}
	Container init() {
		
		JPanel pn = new JPanel();
		pn.setLayout(new FlowLayout());
		chonLv_Lb = new JLabel("Level");
		pn.add(chonLv_Lb);
		for (int i = 0; i < lv_bt.length; i++) {
			lv_bt[i] = new JButton(String.valueOf(i + 1));
			lv_bt[i].addActionListener(this);
			pn.add(lv_bt[i]);
		}
		for (int i = 0; i < lv_bt.length; i++) {
			lv_bt[i].setText((i + 3) + "");
		}
		
		Container cn = this.getContentPane();
		Vector vT = new Vector();
		Vector vD = writeScore();		
		try {	
			vT.clear();
			vT.add("Thứ tự");
			vT.add("Tên");
			vT.add("Số điện thoại");
			vT.add("Điểm số");
			
		}catch (Exception e) {
			
		}
		
		model = new DefaultTableModel(vD, vT);
		tb = new JTable(model);
		tb.setAutoResizeMode(1);
		jsc = new JScrollPane(tb);
		cn.add(pn, "North");
		cn.add(jsc);
		
		this.setVisible(true);
		this.setSize(1000, 600);
//		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		return cn;
	}
	public Vector writeScore() {
		Vector vD = new Vector();
		try {
			URL url = new URL("https://haizukon.000webhostapp.com/HighScore/GameSapXepSo/level_" + level + "/vku.txt");
			URLConnection urlConnection = url.openConnection();
			HttpURLConnection connection = null;
			if(urlConnection instanceof HttpURLConnection) {
				connection = (HttpURLConnection) urlConnection;
			}
			BufferedReader in = new BufferedReader(
			new InputStreamReader(connection.getInputStream()));
			String urlString = "";
			String current;
			while((current = in.readLine()) != null) {
				urlString += current;
			}
//			System.out.println(urlString);
			String str[] = urlString.split("@#@", -1);
//			System.out.println(str[0]);
			for (int i = 0; i < str.length - 1; i += 3) {
				Vector vo = new Vector();
				vo.add(i / 2 + 1);
				vo.add(str[i]);
				vo.add(str[i + 1].substring(0, 4) + "***" + str[i + 1].substring(7));
				vo.add(str[i + 2]);
				vD.add(vo);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return vD;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new HighScore(1);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		new HighScore(Integer.parseInt(e.getActionCommand()) - 2);
		this.dispose();
	}
}
