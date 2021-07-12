/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Models.DongHoDemNguoc;
import Models.Question;
import Network.SQLServer;
import helper.MayPhatNhac;
import helper.Utility;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author midor
 */
public class UIQuestion extends javax.swing.JFrame implements DongHoDemNguoc.DongHoDemNguocListener {
    // luc toi e co hoi dung event lam gi dung k. 1 vi du thuc te luon ne.

    /**
     * Creates new form UIQuestion
     */
    private List<Question> questions;
    private Question currentQuestion;
    private Question question;
    private int[] questionIndex;
    private int numberCorrectQuestion;
    private final int[] money = {200000, 400000, 600000, 1000000, 2000000, 3000000, 6000000, 10000000, 14000000, 22000000,
        30000000, 40000000, 60000000, 85000000, 150000000};
    private final String[] audioOfTrueAnswers = {"true_a.mp3", "true_b.mp3", "true_c.mp3", "true_d.mp3"};
    private final String[] audioOfAnswers = {"ans_a.mp3", "ans_b.mp3", "ans_c.mp3", "ans_d.mp3"};
    private final String[] audioOfLoseAnswers = {"lose_a.mp3", "lose_b.mp3", "lose_c.mp3", "lose_d.mp3"};
    private final JButton[] buttons;
    private DongHoDemNguoc dongHo = new DongHoDemNguoc(15);

    // tạo biến đếm.
    private int count = 0;

    private String name;

    public UIQuestion(String name, int numberCorrectQuestion) {
        this.name = name;
        this.numberCorrectQuestion = numberCorrectQuestion;
        initComponents();

        dongHo.setListener(this);
        lblName.setText(name);
        taoCacCauHoi(1);
        lbl2trieu.setForeground(Color.red);

        buttons = new JButton[]{btnA, btnB, btnC, btnD};
    }

    // a dùng hàm này tạo 1 list câu hỏi hỏi r nè. 
    private void taoCacCauHoi(int lv) {
        // lấy các câu hỏi từ database.
        SQLServer sql = new SQLServer();
        questions = sql.getQuestionList(lv);

        // dựa vào số lượng câu hỏi để tạo mảng index. đáp án thì chắc chắn có 4 đáp án nên  tạo đc
        //sẵn mảng 0,1,2,3. nhưng giờ  k biết bao nhiêu câu hỏi cả.
        questionIndex = new int[questions.size()];
        // giờ thay đổi giá trị của mảng này thành từ 1 tới số câu hỏi trừ 1 ( vì là index mà).
        for (int i = 0; i < questionIndex.length; i++) {
            questionIndex[i] = i;
        }
        // giờ mới random nè.
        Utility.shuffleArray(questionIndex);
    }

    private void hienThiCauHoi(Question question) {
        // đương nhiên hiên =r thị đáp án cũng là 1 phần việc của hiển thị câu hỏi
        lblCauHoi.setText(question.getTitle());

        lblQuestion.setText(Integer.toString(numberCorrectQuestion + 1));

        // 
        hienThiDapAn(question);

        // khi e hien thi cau hoi xong se bat dau tinh gio dung k.dung a.
        // e co the ghi lai ben class DongHo neu dang chay ma batDau BamGio thi khoi dong lai
        dongHo.setListener(this);
        dongHo.batDauBamGio();

    }

    private void hienThiDapAn(Question question) {

        int[] answerIndexs = {0, 1, 2, 3};
        Utility.shuffleArray(answerIndexs); // random vị trí 

        btnA.setText(question.getAnswers(answerIndexs[0]));
        btnB.setText(question.getAnswers(answerIndexs[1]));
        btnC.setText(question.getAnswers(answerIndexs[2]));
        btnD.setText(question.getAnswers(answerIndexs[3]));
    }
//tạo hàm đổi câu hỏi .(cập nhật câu hỏi mới)

    private void newQuestion() {
        if (this.numberCorrectQuestion == 5) {
            count = 0;
            taoCacCauHoi(2);
        }
        if (this.numberCorrectQuestion == 7) {
            count = 0;
            taoCacCauHoi(3);
        }
        if (this.numberCorrectQuestion == 10) {
            count = 0;
            taoCacCauHoi(4);
        }
        currentQuestion = questions.get(questionIndex[count]);
        count++;
        hienThiCauHoi(currentQuestion);

    }

    private void kiemTraCauTraLoi(Question question, String cauTraLoi) {
        // kiểm tra đúng sai  ghi ở class Question 

        if (question.checkAnswer(cauTraLoi)) {
            System.out.println("Trả lời đúng");

            this.numberCorrectQuestion++;
            newQuestion();

        } else {
            System.out.println("Trả lời sai");
            //JOptionPane.showMessageDialog(this, "Đáp án của bạn không đúng");
            UIEndGame end = new UIEndGame(name, numberCorrectQuestion);
            this.setVisible(false);
            end.setVisible(true);
        }
    }

