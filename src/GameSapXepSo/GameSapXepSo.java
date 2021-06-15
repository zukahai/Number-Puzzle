package GameSapXepSo;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.*;

public class GameSapXepSo extends JFrame implements KeyListener, ActionListener{
	Color colorYes = Color.black; // Màu nền của ô trống đã ĐÚNG vị trí.
	Color colorNo = Color.yellow; // Màu nền của ô trống đã SAI vị trí.
	Color colorNumber = Color.green; // Màu của chữ số.
	Color colorBox = Color.LIGHT_GRAY; // Màu của ô trống.
	Timer timer;
	int maxSize = 1001;
	int indexI, indexJ; // tọa độ của ô trống.
	int n; // lưu kích thước của cạnh và hàng trong mảng.
	String name, sdt;
	private Container cn;
	private JPanel pn, pn2, pn3; 
	int sizeW [] = {250, 600, 680};
	int sizeH [] = {350, 700, 850};
	private JButton b[][] = new JButton[maxSize][maxSize]; // một mảng hai chiều là các button.
	private JButton highScore_bt, newGame_bt;
	private JLabel time_lb;
	public GameSapXepSo(int SIZE) {
		super("HaiZuka - Number Puzzle");
		this.name = name;
		this.sdt = sdt;
		cn = this.getContentPane();
		n = SIZE;
		if (n > 5)
			n = 3;
		time_lb = new JLabel("00:00:00:00");
		time_lb.setFont(new Font("Arial", 1, 20));
		time_lb.addKeyListener(this);
		pn2 = new JPanel();
		pn2.setLayout(new FlowLayout());
		pn2.add(time_lb);
		
		highScore_bt = new JButton("High Score");
		highScore_bt.addActionListener(this);
		highScore_bt.addKeyListener(this);
		
		newGame_bt = new JButton("New game");
		newGame_bt.addActionListener(this);
		newGame_bt.addKeyListener(this); 
		
		pn3 = new JPanel();
		pn3.setLayout(new FlowLayout());
		pn3.add(newGame_bt);
		pn3.add(highScore_bt);
		
		pn = new JPanel();
		pn.setLayout(new GridLayout(n,n));
		// khởi tạo ma trân mặc định.
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= n; j++){
				b[i][j] = new JButton(String.valueOf(n*(i-1)+j));
				b[i][j].addKeyListener(this);
				b[i][j].setForeground(colorNumber);
				b[i][j].setFont(new Font("Arial",Font.BOLD,72));
				pn.add(b[i][j]);
			}
		}
		int i1, j1, i2, j2;
		for (int k = 1; k <= 2*n*n; k++) {
			do {
				i1 = (int) (Math.round((n-1)*Math.random()+1));;
				j1 = (int) (Math.round((n-1)*Math.random()+1));
			} while (i1 == n && j1 == n);
			do {
				i2 = (int) (Math.round((n-1)*Math.random()+1));
				j2 = (int) (Math.round((n-1)*Math.random()+1));
			} while ((i2 == n && j2 == n) ||(i2 == i1&&j2 == j1));
			String p = b[i1][j1].getText();
			b[i1][j1].setText(b[i2][j2].getText());
			b[i2][j2].setText(p);
		}
		b[n][n].setText("");
		b[n][n].setBackground(colorBox);
		// Gọi hàm cập nhật màu nền của các ô trống
		updateColor();
		cn.add(pn2, "North");
		cn.add(pn);
		cn.add(pn3, "South");
		this.setVisible(true);
		this.setSize(sizeW[n - 3], sizeH[n - 3]);
		this.setLocationRelativeTo(null);
		setResizable(false);
		indexI = n; indexJ = n;
		timer = new Timer(10, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				time_lb.setText(next(time_lb));
			}
		});
	}
	
	public String next(JLabel lb) {
		String str[] = lb.getText().split(":");
		int tt = Integer.parseInt(str[3]);
		int s = Integer.parseInt(str[2]);
		int m = Integer.parseInt(str[1]);
		int h = Integer.parseInt(str[0]);
		String kq = "";
		int sum = tt + s * 100 + m * 60 * 100 + h * 60 * 60 * 100 + 1;
		if (sum % 100 > 9)
			kq = ":" + sum % 100 + kq;
		else
			kq = ":0" + sum % 100 + kq;
		sum /= 100;
		
		if (sum % 60 > 9)
			kq = ":" + sum % 60 + kq;
		else
			kq = ":0" + sum % 60 + kq;
		sum /= 60;
		
		if (sum % 60 > 9)
			kq = ":" + sum % 60 + kq;
		else
			kq = ":0" + sum % 60 + kq;
		sum /= 60;
		if (sum > 9)
			kq = sum + kq;
		else
			kq = "0" + sum +kq;
		return kq;
	}
	
	// Hàm cập nhập màu.
	public void updateColor(){
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= n; j++) {
				if (b[i][j].getText() != "") {
					if (Integer.parseInt(b[i][j].getText()) == n*(i-1)+j) {
						b[i][j].setBackground(colorYes);
					}
					else {
						b[i][j].setBackground(colorNo);
					}
				} else {
					b[i][j].setBackground(colorBox);
				}
			}	
		}
	}
	// Hàm kiểm tra hoàn thành màn chơi đó.
	public void checkWin() {
		if (b[n][n].getText()=="") {
			b[n][n].setText(String.valueOf(n*n));
			boolean kt = true;
			for (int i = 1; i <= n; i++) {
				for (int j = 1; j <= n; j++) {
					if (Integer.parseInt(b[i][j].getText()) != n*(i-1)+j) kt = false;					
				}
			}
			if (kt) {
				this.dispose(); // Đóng cửa số màn hình hiển tại.
				new GameSapXepSo(n + 1);
			} else {
				b[n][n].setText(String.valueOf(""));
			}
		}
	}
	
	// Xử lý khi gõ phím
	public void keyPressed(KeyEvent e) {
		timer.start();
		if (e.getKeyCode()==KeyEvent.VK_ESCAPE) System.exit(0); // thoát chương trình
		if (e.getKeyCode()==KeyEvent.VK_DOWN) { // khi bấm phím xuống: Hoán đổi vị trị của ôn trống với ô phím trên nó.
			if (indexI > 1) {
				String s = b[indexI][indexJ].getText();
				b[indexI][indexJ].setText(b[indexI-1][indexJ].getText());
				b[indexI-1][indexJ].setText(s);
				indexI--;
			}
		}
		
		if (e.getKeyCode() == KeyEvent.VK_UP) { //khi bấm phím lên: Hoán đổi vị trị của ôn trống với ô phím dưới nó.
			if (indexI < n) {
				String s = b[indexI][indexJ].getText();
				b[indexI][indexJ].setText(b[indexI+1][indexJ].getText());
				b[indexI+1][indexJ].setText(s);
				indexI++;
			}
		}
		
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {//khi bấm phím sang phải: Hoán đổi vị trị của ôn trống với ô bên trái nó.
			if (indexJ > 1) {
				String s = b[indexI][indexJ].getText();
				b[indexI][indexJ].setText(b[indexI][indexJ-1].getText());
				b[indexI][indexJ-1].setText(s);
				indexJ--;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) { //khi bấm phím sang trái: Hoán đổi vị trị của ôn trống với ô bên phải nó.
			if (indexJ < n) {
				String s = b[indexI][indexJ].getText();
				b[indexI][indexJ].setText(b[indexI][indexJ+1].getText());
				b[indexI][indexJ+1].setText(s);
				indexJ++;
			}
		}
		checkWin();
	}
	public void keyReleased(KeyEvent e) {
		updateColor();
	}
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().endsWith(highScore_bt.getText())) {
		}
		else if (e.getActionCommand().endsWith(newGame_bt.getText())) {
			new GameSapXepSo(3);
			this.dispose();
		}
	}
	
	public static void main(String[] args) {
		new GameSapXepSo(3);
	}
}

