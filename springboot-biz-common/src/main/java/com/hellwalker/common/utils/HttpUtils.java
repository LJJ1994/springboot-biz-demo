package com.hellwalker.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {
    public static void responseJson(HttpServletResponse response, HttpStatus httpStatus,
                                      int code, String msg) {
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(httpStatus.value());
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", code);
        resultMap.put("msg", msg);
        String jsonStr = "";
        try {
            jsonStr = new ObjectMapper().writeValueAsString(resultMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            jsonStr = e.getMessage();
        }
        if (out != null) {
            out.write(jsonStr);
            out.flush();
            out.close();
        }
    }

    public static void responseJsonOK(HttpServletResponse response,
                                    int code, String msg) {
        responseJson(response, HttpStatus.OK, code, msg);
    }

    public static void responseJsonFailed(HttpServletResponse response,
                                      int code, String msg) {
        responseJson(response, HttpStatus.BAD_REQUEST, code, msg);
    }

    /**
     * 从 HttpServletRequest 获取表单或者json类型的参数
     * @param request
     * @param paramName
     * @return
     */
    public static String getParamFromJsonOrForm(HttpServletRequest request, String paramName) {
        String param = request.getParameter(paramName);
        if (StringUtils.isEmpty(param)) {
            JSONObject jsonFromRequest = getJsonFromRequest(request);
            if (jsonFromRequest != null) {
                Object paramFromJson = jsonFromRequest.get(paramName);
                if (paramFromJson != null) {
                    param = (String) paramFromJson;
                }
            }
        }
        return param;
    }


    /**
     * 从HttpServletRequest获取json请求数据
     * @param request
     * @return
     */
    public static JSONObject getJsonFromRequest(HttpServletRequest request) {
        JSONObject jsonObject = null;
        try {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);

            jsonObject = JSONObject.parseObject(responseStrBuilder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