    private boolean kiemTraDapAn(Question question, String cauTraLoi) {
        return question.checkAnswer(cauTraLoi);
    }

    private void kiemTra(Question question, String cauTraLoi, int dapAn) {
        dongHo.tamNgung();
        boolean ketQua = kiemTraDapAn(question, cauTraLoi);
        MayPhatNhac.getInstance().play(audioOfAnswers[dapAn], new MayPhatNhac.MayPhatNhacListener() {
            @Override
            public void playDidFinish() {
                showKetQua(ketQua, dapAn);
            }
        });
    }

    private void showKetQua(boolean ketQua, int dapAn) {
        MayPhatNhac.getInstance().play("ans_now1.mp3", new MayPhatNhac.MayPhatNhacListener() {
            @Override
            public void playDidFinish() {
                if (ketQua) {
                    MayPhatNhac.getInstance().play(audioOfTrueAnswers[dapAn], null);
                } else {
                    MayPhatNhac.getInstance().play(audioOfLoseAnswers[getIndexDapAnDung(currentQuestion)], null);
                }
                //
                showButtonDung(getIndexDapAnDung(currentQuestion));
            }
        });
    }
    
    private int getIndexDapAnDung(Question question) {
        for(int i = 0; i < buttons.length; i++) {
            if(buttons[i].getText().compareTo(question.getCorrectAnswer()) == 0){
                return i;
            }
        }
        return 0;
    }
    
    private void showButtonDung(int dapAnDung) {
        buttons[dapAnDung].setBackground(Color.green);
    }
    
    private void showTienThuong(int index) {
        
    }

    @Override
    public void thoiGianConLai(int thoiGianConLai) {
        // e chi can cho no hien thi len 1 label thoi.
        // no se k lam luon viec hien thi label. ma no chi don gian dua cho 1 gia tri thoi gian con lai.
        // e muon lam gi tiep theo thi lam.
        // có những việc e k biết khi nào nó hoàn thành. thì phải dùng event như thế này.
        // ok?e vua dinh hoi a v dung event khi nao? hhh a ns roi.ok a.
        // e co can a ra vi du de e thu lam k.co a. chu e chua hieu lam ve event .
        // Youtuber và người dùng. Khi người dùng đăng kí kênh của Youtuber.
        // thì khi youtuber ra video mới. người dùng đc nhận thông báo. e ok với đề bài chưa.ok a.
        // ok e. mai a coi thu nhe. bye e.bye a.a nn.
        lblTime.setText(Integer.toString(thoiGianConLai));
        System.out.println(thoiGianConLai);
    }

