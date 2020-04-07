package io.dfjinxin.common.govcafilter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.*;
import java.io.IOException;


public class GovCaFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(GovCaFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding("gbk");//赶在被utf8之前……先gbk了
        servletRequest.getParameter("BJCA_TICKET");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void destroy() {
    }

}
