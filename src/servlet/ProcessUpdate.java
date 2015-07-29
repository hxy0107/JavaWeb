package servlet;

import automation.AutoProcess;
import automation.AutoUpdate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by xianyu.hxy on 2015/7/28.
 */
@WebServlet("/ProcessUpdate")
public class ProcessUpdate extends HttpServlet {
    String s="";
    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //  super.doGet(req, resp);
        // response.setIntHeader("Refresh", 3);
        s=request.getParameter("index_name_servlet");
        response.setContentType("text/html");

        // 实际的逻辑是在这里
        PrintWriter out = response.getWriter();
        out.println("<h1>" + "Checking update. ... Please be patient" + "</h1>");
        /*
        int result= AutoUpdate.main(s);
        if(result==0){
            System.out.println("当前版本应用是最新");
            out.println("<h1>" + "The current version is already up to date" + "</h1>");
        }*/

        new Thread(new Runnable() {
            @Override
            public void run() {


                int result= AutoProcess.main(s);
                if(result==0){
                    System.out.println("当前版本应用是最新");
                    out.println("<h1>" + "The current version is already up to date" + "</h1>");
                }else {
                    out.println("<h1>" + "The current version is not the latest, now updated to the latest" + "</h1>");
                }
            }
        }).start();
        response.sendRedirect("/search_app.jsp");
    }
}
