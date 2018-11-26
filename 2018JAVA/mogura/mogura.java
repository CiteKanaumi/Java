package mainframe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Color;

/*text*/
import java.io.*;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.swing.JTextField;

/*sort*/
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

class MainFrame extends JFrame{
    public String[] PanelNames = { "Title", "Game", "Ranking", "HowtoPlay" };
    GameTitlePanel gtp = new GameTitlePanel(this, PanelNames[0]);
    GamePanel gp = new GamePanel(this, PanelNames[1]);
    RankingPanel rp = new RankingPanel(this, PanelNames[2]);
    HowtoPlayPanel hpp = new HowtoPlayPanel(this, PanelNames[3]);

    public MainFrame() {
	this.add(gtp); gtp.setVisible(true);
	this.add(gp); gp.setVisible(false);
	this.add(rp); rp.setVisible(false);
	this.add(hpp); hpp.setVisible(false);
	this.setBounds(100, 100, 516, 638);
    }

    public static void main(String[] args) {
	MainFrame mf = new MainFrame();
	mf.setDefaultCloseOperation(EXIT_ON_CLOSE);
	mf.setVisible(true);
    }

    /* パネルの変更 */
    public void PanelChange(JPanel jp, String str){
	String name = jp.getName();
	if(name==PanelNames[0])
	    gtp = (GameTitlePanel)jp; gtp.setVisible(false);
	if(name==PanelNames[1])
	    gp = (GamePanel)jp; gp.setVisible(false);
	if(name==PanelNames[2])
      rp = (RankingPanel)jp; rp.setVisible(false);
	if(name==PanelNames[3])
	    hpp = (HowtoPlayPanel)jp; hpp.setVisible(false);

	if(str==PanelNames[0])
	    gtp.setVisible(true);
	if(str==PanelNames[1])
	    gp.setVisible(true);
	if(str==PanelNames[2])
	  {
      rp.setVisible(true);

    }
	if(str==PanelNames[3])
	    hpp.setVisible(true);
    }
}

/* タイトルの画面 ********************************************************/
class GameTitlePanel extends JPanel {
    JButton GameButton, RankingButton, HowtoPlayButton;
    MainFrame mf;
    String str;

    public GameTitlePanel(MainFrame m, String s) {
        mf = m;
        str = s;
        this.setName("Title");
        this.setLayout(null);
        this.setSize(500, 600);
	this.setBackground(Color.cyan);

	/* ゲームタイトル */
	JLabel l = new JLabel("<html><span style='font-size:62pt; color:blue; "+"background-color:yellow;'>もぐらたたき</span></html>");
	this.add(l);
	l.setBounds(50, 50, 400, 150);

	/* ゲームスタートボタン */
	GameButton = new JButton("ゲームスタート");
        this.add(GameButton);
        GameButton.setBounds(100, 250, 300, 50);
	GameButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
		    pc(mf.PanelNames[1]);
		}
	    });

	/* ランキングボタン */
        RankingButton = new JButton("ランキング");
        this.add(RankingButton);
        RankingButton.setBounds(100, 320, 300, 50);
	RankingButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
		    pc(mf.PanelNames[2]);
		}
	    });

	/* 遊び方ボタン */
        HowtoPlayButton = new JButton("遊び方");
        this.add(HowtoPlayButton);
        HowtoPlayButton.setBounds(100, 390, 300, 50);
        HowtoPlayButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
		    pc(mf.PanelNames[3]);
		}
	    });
    }

    public void pc(String str){
        mf.PanelChange((JPanel)this, str);
    }
}

/* ゲームの画面 *****************************************************/
class GamePanel extends JPanel implements ActionListener{

    Timer time;  //ランダムで回す用
    MainFrame mf;
    String str;

    int z = 30;   //残りtime
    int s = 0;    //score
    JLabel score; //文字(メソッドで呼ぶこともないけど一応)
    JLabel zanki; //文字(同上): 歴史的な都合でzankiのままだけど、残り時間
    JPanel masu;  //もぐらが出てくるマス
    JButton b[] = new JButton[20];//masu内に配置
    int i=0; //forループ:ボタン設置などいろいろ使い回す要員

