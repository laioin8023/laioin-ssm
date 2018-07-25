package com.ssm.web.base;

import com.ssm.common.utils.JsonUtil;
import com.ssm.common.utils.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * 自己处理，请求
 */
public class SsmDispatcherServlet extends DispatcherServlet {

    private Logger LGR = LoggerFactory.getLogger(SsmDispatcherServlet.class);

    @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String url = request.getRequestURI();
        String ip = Tools.getIp(request);
        LGR.info("==================================================== {} start =====================================================================",url);
       /*
        处理，授权，什么接口，必须授权
        HandlerExecutionChain mappedHandler = getHandler(request);
        if (null != mappedHandler) {
            if (mappedHandler.getHandler() instanceof HandlerMethod) {
                HandlerMethod hm = (HandlerMethod) mappedHandler.getHandler();
                if (hm.getBean() instanceof INeedToken) {  // 需要token

                }
            }
        }*/
        Map<String, String[]> paramMap = request.getParameterMap();
        if (null != paramMap && paramMap.size() > 0) {
            LGR.info("request params: [{}].", ip, url, JsonUtil.gsonString(paramMap));
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ServletInputStream is = null;
        try {
            is = request.getInputStream();
            byte[] item = new byte[1024];
            int len;
            while ((len = is.read(item)) != -1) {
                out.write(item, 0, len);
            }
            String requestData = new String(out.toByteArray(), "utf-8");

            if (!Tools.isEmpty(requestData)) {
                LGR.info("IP = [{}{}] request params: [{}].", ip, url, requestData);
            }
        } catch (Exception e) {
            LGR.error("get params error : ", e);
        }
        SsmRequestWrapper meRequest = new SsmRequestWrapper(request, out);
        doDispatch(meRequest, response);
        LGR.info("==================================================== {} end =====================================================================",url);
    }
}
