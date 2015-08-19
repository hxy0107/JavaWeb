package servlet;

import threadcrawler.MspUpdate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by xianyu.hxy on 2015/8/17.
 */
@WebServlet("/TableUpdateServlet")
public class TableUpdateServlet extends HttpServlet {
    String s=null;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        s=request.getParameter("tableupdate_servlet");
        new Thread(new Runnable() {
            @Override
            public void run() {
                MspUpdate.mspUpdate();
            }
        }).start();

        response.sendRedirect("/index.jsp");
    }
}
