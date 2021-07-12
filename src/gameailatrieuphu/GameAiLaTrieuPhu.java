/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameailatrieuphu;

/**
 *
 * @author midor
 */
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.beans.Statement;
import java.sql.ResultSet;
import java.sql.Connection;

import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import Models.Question;
import Network.SQLServer;
import helper.MayPhatNhac;

public class GameAiLaTrieuPhu {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MayPhatNhac.getInstance().play("ans_d.mp3", new MayPhatNhac.MayPhatNhacListener() {

            @Override
            public void playDidFinish() {
                System.out.println(".playDidFinish()");
            }
        });
        System.out.println("done 4");
        // main -> done 1.
        //         thread t -> run
    }

}
