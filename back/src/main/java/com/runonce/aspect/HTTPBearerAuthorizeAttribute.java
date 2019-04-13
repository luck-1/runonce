package com.runonce.aspect;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.runonce.model.system.User;
import com.runonce.service.system.UserService;
import com.runonce.util.WebTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.runonce.ReturnCode;



/** 
* @author  yf
* Http拦截器，处理webtoken
* @date 创建时间：2017年12月1日 下午6:55:55 
* @version 1.0   
*/
public class HTTPBearerAuthorizeAttribute implements Filter {

	@Autowired
	private UserService userService;


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, filterConfig.getServletContext());

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;

		// 登录和测试接口放行，其余接口全部监听
		boolean isSwagger = httpRequest.getRequestURL().toString().contains("swagger");
		boolean isSwaggerApi = httpRequest.getRequestURL().toString().contains("api-docs");
		boolean isLogin = httpRequest.getRequestURL().toString().endsWith("login");
		boolean isExit = httpRequest.getRequestURL().toString().endsWith("exit");
		boolean isDruid = httpRequest.getRequestURL().toString().contains("druid");
		boolean isDownload = httpRequest.getRequestURL().toString().contains("download");
		if (isLogin||isExit || isSwagger || isSwaggerApi||isDownload||isDruid) {
			chain.doFilter(request, response);
			return;
		}

		boolean isNeddLogin=false;
		String webtoken = httpRequest.getHeader("x-access-token");// 从请求header中拿出webtoken
		if ((webtoken != null) && (webtoken.length() > 7)) {
			String userId=WebTokenUtil.getUserByWebToken(httpRequest);

			if(userId!=null){

				User user  =  userService.get(userId);
				if (user == null || user.getDelFlag() != 0) {

					HttpServletResponse httpResponse = (HttpServletResponse) response;
					httpResponse.setCharacterEncoding("UTF-8");
					httpResponse.setContentType("application/json; charset=utf-8");
					httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

					ObjectMapper mapper = new ObjectMapper();
					//((HttpServletResponse) response).sendRedirect("");
					//直接重定向
					ReturnCode msg = new ReturnCode();
					msg.setCode(HttpServletResponse.SC_UNAUTHORIZED);
					msg.setMsg("用户名不存在");
					httpResponse.getWriter().write(mapper.writeValueAsString(msg));
					return;
				}
				if (user.getStatus() == -1){

					HttpServletResponse httpResponse = (HttpServletResponse) response;
					httpResponse.setCharacterEncoding("UTF-8");
					httpResponse.setContentType("application/json; charset=utf-8");
					httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

					ObjectMapper mapper = new ObjectMapper();
					//((HttpServletResponse) response).sendRedirect("");
					//直接重定向
					ReturnCode msg = new ReturnCode();
					msg.setCode(HttpServletResponse.SC_UNAUTHORIZED);
					msg.setMsg("用户名已被禁用");
					httpResponse.getWriter().write(mapper.writeValueAsString(msg));
					return;
				}
				isNeddLogin=false;
				chain.doFilter(request, response);
				return;
			}else{
				isNeddLogin=true;

			}
/*			SysUser sysUser = MeasuringUtil.getUserByWebToken(httpRequest);
			sysUser = GetRedisData.GetUser(sysUser.getfId());
			if (sysUser != null)// 有缓存数据时表示请求合法
			{
                chain.doFilter(request, response);
                return;
			}*/
		}else
		{

			isNeddLogin=true;
		}

		if(isNeddLogin) {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setCharacterEncoding("UTF-8");
			httpResponse.setContentType("application/json; charset=utf-8");
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			ObjectMapper mapper = new ObjectMapper();


			//((HttpServletResponse) response).sendRedirect("");
			//直接重定向
			ReturnCode msg = new ReturnCode();
			msg.setCode(HttpServletResponse.SC_UNAUTHORIZED);
			msg.setMsg("未登陆或登陆超时");
			httpResponse.getWriter().write(mapper.writeValueAsString(msg));
			return;
		}
	}

	@Override
	public void destroy() {
	}
}
