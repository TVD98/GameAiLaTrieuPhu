/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import Models.Question;

/**
 *
 * @author midor
 */
public class SQLServer {

    private String databaseName = "AiLaTrieuPhu";
    private String user = "sa";
    private String password = "123";
    private String dbURL;

    public SQLServer() {
        // tao duong dan ket noi toi database
        dbURL = String.format("jdbc:sqlserver://localhost;databaseName=%s;user=%s;password=%s", databaseName,
                user, password);
    }

    public List<Question> getQuestionList(int lv) {

        try {
            Connection conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                System.out.println("Connect sucessfully");

                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("select * from Questions where level=" + lv);

                List<Question> questionList = new ArrayList<>();
                while (rs.next()) {
                    String title = rs.getString(1);
                    String correctAnswer = rs.getString(2);
                    String otherAnswer1 = rs.getString(3);
                    String otherAnswer2 = rs.getString(4);
                    String otherAnswer3 = rs.getString(5);
                    int level = rs.getInt(6);

                    Question questions = new Question(title, correctAnswer, otherAnswer1, otherAnswer2, otherAnswer3, level);
                    questionList.add(questions);
                }
                return questionList;
            }
        } catch (SQLException ex) {
            System.err.println("Cannot connect database, " + ex);
        }

        return null;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDbURL(String dbURL) {
        this.dbURL = dbURL;
    }

}
