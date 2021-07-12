/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

/**
 *
 * @author midor
 */
// để a copy code qua.
public class MayPhatNhac {

    private static MayPhatNhac singleton = null;
    // đây là đường dẫn tới mấy file âm thanh của e nha.
    private final String audiosPath = "E:\\HOCKIHE\\JAVA\\GameAiLaTrieuPhu\\src\\Audios\\%s";
    private AdvancedPlayer player;
    private String path;

    private MayPhatNhac() {

    }

    public static MayPhatNhac getInstance() {
        if (singleton == null) {
            singleton = new MayPhatNhac();
        }
        return singleton;
    }

    public void stop() {
        if (player != null) {
            player.close();
        }
    }

    public void play(String fileName, MayPhatNhacListener completion) {
        path = String.format(audiosPath, fileName);
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
            player = new AdvancedPlayer(bis);
            player.setPlayBackListener(new PlaybackListener() {
                @Override
                public void playbackFinished(PlaybackEvent evt) {
                    super.playbackFinished(evt);
                    if (completion != null) {
                        completion.playDidFinish();
                    }
                }
            });
        } catch (Exception ex) {
            System.out.println(ex);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    player.play();
                } catch (Exception ex) {
                    System.out.println(".run()");
                    System.out.println(ex);
                }
            }
        }).start();
    }

    public interface MayPhatNhacListener {

        void playDidFinish();
    }
}
