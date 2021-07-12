/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 *
 * @author midor
 */
// khi e tao 1 class nhu the nayf thi e co the tai su dung dc no.
public class DongHoDemNguoc implements ActionListener{
    // dong ho dem nguoc e nghi se co thuoc tinh gi.thời gian a. cai thoi gian nay la thoi gian minh cai nhe,
    private int thoiGian;
    private int thoiGianConLai; 
    // dung timer ne.
    // 1000 tuong duong voi 1s nhe. co nghia la cu 1s troi qua nos se thuc hien ham actionPerformed 1 lan.
    // ok?ok a. // day la 1 cach dua actionListner vo. c2 la
    // e ok voi 2 cach chua.roi a.
    private Timer timer = new Timer(1000, this);
    private DongHoDemNguocListener listener;

    // tao thoi gian ban dau.
    public DongHoDemNguoc(int thoiGian) {
        this.thoiGian = thoiGian;
        this.thoiGianConLai = thoiGian; // duong nhien lucs dau thoi gian con lai bang dung thoi gian luon
    }

    public void setListener(DongHoDemNguocListener listener) {
        this.listener = listener;
    }

    public void setThoiGianConLai(int thoiGianConLai) {
        this.thoiGianConLai = thoiGianConLai;
        
        // bao thoi gian con lai
        if(listener != null) {
            listener.thoiGianConLai(thoiGianConLai);
        }
        // sau khi set xong thoi gian. minh se bao ra ben ngoai biet con bao nhieu thoi gian.
        // va khi thoi gian con lai bang 0 se bao ra la het gio. e ok 2 viec nay chua?ok a.
        // kiem tra het gio chua.
        // e ok het voi cac cong viec nay chua.ok a.
        if(thoiGianConLai == 0){
            // dung cai timer lai luon nha
            timer.stop();
            if(listener != null) {
                listener.hetGio();
            }
        }
        
    }
    
    // e hieu het may method nay chu.hieu a.
    public void batDauBamGio() {
        // bat dau bam gio se cho timer chay.
        if(timer.isRunning()){
            khoiDongLai();
        }
        else timer.start();
    }
    
    public void tamNgung() {
        timer.stop();
    }
    
    public void khoiDongLai() {
        // khoi dong lai thi minh dat lai thoi gian nhe.
        timer.stop();
        thoiGianConLai = thoiGian; // gan lai thoi gian con lai ne.
        batDauBamGio();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // duong nhien khi 1s troi qua thi minh se lam gi e. 1s troi qua nhe. minh lam gi e?
        // dau tien thoiGianConLai = 15. sau 1s thi no bang nhieu .e. 14 a. v cai minh can lam khi 1s troi 
        // qua la gi.tinh thoi gian con lai a. dunsg r nha. phai set lai thoi gian con lai.
        setThoiGianConLai(thoiGianConLai - 1); 
    }
    
    public interface DongHoDemNguocListener {
        // ham dau tien. bao con lai bao nhieu thoi gian
        void thoiGianConLai(int thoiGianConLai);
        // ham thu 2. het gio
        void hetGio();
    }
}