    @Override
    public void hetGio() {
        // het gio la coi nhu thua nhe
        // tuong tu nhu cai nay.
        JOptionPane.showMessageDialog(this, "Het gio");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel3 = new javax.swing.JPanel();
        lblCauHoi = new javax.swing.JLabel();
        btnQuestion5 = new javax.swing.JButton();
        btnD = new javax.swing.JButton();
        btnC = new javax.swing.JButton();
        btnA = new javax.swing.JButton();
        btnB = new javax.swing.JButton();
        lblTime = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        lbl200 = new javax.swing.JLabel();
        lbl400 = new javax.swing.JLabel();
        lbl600 = new javax.swing.JLabel();
        lbl1trieu = new javax.swing.JLabel();
        lbl2trieu = new javax.swing.JLabel();
        lbl3trieu = new javax.swing.JLabel();
        lbl6trieu = new javax.swing.JLabel();
        lbl10trieu = new javax.swing.JLabel();
        lbl14trieu = new javax.swing.JLabel();
        lbl22trieu = new javax.swing.JLabel();
        lbl30trieu = new javax.swing.JLabel();
        lbl40trieu = new javax.swing.JLabel();
        lbl60trieu = new javax.swing.JLabel();
        lbl85trieu = new javax.swing.JLabel();
        lbl150trieu = new javax.swing.JLabel();
        lblQuestion = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("WELCOM TO GAME AI LA TRIEU PHU");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel3.setOpaque(false);

        lblCauHoi.setBackground(new java.awt.Color(0, 153, 153));
        lblCauHoi.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lblCauHoi.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCauHoi.setBorder(new javax.swing.border.MatteBorder(null));
        lblCauHoi.setOpaque(true);
        lblCauHoi.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                lblCauHoiAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        btnQuestion5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btnQuestion5.setText("Question");
        btnQuestion5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuestion5ActionPerformed(evt);
            }
        });

        btnD.setBackground(new java.awt.Color(0, 204, 204));
        btnD.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/btnD.png"))); // NOI18N
        btnD.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDActionPerformed(evt);
            }
        });

        btnC.setBackground(new java.awt.Color(0, 204, 204));
        btnC.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/btnC.png"))); // NOI18N
        btnC.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCActionPerformed(evt);
            }
        });

        btnA.setBackground(new java.awt.Color(0, 204, 204));
        btnA.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnA.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/btnA.png"))); // NOI18N
        btnA.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnA.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAActionPerformed(evt);
            }
        });

        btnB.setBackground(new java.awt.Color(0, 204, 204));
        btnB.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/btnB.png"))); // NOI18N
        btnB.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnB.setPreferredSize(new java.awt.Dimension(47, 23));
        btnB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBActionPerformed(evt);
            }
        });

        lblTime.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTime.setForeground(new java.awt.Color(255, 51, 0));
        lblTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTime.setText("s");
        lblTime.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                lblTimeAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "PLAYER", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 12), new java.awt.Color(255, 0, 51))); // NOI18N

        jLabel20.setText("Name: ");

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/player.png"))); // NOI18N

        lblName.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(lblName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(jLabel20))
                        .addGap(0, 49, Short.MAX_VALUE))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel20)
                        .addGap(18, 18, 18)
                        .addComponent(lblName, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel16))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/5050.png"))); // NOI18N

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/call.png"))); // NOI18N

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/khan gia.png"))); // NOI18N

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 153, 0));
        jLabel1.setText("1:");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 153, 0));
        jLabel2.setText("2: ");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 153, 0));
        jLabel3.setText("3: ");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 153, 0));
        jLabel4.setText("4:");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 153, 0));
        jLabel5.setText("5:");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 153, 0));
        jLabel6.setText("6:");

        jLabel7.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 153, 0));
        jLabel7.setText("7:");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 153, 0));
        jLabel8.setText("8:");

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 153, 0));
        jLabel9.setText("9:");

        jLabel10.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 153, 0));
        jLabel10.setText("10:");

        jLabel11.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 153, 0));
        jLabel11.setText("11:");

        jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 153, 0));
        jLabel12.setText("12:");

        jLabel13.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 153, 0));
        jLabel13.setText("13:");

        jLabel14.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 153, 0));
        jLabel14.setText("14:");

        jLabel15.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 153, 0));
        jLabel15.setText("15:");

        lbl200.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lbl200.setForeground(new java.awt.Color(255, 255, 255));
        lbl200.setText("200.000");

        lbl400.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lbl400.setForeground(new java.awt.Color(255, 255, 255));
        lbl400.setText("400.000");

        lbl600.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lbl600.setForeground(new java.awt.Color(255, 255, 255));
        lbl600.setText("600.000");

        lbl1trieu.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lbl1trieu.setForeground(new java.awt.Color(255, 255, 255));
        lbl1trieu.setText("1.000.000");

        lbl2trieu.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lbl2trieu.setForeground(new java.awt.Color(255, 255, 255));
        lbl2trieu.setText("2.000.000");

        lbl3trieu.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lbl3trieu.setForeground(new java.awt.Color(255, 255, 255));
        lbl3trieu.setText("3.000.000");

        lbl6trieu.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lbl6trieu.setForeground(new java.awt.Color(255, 255, 255));
        lbl6trieu.setText("6.000.000");

        lbl10trieu.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lbl10trieu.setForeground(new java.awt.Color(255, 255, 255));
        lbl10trieu.setText("10.000.000");

        lbl14trieu.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lbl14trieu.setForeground(new java.awt.Color(255, 255, 255));
        lbl14trieu.setText("14.000.000");

        lbl22trieu.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lbl22trieu.setForeground(new java.awt.Color(255, 255, 255));
        lbl22trieu.setText("22.000.000");

        lbl30trieu.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lbl30trieu.setForeground(new java.awt.Color(255, 255, 255));
        lbl30trieu.setText("30.000.000");

        lbl40trieu.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lbl40trieu.setForeground(new java.awt.Color(255, 255, 255));
        lbl40trieu.setText("40.000.000");

        lbl60trieu.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lbl60trieu.setForeground(new java.awt.Color(255, 255, 255));
        lbl60trieu.setText("60.000.000");

        lbl85trieu.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lbl85trieu.setForeground(new java.awt.Color(255, 255, 255));
        lbl85trieu.setText("85.000.000");

        lbl150trieu.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lbl150trieu.setForeground(new java.awt.Color(255, 255, 255));
        lbl150trieu.setText("150.000.000");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl1trieu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl600, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl400, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addComponent(lbl200, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(lbl10trieu, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel12))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbl3trieu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lbl2trieu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl6trieu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel11)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl40trieu, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lbl14trieu, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel13))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lbl30trieu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lbl22trieu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lbl60trieu, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                            .addComponent(lbl85trieu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbl150trieu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(24, 24, 24))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel5)
                    .addComponent(jLabel9)
                    .addComponent(jLabel13)
                    .addComponent(lbl200)
                    .addComponent(lbl2trieu)
                    .addComponent(lbl14trieu)
                    .addComponent(lbl60trieu))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel6)
                    .addComponent(jLabel10)
                    .addComponent(jLabel14)
                    .addComponent(lbl400)
                    .addComponent(lbl3trieu)
                    .addComponent(lbl22trieu)
                    .addComponent(lbl85trieu))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel7)
                    .addComponent(jLabel11)
                    .addComponent(jLabel15)
                    .addComponent(lbl600)
                    .addComponent(lbl6trieu)
                    .addComponent(lbl30trieu)
                    .addComponent(lbl150trieu))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jLabel8)
                        .addComponent(jLabel12)
                        .addComponent(lbl1trieu)
                        .addComponent(lbl10trieu))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(lbl40trieu)
                        .addContainerGap())))
        );

        lblQuestion.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lblQuestion.setForeground(new java.awt.Color(255, 255, 0));
        lblQuestion.setText("LABEL");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(btnQuestion5)
                                .addGap(10, 10, 10))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(lblQuestion)
                                .addGap(29, 29, 29)))
                        .addComponent(lblCauHoi, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnB, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnA, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnC, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(lblTime, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnD, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(48, 48, 48))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCauHoi, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(btnQuestion5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblQuestion, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnB, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnA, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(lblTime)
                                .addGap(63, 63, 63))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnD, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnC, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22))))))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(jPanel3, gridBagConstraints);

        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/trieuphu2.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(jLabel19, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblCauHoiAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblCauHoiAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_lblCauHoiAncestorAdded

    private void btnQuestion5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuestion5ActionPerformed

        if (this.numberCorrectQuestion == 5) {
            count = 0;
            taoCacCauHoi(2);
        }
        if (this.numberCorrectQuestion == 7) {
            count = 0;
            taoCacCauHoi(3);
        }
        if (this.numberCorrectQuestion == 10) {
            count = 0;
            taoCacCauHoi(4);
        }
        currentQuestion = questions.get(questionIndex[count]);
        count++;
        hienThiCauHoi(currentQuestion);
    }//GEN-LAST:event_btnQuestion5ActionPerformed

    private void btnDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDActionPerformed
        String cauTraLoi = btnD.getText();
        btnD.setBackground(Color.orange);
        kiemTra(currentQuestion, cauTraLoi, 3);
    }//GEN-LAST:event_btnDActionPerformed

    private void btnCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCActionPerformed
        String cauTraLoi = btnC.getText();
        btnC.setBackground(Color.orange);
        kiemTra(currentQuestion, cauTraLoi, 2);
    }//GEN-LAST:event_btnCActionPerformed

    private void btnAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAActionPerformed
        // giờ sẽ kiểm tra xem đáp án người chơi chọn đúng k nhé.
        String cauTraLoi = btnA.getText();
        btnA.setBackground(Color.orange);
        kiemTra(currentQuestion, cauTraLoi, 0);
    }//GEN-LAST:event_btnAActionPerformed

    private void btnBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBActionPerformed
        String cauTraLoi = btnB.getText();
        btnB.setBackground(Color.orange);
        kiemTra(currentQuestion, cauTraLoi, 1);
    }//GEN-LAST:event_btnBActionPerformed

    private void lblTimeAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblTimeAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_lblTimeAncestorAdded

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UIQuestion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UIQuestion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UIQuestion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UIQuestion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new UIQuestion().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnA;
    private javax.swing.JButton btnB;
    private javax.swing.JButton btnC;
    private javax.swing.JButton btnD;
    private javax.swing.JButton btnQuestion5;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel lbl10trieu;
    private javax.swing.JLabel lbl14trieu;
    private javax.swing.JLabel lbl150trieu;
    private javax.swing.JLabel lbl1trieu;
    private javax.swing.JLabel lbl200;
    private javax.swing.JLabel lbl22trieu;
    private javax.swing.JLabel lbl2trieu;
    private javax.swing.JLabel lbl30trieu;
    private javax.swing.JLabel lbl3trieu;
    private javax.swing.JLabel lbl400;
    private javax.swing.JLabel lbl40trieu;
    private javax.swing.JLabel lbl600;
    private javax.swing.JLabel lbl60trieu;
    private javax.swing.JLabel lbl6trieu;
    private javax.swing.JLabel lbl85trieu;
    private javax.swing.JLabel lblCauHoi;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblQuestion;
    private javax.swing.JLabel lblTime;
    // End of variables declaration//GEN-END:variables

}