    int r = 0;   //randam


    result R;

    public GamePanel(MainFrame m, String s) {
	mf = m;
	str = s;
	this.setName("GAME");
	this.setLayout(null);
        this.setSize(500, 600);


	this.setLayout(new BorderLayout());

	// Score ****************************************************************

	//文字"time"と文字"score"を分けて置くためのJPanelを用意

	JPanel hyouji = new JPanel();
	this.add(hyouji,BorderLayout.NORTH);
	hyouji.setLayout(new GridLayout(1,2));

	JLabel Moji = new JLabel("<html><span style='font-size:24pt;'>"
				 +"time</span><html>");
	hyouji.add(Moji);

	JLabel moji = new JLabel("<html><span style='font-size:24pt;'>"
				 +"score</span><html>",JLabel.RIGHT);
	hyouji.add(moji);

        score = new JLabel("<html><span style='font-size:24pt;'>"
			   +"0</span><html>");
	this.add(score,BorderLayout.EAST);

        zanki = new JLabel("<html><span style='font-size:24pt;'>"
        +"push any button!</span><html>");///////////////25日追加
	this.add(zanki,BorderLayout.WEST);

	// クリックするところ ********************************************************

        masu = new JPanel();//パネルのなかにマスを配置
	masu.setPreferredSize(new Dimension(300,400));
	this.add(masu,BorderLayout.SOUTH);
       	masu.setLayout(new GridLayout(4,5));

	for(i=0; i<20; i++) {
		b[i] = new JButton();
		b[i].setPreferredSize(new Dimension(10,10));
		masu.add(b[i]);
		b[i].setBackground(Color.BLACK);
		b[i].addActionListener(this);
		b[i].setActionCommand(i+"");
	}

	time = new Timer(500, new Exlistener());//500ミリ秒毎にactionListener : Exlistener呼び出し

    	this.setVisible(true);
     }//end of GAME()

   // メソッド *********************************************************************


    public void actionPerformed(ActionEvent ev) {
    	time.start();
	String T = ev.getActionCommand();
	//(もぐらが頭を出してるとき)
	if(T.equals(r+"")) s++;
	int z2=z/2;
	score.setText("<html><span style='font-size:24pt;'>"+s+"</span><html>");
	zanki.setText("<html><span style='font-size:24pt;'>"+z2+"</span><html>");
	/*
	//終了条件
	if(z<=0)
	    {

		//スコアを一度textfileに送って，間接的にresultクラスへ渡す
		try{
		    // FileWriterクラスのオブジェクトを生成する
		    File file = new File("Temp.dat");
		    // PrintWriterクラスのオブジェクトを生成する
		    FileWriter fw = new FileWriter(file);
		    fw.write(Integer.toString(s));
		    //ファイルを閉じる
		    fw.close();
		}
		catch (IOException e) {e.printStackTrace();}

	    }
	*/
    }//end of actionPerformed

    //タイマーで常時:ボタン押さなくても動く部分
    class Exlistener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    r = (int)(Math.random() * 20);


	    for(i = 0;i<20;i++)
		{
		    if(r == i) {
			    b[i].setBackground(Color.BLACK);
			}
		    else { b[i].setBackground(Color.LIGHT_GRAY); }
		}//end of randamによる色の変更

	    //カウントダウン
	    --z;
	    int z2 = z/2;
	    zanki.setText("<html><span style='font-size:24pt;'>"+z2+"</span><html>");

