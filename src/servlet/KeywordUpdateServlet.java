package servlet;

import threadcrawler.AliMsp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by xianyu.hxy on 2015/8/17.
 */
@WebServlet("/KeywordUpdateServlet")
public class KeywordUpdateServlet extends HttpServlet {
    String appName=null;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        appName=request.getParameter("alimsp_servlet");
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("keyword:"+appName);
                AliMsp.update(appName);
            }
        }).start();
        response.sendRedirect("/index.jsp");
    }
}
