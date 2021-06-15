## <p align="center"> Làm Game Caro Cở bản Bằng Java </p>
<p align="center"> <img src="https://github.com/zukahai/HaiZuka/blob/master/Images/Number-Puzzle/11.png" alt="Tieude" /> </p>

## Làm Game Sắp Xếp Số Bằng Java

Hiện nay có vô số tựa game hay, đồ họa sống động, nhưng chắc hẳn trong tuổi thơ của nhiều người sẽ gắn liền với tựa game sắp xếp số. Thay vì các bạn tìm lại game sắp xếp số để chơi, thì các bạn hoàn toàn có thể tự tạo ra game đó và chơi theo cách của mình đấy. Bài viết này mình sẽ giúp các bạn lập trình game sắp xếp số vô cùng đơn giản.

### 1. Giới thiệu game sắp xếp số

Game sắp xếp số là một game cơ bản, quen thuộc với nhiều người. Giao diện của game bao gồm một lưới ô vuông kích thước n * n, chứa những con số từ 1 đến n * n và một ô trống.

<p align="center"> <img src="https://github.com/zukahai/HaiZuka/blob/master/Images/Number-Puzzle/2.png" alt="Tieude" /> </p>

Việc của người chơi là dùng các phím ↑ ← ↓ → để chuyển ô trống nhằm mục đích sắp xếp lại các ô vuông đó sao cho giá trị của chúng tăng dần từ trái qua phải và từ trên xuống dưới. Mỗi lần di chuyển bạn sẽ có thể đổi chỗ ô trống cho các ô vuông kề cạnh với nó.

<p align="center"> <img src="https://github.com/zukahai/HaiZuka/blob/master/Images/Number-Puzzle/3.png" alt="Tieude" /> </p>

Trò chơi có nhiều level, sau mỗi level kích thước của của biến n (số hàng và cột) sẽ tăng lên, ngoài ra game còn có thể tính theo thời gian hoàn thành màn chơi (sẽ làm trong phần tới).

### 2. Thiết lập giao diện và khởi tạo dữ liệu

#### 2.1 Thiết lập giao diện.

Với game này, giao diện đơn giản sẽ bao gồm n*n button, với mỗi button sẽ chỉ có 2 thuộc tính là:

- Text: chứa mỗi số hoặc rỗng (với ô trống)
- Màu nền: Dùng để phân biệt những ô đã sắp xếp đúng và chưa đúng vị trí.

```Java
	Color colorYes = Color.black; // Màu nền của ô trống đã ĐÚNG vị trí.
	Color colorNo = Color.yellow; // Màu nền của ô trống đã SAI vị trí.
	Color colorNumber = Color.green; // Màu của chữ số.
	Color colorBox = Color.LIGHT_GRAY; // Màu của ô trống.
	int maxSize = 1001;
	int indexI, indexJ; // tọa độ của ô trống.
	int n; // lưu kích thước của cạnh và hàng trong mảng.
	private Container cn;
	private JPanel pn; 
	private JButton b[][] = new JButton[maxSize][maxSize]; // một mảng hai chiều là các button.
	JButton size;
```

#### 2.2 Khởi tạo dữ liệu

Dữ liệu của game phụ thuộc vào biến n (kích thước của các hàng và cột trong mảng hai chiều) để khởi tạo ra một ma trận (mảng hai chiều).

Đặc điểm của ma trận là:

- Gồm các phần tử từ 1 đến n*n-1 và một ô trống.
- Không có 2 ô nào chứa cùng một giá trị.


Đến đây nhiều bạn đã nghĩ ra cách khởi tạo dữ liệu cho ma trận, nhưng hãy chú ý rằng, không phải cách khởi tạo nào như vậy cũng là cách khởi tạo đúng. Cách khởi tạo đúng là khi nó có thể sắp xếp được dãy về trạng thái chiến thắng.

Ví dụ dưới đây:

<p align="center"> <img src="https://github.com/zukahai/HaiZuka/blob/master/Images/Number-Puzzle/4.png" alt="Tieude" /> </p>

Như hình trên cho dù đã khởi tạo dữ liệu như yêu cầu, nhưng bạn sẽ không bao giờ có thể hoàn thành được vòng chơi đó với dữ liệu như vậy.