	    //終了条件 25日追加：念のため==でなく<=にしたいけど...要らん？
	    if(z<=0)
		{
		    time.stop();
		    //スコアを一度textfileに送って，間接的にresultクラスへ渡す
		    try {
			// FileWriterクラスのオブジェクトを生成する
			File file = new File("Temp.dat");
			// PrintWriterクラスのオブジェクトを生成する
			FileWriter fw = new FileWriter(file);
			fw.write(Integer.toString(s));
			fw.close(); //ファイルを閉じる
		    }
		    catch (IOException ee) {ee.printStackTrace();}

		    R = new result();
        //R.setVisible(true);/////////////////////
		    pc();
		    z=30;
		    s=0;

        /*ここ追加（25日）*/
        score.setText("<html><span style='font-size:24pt;'>"+s+"</span><html>");
      	zanki.setText("<html><span style='font-size:24pt;'>"
        +"push any button!</span><html>");

        for(i = 0;i<20;i++)
        {
          b[i].setBackground(Color.BLACK);
        }
        /**/
    }
	}
    }//end of Ex.class in GAME.class

    public void pc(){
        mf.PanelChange((JPanel)this, mf.PanelNames[0]);
    }
}


/* ランキングの画面 ***************************************************/
class RankingPanel extends JPanel {
    JButton back = new JButton("戻る");
    JButton re = new JButton("更新");
    MainFrame mf;
    String str;
    JLabel l[] = new JLabel[11];

    public RankingPanel(MainFrame m, String s){
	mf = m;
	str = s;
	this.setName("Ranking");
	this.setLayout(null);
        this.setSize(500, 600);
	this.setBackground(Color.lightGray);
	
	/* Button */
	this.add(back);
    back.setBounds(10, 10, 80, 30);
    back.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e){
            pc();
        }
	});
    this.add(re);
    re.setBounds(410, 10, 80, 30);
    re.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e){
    		read();
    	}
    });

	/* Label */
	for(int i=0; i<10; i++)
	    {
		if(i == 0) {
		    l[i] = new JLabel();
		    this.add(l[i]);
		    l[i].setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 40));
		    l[i].setForeground(Color.red);
		    l[i].setBounds(50, 50 * i + 60, 400, 50);
		}
		else if(i == 1) {
		    l[i] = new JLabel();
		    this.add(l[i]);
		    l[i].setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 35));
		    l[i].setForeground(Color.red);
		    l[i].setBounds(77, 50 * i + 70, 400, 50);
		}
		else if(i == 2) {
		    l[i] = new JLabel();
		    this.add(l[i]);
		    l[i].setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 30));
		    l[i].setForeground(Color.red);
		    l[i].setBounds(115, 50 * i + 80, 400, 50);
		}
		else if(i == 9) {
		    l[i] = new JLabel();
		    this.add(l[i]);
		    l[i].setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 25));
		    l[i].setBounds(150, 50 * i + 100, 400, 40);
		}
		else {
		    l[i] = new JLabel();
		    this.add(l[i]);
		    l[i].setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 25));
		    l[i].setBounds(165, 50 * i + 100, 400, 40);
		}
	    }
	read();
	
	/* view */
	this.setVisible(true);
    }
    
    public void read(){
    	/* text read */
    	int i=0;
    	String result[] = new String[100];
    	try{
    	    File file = new File("Score.dat");
    	    BufferedReader br = new BufferedReader(new FileReader(file));
    	    String str = br.readLine();
    	    while(str != null)
    		{
    		    result[i]=str;
    		    str = br.readLine();
    		    i++;
    		}
    	    br.close();
    	}
    	catch(IOException e){ System.out.println(e); }
    	int j = i;
    	
    	/* sort */
    	ArrayList<String> list = new ArrayList<>();
    	for(i=0; i<j; i++)
    	    {
    		list.add(result[i]);
    	    }
    	Comparator<String> reverseOrder = Collections.reverseOrder();
    	Collections.sort(list, reverseOrder);
    	Object[] objs = list.toArray(new String[list.size()]);
    	result = (String[])objs;

    	/* print */
    	for(i=0; i<10; i++) { l[i].setText((i+1)+" 位 "+result[i]); }
    }

    public void pc() {
	mf.PanelChange((JPanel)this, mf.PanelNames[0]);
    }
}

