/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.filter;

import entity.Employee;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import util.enumeration.AccessRightEnum;

/**
 *
 * @author CHEN BINGSEN
 */
@WebFilter(filterName = "SecurityFilter", urlPatterns = {"/*"})
public class SecurityFilter implements Filter 
{
    
    FilterConfig filterConfig;
    
    private static final String CONTEXT_ROOT = "/PaintSalesSystem-war";
    
    public void init(FilterConfig filterConfig) throws ServletException
    {
        this.filterConfig = filterConfig;
    }
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;
        HttpSession httpSession = httpServletRequest.getSession(true);
        String requestServletPath = httpServletRequest.getServletPath();
        
        if(httpSession.getAttribute("isLogin") == null)
        {
            httpSession.setAttribute("isLogin", false);
        }
        
        Boolean isLogin = (Boolean)httpSession.getAttribute("isLogin");
        
        if(!excludeLoginCheck(requestServletPath))
        {
            if(isLogin == true)
            {
                Employee currentEmployee = (Employee)httpSession.getAttribute("currentEmployeeEntity");
                if(checkAccessRight(requestServletPath, currentEmployee.getAccessRightEnum()))
                {
                    chain.doFilter(request, response);
                }
                else
                {
                    httpServletResponse.sendRedirect(CONTEXT_ROOT + "/accessRightError.xhtml");
                }
            }
            else
            {
                httpServletResponse.sendRedirect(CONTEXT_ROOT + "/accessRightError.xhtml");
            }
        }
        else
        {
            chain.doFilter(request, response);
        }
    }
    
    private Boolean checkAccessRight(String path, AccessRightEnum accessRight)
    {
        if(accessRight.equals(AccessRightEnum.NORMAL))
        {
            if(path.equals("/paintManagement/filterPaintsByCategories.xhtml") ||
                    path.equals("/paintManagement/filterPaintsByTags.xhtml") ||
                    path.equals("/paintManagement/paintManagement.xhtml") ||
                    path.equals("/paintManagement/paintCategoryManagement.xhtml") ||
                    path.equals("/paintManagement/paintTagManagement.xhtml") ||
                    path.equals("/paintManagement/searchPaintsByColourCode.xhtml") ||
                    path.equals("/deliveryManagement/deliveryManagement.xhtml") ||
                    path.equals("/deliveryManagement/filterDelivery.xhtml") ||
                    path.equals("/paintServiceManagement/paintServiceManagement.xhtml") ||
                    path.equals("/paintServiceManagement/filterPaintService.xhtml") ||
                    path.equals("/systemAdministration/customerManagement.xhtml") ||
                    path.equals("/transactionManagement/transactionManagement.xhtml"))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if(accessRight.equals(AccessRightEnum.MANAGER))
        {
            if(path.equals("/paintManagement/filterPaintsByCategories.xhtml") ||
                    path.equals("/paintManagement/filterPaintsByTags.xhtml") ||
                    path.equals("/paintManagement/paintManagement.xhtml") ||
                    path.equals("/paintManagement/searchPaintsByColourCode.xhtml") ||
                    path.equals("/paintManagement/paintCategoryManagement.xhtml") ||
                    path.equals("/paintManagement/paintTagManagement.xhtml") ||
                    path.equals("/deliveryManagement/deliveryManagement.xhtml") ||
                    path.equals("/deliveryManagement/filterDelivery.xhtml") ||
                    path.equals("/paintServiceManagement/paintServiceManagement.xhtml") ||
                    path.equals("/paintServiceManagement/filterPaintService.xhtml") ||
                    path.equals("/systemAdministration/employeeManagement.xhtml") ||
                    path.equals("/systemAdministration/customerManagement.xhtml") ||
                    path.equals("/transactionManagement/transactionManagement.xhtml"))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        
        return false;
    }
    
    public void destroy()
    {

    }
    
    
    private Boolean excludeLoginCheck(String path)
    {
        if(path.equals("/index.xhtml") ||
            path.equals("/accessRightError.xhtml") ||
            path.startsWith("/javax.faces.resource"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
}