Vậy cách tạo dữ liệu như thế nào cho đúng, chúng ta sẽ khởi tạo dữ liệu như sau:

- Khởi tạo một mảng đã được sắp xếp ở trạng thái chiến thắng:
  Ví dụ:
  
  <p align="center"> <img src="https://github.com/zukahai/HaiZuka/blob/master/Images/Number-Puzzle/5.png" alt="Tieude" /> </p>
  
- Một phép hoán đổi sẽ hoán đổi 2 phần từ khác nhau và khác ô trống. Nếu số lần hoán đổi là số chẵn thì mảng chắc chắn sắp xếp được về trạng thái chiến thắng.

```Java
		for (indexI = 1; indexI <= n; indexI++) {
			for (indexJ = 1; indexJ <= n; indexJ++){
				b[indexI][indexJ] = new JButton(String.valueOf(n*(indexI-1)+indexJ));
				b[indexI][indexJ].addKeyListener(this);
				b[indexI][indexJ].setForeground(colorNumber);
				b[indexI][indexJ].setFont(new Font("Arial",Font.BOLD,72));
				pn.add(b[indexI][indexJ]);
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
```
  
### 3. Các hàm xử lý

#### 3.1 Di chuyển ô trống

Việc di chuyển ô trống thực chất là việc hoán đổi text giữa ô trống và ô chỉ định.

Ví dụ khi mình bấm phím xuống (down) thì ta sẽ hoán đổi text của ô trống và text của ô phía trên nó. Nhớ cập nhật tọa độ mới của ô trống:

```Java
		if (e.getKeyCode()==KeyEvent.VK_DOWN) { // khi bấm phím xuống: Hoán đổi vị trị của ôn trống với ô phím trên nó.
			if (indexI > 1) {
				String s = b[indexI][indexJ].getText();
				b[indexI][indexJ].setText(b[indexI-1][indexJ].getText());
				b[indexI-1][indexJ].setText(s);
				indexI--;
			}
		}
```

#### 3.2 Kiếm tra chiến thắng.

Sau mỗi lần hoạt động (ấn phím) chúng ta cần phải kiểm tra chiến thắng, sẽ kiểm tra lần lượt các text của các button xem nó đã đúng vị trí hay chưa.

```Java
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
				// Qua level mới
				new GameSapXepSo("CodeLearn - Game Sắp Xếp Số - Level: "+String.valueOf(Integer.parseInt(size.getText())-1),String.valueOf(Integer.parseInt(size.getText())+1));
			} else {
				b[n][n].setText(String.valueOf(""));
			}
		}
	}
```

Nếu như người chơi đã hoàn thành màn đó thì cửa số trò chơi hiện tại sẽ đóng lại, đồng thời mở ra cửa số của màn chơi mới, với mức độ cao hơn, khó hơn.

#### 3.3 Cập nhật màu nền

Với một game thì màu sắc rất quan trọng. Trong game này cũng vậy, chúng ta sẽ sử dụng 3 màu nền chủ đạo là, màu nền của ô đã đúng vị trí, màu nền của ô chưa đúng vị trí và màu nền của của ô trống. Các bạn có thể thay bằng nhưng màu khác phù hợp với các bạn.

```Java
	public void updateColor(){
		for (int q = 1; q <= n; q++) {
			for (int w = 1; w <= n; w++) {
				if (b[q][w].getText() != "") {
					if (Integer.parseInt(b[q][w].getText()) == n*(q-1)+w) {
						b[q][w].setBackground(colorYes);
					}
					else {
						b[q][w].setBackground(colorNo);
					}
				} else {
					b[q][w].setBackground(colorBox);
				}
			}	
		}
	}
```

### 4. Kết

Trên đây là ý tưởng cũng như cách tạo ra game sắp xếp số một cách đơn giản nhất. Các bạn có thể cải tiến thêm chương trình bằng cách thêm một số nội dung khác ví dụ như thêm thời gian để trò chơi hấp dẫn hơn. Rất hi vọng nhận được những góp ý đến từ tất cả các bạn.



## <p align="center">  :tv: Thanks for whatching :earth_africa: </p>