/* 遊び方の画面 **********************************************************/
class HowtoPlayPanel extends JPanel {
    JButton back = new JButton("戻る");
    JLabel gl = new JLabel("<html><span style='font-size:22pt; color:black; '>・色の変わったボタンをクリックするだけ</span></html>");
    JLabel rl = new JLabel("<html><span style='font-size:22pt; color:black; '>・名前を入力してスコアを記録しよう</span></html>");
    JLabel gjpg = new JLabel("<html><img src='file:game.jpg' width=150 height=180></html>");
    JLabel rjpg = new JLabel("<html><img src='file:result.jpg' width=150 height=180></html>");
    MainFrame mf;
    String str;

    public HowtoPlayPanel(MainFrame m, String s) {
        mf = m;
        str = s;
        this.setLayout(null);
        this.setName("HowtoPlay");
        this.setSize(500, 600);
	this.setBackground(Color.cyan);
        this.add(gl);
        gl.setBounds(20, 50, 440, 30);
        this.add(rl);
        rl.setBounds(20, 300, 450, 30);       
        this.add(gjpg);
        gjpg.setBounds(150, 100, 150, 180);
        this.add(rjpg);
        rjpg.setBounds(150, 350, 150, 180);
        this.add(back);
        back.setBounds(10, 10, 80, 30);
        back.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    pc();
                }
	    });
    }

    public void pc(){
        mf.PanelChange((JPanel)this, mf.PanelNames[0]);
    }
}

/* ゲーム終了後のリザルト画面のクラス ***************************************/
class result extends JFrame implements ActionListener {
    public String sc;//文字列と化したスコア
    public JPanel select;//リトライとかの「選択」ボタンを用意しようとしたときのなごり
    public JPanel shita;//textfieldとokボタンを入れるためのパネル
    public JButton OK;
    public JTextField na;//入力する名前のフィールド
    public String NAME;//入力した名前をStringで受けるための変数

    result() {
	this.setTitle("result");
	this.setBounds(200,200,300,400);
	//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	try{
	    File file = new File("Temp.dat");///////txt->dat
	    BufferedReader br = new BufferedReader(new FileReader(file));
	    String str;
	    while((str = br.readLine()) != null) { sc=str; }
	}
	catch (IOException e) { System.out.println(e); }

	select = new JPanel();
	select.setPreferredSize(new Dimension(300,500));
	this.add(select,BorderLayout.CENTER);
	select.setLayout(new GridLayout(3,1));
	//(上)正直サイズ調整めんどうだった、(3,1)変えると変なところに表示された気がする
	//ここの例外処理だけは必要性が微妙だったので、念のため残しときます
	//なくてもいいと思ってる、実行してFAILEDしたことない
	if(Integer.valueOf(sc).intValue()<=0)
	    //万が一 z<=0||s>=5 以外でここ来たときのために if のないただの elseにした
	    { JLabel res = new JLabel("FAILED..."); }

	//ここからは必要
	JLabel res = new JLabel("YOUR SCORE "+sc);
	select.add(res);
	res.setBounds(10,10,100,50);
	shita = new JPanel();
	shita.setPreferredSize(new Dimension(30,50));
	this.add(shita,BorderLayout.SOUTH);
	shita.setLayout(new GridLayout(2,1));
	na = new JTextField("Name");
	shita.add(na);
	OK = new JButton("OK");
	OK.addActionListener(this);
	OK.setBounds(50,50,50,50);
	shita.add(OK);
	this.setVisible(true);
    }

    //OKボタンおした時
    public void actionPerformed(ActionEvent ev) {
	//textfieldからString nameを確保
	NAME = na.getText();
	//score.txtに保存
	try {
	    // FileWriterクラスのオブジェクトを生成する
	    FileWriter file = new FileWriter("Score.dat", true);
	    // PrintWriterクラスのオブジェクトを生成する
	    PrintWriter pw = new PrintWriter(new BufferedWriter(file));
	    //ファイルに追記する
	    pw.println(String.format("%3s",sc).replace(" ","0")+"  "+NAME);
	    pw.close(); //ファイルを閉じる
	    this.setVisible(false);
	} catch (IOException e) { e.printStackTrace(); }
    }
}//end of result.class
