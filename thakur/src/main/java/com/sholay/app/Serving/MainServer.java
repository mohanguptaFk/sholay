package com.sholay.app.Serving;

import com.sholay.app.Utils;
import com.sholay.app.engine.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mohan.gupta on 21/07/16.
 */
public class MainServer extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(MainServer.class);
    private static final long serialVersionUID = 1L;

    public MainServer() {

    }

    @Override
    protected void doGet(final HttpServletRequest req,
                         final HttpServletResponse res) throws ServletException, IOException {

        try {
            String output = "";


            String startTime= req.getParameter("startTime");
            String endTime=req.getParameter("endTime");
            String lat= req.getParameter("lat");
            String lng= req.getParameter("lng");

            String inout= req.getParameter("inout");
            String chillAdvent= req.getParameter("chillAdvent");
            String familyElse = req.getParameter("familyElse");
            String[] keywords = req.getParameterValues("keywords[]");

            if (Utils.tryParseLong(startTime) != null && Utils.tryParseLong(endTime) != null) {
                output = Controller.getEventsResponse(Utils.tryParseLong(startTime),
                        Utils.tryParseLong(endTime),
                        lat,
                        lng,
                        inout,
                        chillAdvent,
                        familyElse,
                        keywords);
            }
            else {
                LOG.error("Starttime and endtime is must !!");
            }

            writeString(res, output);
        }
        catch (Exception e) {
            LOG.error(e.toString() + " stack = " + e.getStackTrace().toString());
        }
    }

    private void writeString(HttpServletResponse res, String str) throws IOException{
        res.getWriter().append(str);
    }
}
